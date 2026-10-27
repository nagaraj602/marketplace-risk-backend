package com.marketplacerisk.service;

import com.marketplacerisk.model.ModelScoreResponse;
import com.marketplacerisk.model.RiskCase;
import com.marketplacerisk.model.RiskEvaluationResult;
import com.marketplacerisk.model.SellerSignal;
import com.marketplacerisk.repository.RiskCaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class RiskAggregationService {

    private static final Logger log = LoggerFactory.getLogger(RiskAggregationService.class);

    private final VelocityRuleEngine velocityRuleEngine;
    private final AnomalyModelClient anomalyModelClient;
    private final RiskCaseRepository caseRepository;
    private final EnforcementOrchestrationService enforcementService;

    @Value("${risk.threshold.auto-enforce:85.0}")
    private double autoEnforceThreshold;

    @Value("${risk.threshold.analyst-review:50.0}")
    private double analystReviewThreshold;

    public RiskAggregationService(VelocityRuleEngine velocityRuleEngine,
                                  AnomalyModelClient anomalyModelClient,
                                  RiskCaseRepository caseRepository,
                                  EnforcementOrchestrationService enforcementService) {
        this.velocityRuleEngine = velocityRuleEngine;
        this.anomalyModelClient = anomalyModelClient;
        this.caseRepository = caseRepository;
        this.enforcementService = enforcementService;
    }

    public RiskEvaluationResult evaluateSellerRisk(SellerSignal signal) {
        log.info("Aggregating risk signals for sellerId={}", signal.getSellerId());

        // 1. Evaluate Real-Time Velocity Rules
        VelocityRuleEngine.VelocityEvaluation velResult = velocityRuleEngine.evaluate(signal);
        double velScore = velResult.getScore();

        // 2. Query Machine Learning Anomaly Model
        ModelScoreResponse mlResult = anomalyModelClient.scoreSeller(signal);
        double mlScoreNorm = (mlResult.getAnomalyScore() != null ? mlResult.getAnomalyScore() : 0.0) * 100.0;

        // 3. Composite Risk Calculation (Weighted: 45% Velocity + 55% ML Model)
        double compositeScore = Math.round(((velScore * 0.45) + (mlScoreNorm * 0.55)) * 10.0) / 10.0;

        // 4. Aggregate Flags
        List<String> combinedFlags = new ArrayList<>(velResult.getRuleHits());
        if (mlResult.getAnomalyFlags() != null) {
            combinedFlags.addAll(mlResult.getAnomalyFlags());
        }

        // 5. Determine Risk Tier
        String riskTier;
        if (compositeScore >= 80.0) {
            riskTier = "CRITICAL";
        } else if (compositeScore >= 60.0) {
            riskTier = "HIGH";
        } else if (compositeScore >= 40.0) {
            riskTier = "ELEVATED";
        } else {
            riskTier = "LOW";
        }

        // 6. Routing Decision Logic
        String routingDecision;
        String caseStatus;
        List<String> enforcementsTriggered = new ArrayList<>();

        if (compositeScore >= autoEnforceThreshold) {
            routingDecision = "AUTO_ENFORCE";
            caseStatus = "AUTO_ENFORCED";
        } else if (compositeScore >= analystReviewThreshold) {
            routingDecision = "ANALYST_REVIEW";
            caseStatus = "PENDING";
        } else {
            routingDecision = "DISMISSED";
            caseStatus = "DISMISSED";
        }

        // 7. Save Case to Persistence
        String sellerName = signal.getSellerName() != null ? signal.getSellerName() : "Seller-" + signal.getSellerId();
        RiskCase riskCase = new RiskCase(
                signal.getSellerId(),
                sellerName,
                compositeScore,
                riskTier,
                caseStatus,
                String.join(", ", combinedFlags),
                velScore,
                mlScoreNorm
        );
        riskCase = caseRepository.save(riskCase);

        // 8. Trigger Automated Enforcements if High Confidence
        if ("AUTO_ENFORCE".equals(routingDecision)) {
            enforcementsTriggered = enforcementService.triggerAutomatedEnforcement(
                    riskCase.getId(),
                    signal.getSellerId(),
                    riskTier,
                    combinedFlags
            );
        }

        // 9. Prepare Final Result
        RiskEvaluationResult result = new RiskEvaluationResult();
        result.setSellerId(signal.getSellerId());
        result.setCompositeRiskScore(compositeScore);
        result.setRiskTier(riskTier);
        result.setRoutingDecision(routingDecision);
        result.setDetectedFlags(combinedFlags);
        result.setVelocityScore(velScore);
        result.setAnomalyScore(mlScoreNorm);
        result.setCaseId(riskCase.getId());
        result.setEnforcementsTriggered(enforcementsTriggered);
        result.setTimestamp(LocalDateTime.now().toString());

        return result;
    }
}
