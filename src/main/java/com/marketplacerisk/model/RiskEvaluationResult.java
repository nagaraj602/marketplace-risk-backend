package com.marketplacerisk.model;

import java.util.List;
import java.util.Map;

public class RiskEvaluationResult {
    private String sellerId;
    private double compositeRiskScore;
    private String riskTier;
    private String routingDecision; // AUTO_ENFORCE, ANALYST_REVIEW, DISMISSED
    private List<String> detectedFlags;
    private double velocityScore;
    private double anomalyScore;
    private Long caseId;
    private List<String> enforcementsTriggered;
    private String timestamp;

    public RiskEvaluationResult() {}

    public String getSellerId() { return sellerId; }
    public void setSellerId(String sellerId) { this.sellerId = sellerId; }

    public double getCompositeRiskScore() { return compositeRiskScore; }
    public void setCompositeRiskScore(double compositeRiskScore) { this.compositeRiskScore = compositeRiskScore; }

    public String getRiskTier() { return riskTier; }
    public void setRiskTier(String riskTier) { this.riskTier = riskTier; }

    public String getRoutingDecision() { return routingDecision; }
    public void setRoutingDecision(String routingDecision) { this.routingDecision = routingDecision; }

    public List<String> getDetectedFlags() { return detectedFlags; }
    public void setDetectedFlags(List<String> detectedFlags) { this.detectedFlags = detectedFlags; }

    public double getVelocityScore() { return velocityScore; }
    public void setVelocityScore(double velocityScore) { this.velocityScore = velocityScore; }

    public double getAnomalyScore() { return anomalyScore; }
    public void setAnomalyScore(double anomalyScore) { this.anomalyScore = anomalyScore; }

    public Long getCaseId() { return caseId; }
    public void setCaseId(Long caseId) { this.caseId = caseId; }

    public List<String> getEnforcementsTriggered() { return enforcementsTriggered; }
    public void setEnforcementsTriggered(List<String> enforcementsTriggered) { this.enforcementsTriggered = enforcementsTriggered; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}
