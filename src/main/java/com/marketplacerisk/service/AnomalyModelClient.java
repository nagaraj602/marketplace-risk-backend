package com.marketplacerisk.service;

import com.marketplacerisk.model.ModelScoreResponse;
import com.marketplacerisk.model.SellerSignal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Collections;
import java.util.HashMap;

@Service
public class AnomalyModelClient {

    private static final Logger log = LoggerFactory.getLogger(AnomalyModelClient.class);
    private final RestClient restClient;

    @Value("${risk.model.url:http://localhost:8282}")
    private String modelUrl;

    public AnomalyModelClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public ModelScoreResponse scoreSeller(SellerSignal signal) {
        String targetUrl = modelUrl + "/score";
        try {
            log.info("Requesting anomaly inference for sellerId={} at {}", signal.getSellerId(), targetUrl);
            ModelScoreResponse response = restClient.post()
                    .uri(targetUrl)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(signal)
                    .retrieve()
                    .body(ModelScoreResponse.class);

            if (response != null) {
                return response;
            }
        } catch (Exception ex) {
            log.warn("Anomaly model service unreachable at {}. Activating resilient fallback profiler. Reason: {}",
                    targetUrl, ex.getMessage());
        }

        // Resilient Fallback profiler
        return fallbackEvaluation(signal);
    }

    private ModelScoreResponse fallbackEvaluation(SellerSignal s) {
        ModelScoreResponse fb = new ModelScoreResponse();
        fb.setSellerId(s.getSellerId());
        double score = 0.10;
        if (s.getListingsLastHour() > 20) score += 0.40;
        if (s.getUniqueDevicesCount() > 2) score += 0.25;
        if (s.isPayoutAccountChangedRecently()) score += 0.20;

        fb.setAnomalyScore(Math.min(1.0, score));
        fb.setIsAnomaly(score >= 0.60);
        fb.setRiskTier(score >= 0.75 ? "CRITICAL" : (score >= 0.50 ? "HIGH" : "LOW"));
        fb.setConfidence("fallback");
        fb.setAnomalyFlags(Collections.singletonList("FALLBACK_RULE_EVALUATION"));
        fb.setFeatureContributions(new HashMap<>());
        fb.setEvaluationLatencyMs(1.0);
        return fb;
    }
}
