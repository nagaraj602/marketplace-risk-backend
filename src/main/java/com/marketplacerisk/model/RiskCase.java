package com.marketplacerisk.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "risk_cases")
public class RiskCase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "seller_id")
    private String sellerId;

    @Column(name = "seller_name")
    private String sellerName;

    @Column(name = "risk_score", nullable = false)
    private Double riskScore;

    @Column(name = "risk_tier", nullable = false)
    private String riskTier; // CRITICAL, HIGH, ELEVATED, LOW

    @Column(name = "status", nullable = false)
    private String status; // PENDING, UNDER_REVIEW, RESOLVED, DISMISSED, AUTO_ENFORCED

    @Column(name = "anomaly_flags", length = 1000)
    private String anomalyFlags;

    @Column(name = "velocity_score")
    private Double velocityScore;

    @Column(name = "ml_anomaly_score")
    private Double mlAnomalyScore;

    @Column(name = "sla_deadline")
    private LocalDateTime slaDeadline;

    @Column(name = "decision_reason", length = 1000)
    private String decisionReason;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    public RiskCase() {}

    public RiskCase(String sellerId, String sellerName, Double riskScore, String riskTier,
                    String status, String anomalyFlags, Double velocityScore, Double mlAnomalyScore) {
        this.sellerId = sellerId;
        this.sellerName = sellerName;
        this.riskScore = riskScore;
        this.riskTier = riskTier;
        this.status = status;
        this.anomalyFlags = anomalyFlags;
        this.velocityScore = velocityScore;
        this.mlAnomalyScore = mlAnomalyScore;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        // 2-hour SLA for critical/high, 6-hour SLA for others
        int slaHours = ("CRITICAL".equalsIgnoreCase(riskTier) || "HIGH".equalsIgnoreCase(riskTier)) ? 2 : 6;
        this.slaDeadline = this.createdAt.plusHours(slaHours);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSellerId() { return sellerId; }
    public void setSellerId(String sellerId) { this.sellerId = sellerId; }

    public String getSellerName() { return sellerName; }
    public void setSellerName(String sellerName) { this.sellerName = sellerName; }

    public Double getRiskScore() { return riskScore; }
    public void setRiskScore(Double riskScore) { this.riskScore = riskScore; }

    public String getRiskTier() { return riskTier; }
    public void setRiskTier(String riskTier) { this.riskTier = riskTier; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getAnomalyFlags() { return anomalyFlags; }
    public void setAnomalyFlags(String anomalyFlags) { this.anomalyFlags = anomalyFlags; }

    public Double getVelocityScore() { return velocityScore; }
    public void setVelocityScore(Double velocityScore) { this.velocityScore = velocityScore; }

    public Double getMlAnomalyScore() { return mlAnomalyScore; }
    public void setMlAnomalyScore(Double mlAnomalyScore) { this.mlAnomalyScore = mlAnomalyScore; }

    public LocalDateTime getSlaDeadline() { return slaDeadline; }
    public void setSlaDeadline(LocalDateTime slaDeadline) { this.slaDeadline = slaDeadline; }

    public String getDecisionReason() { return decisionReason; }
    public void setDecisionReason(String decisionReason) { this.decisionReason = decisionReason; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
