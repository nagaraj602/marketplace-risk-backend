package com.marketplacerisk.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "enforcement_actions")
public class EnforcementAction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "case_id")
    private Long caseId;

    @Column(nullable = false, name = "seller_id")
    private String sellerId;

    @Column(nullable = false, name = "action_type")
    private String actionType; // SUSPEND_SELLER, TAKEDOWN_LISTINGS, HOLD_PAYOUT, REJECT_REVIEWS

    @Column(nullable = false, name = "status")
    private String status; // SUCCESS, PENDING_RETRY, FAILED

    @Column(name = "retry_count")
    private Integer retryCount = 0;

    @Column(name = "reason", length = 500)
    private String reason;

    @Column(name = "initiated_by")
    private String initiatedBy; // AUTOMATED_PIPELINE, ANALYST

    @Column(name = "executed_at")
    private LocalDateTime executedAt = LocalDateTime.now();

    public EnforcementAction() {}

    public EnforcementAction(Long caseId, String sellerId, String actionType, String status, String reason, String initiatedBy) {
        this.caseId = caseId;
        this.sellerId = sellerId;
        this.actionType = actionType;
        this.status = status;
        this.reason = reason;
        this.initiatedBy = initiatedBy;
        this.executedAt = LocalDateTime.now();
        this.retryCount = 0;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCaseId() { return caseId; }
    public void setCaseId(Long caseId) { this.caseId = caseId; }

    public String getSellerId() { return sellerId; }
    public void setSellerId(String sellerId) { this.sellerId = sellerId; }

    public String getActionType() { return actionType; }
    public void setActionType(String actionType) { this.actionType = actionType; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getRetryCount() { return retryCount; }
    public void setRetryCount(Integer retryCount) { this.retryCount = retryCount; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getInitiatedBy() { return initiatedBy; }
    public void setInitiatedBy(String initiatedBy) { this.initiatedBy = initiatedBy; }

    public LocalDateTime getExecutedAt() { return executedAt; }
    public void setExecutedAt(LocalDateTime executedAt) { this.executedAt = executedAt; }
}
