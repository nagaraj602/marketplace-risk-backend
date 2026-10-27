package com.marketplacerisk.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ModelScoreResponse {
    @JsonAlias("seller_id")
    private String sellerId;

    @JsonAlias("anomaly_score")
    private Double anomalyScore;

    private String confidence;

    @JsonAlias("is_anomaly")
    private Boolean isAnomaly;

    @JsonAlias("risk_tier")
    private String riskTier;

    @JsonAlias("anomaly_flags")
    private List<String> anomalyFlags;

    @JsonAlias("feature_contributions")
    private Map<String, Object> featureContributions;

    @JsonAlias("evaluation_latency_ms")
    private Double evaluationLatencyMs;

    public ModelScoreResponse() {}

    public String getSellerId() { return sellerId; }
    public void setSellerId(String sellerId) { this.sellerId = sellerId; }

    public Double getAnomalyScore() { return anomalyScore; }
    public void setAnomalyScore(Double anomalyScore) { this.anomalyScore = anomalyScore; }

    public String getConfidence() { return confidence; }
    public void setConfidence(String confidence) { this.confidence = confidence; }

    public Boolean getIsAnomaly() { return isAnomaly; }
    public void setIsAnomaly(Boolean isAnomaly) { this.isAnomaly = isAnomaly; }

    public String getRiskTier() { return riskTier; }
    public void setRiskTier(String riskTier) { this.riskTier = riskTier; }

    public List<String> getAnomalyFlags() { return anomalyFlags; }
    public void setAnomalyFlags(List<String> anomalyFlags) { this.anomalyFlags = anomalyFlags; }

    public Map<String, Object> getFeatureContributions() { return featureContributions; }
    public void setFeatureContributions(Map<String, Object> featureContributions) { this.featureContributions = featureContributions; }

    public Double getEvaluationLatencyMs() { return evaluationLatencyMs; }
    public void setEvaluationLatencyMs(Double evaluationLatencyMs) { this.evaluationLatencyMs = evaluationLatencyMs; }
}
