package com.marketplacerisk.repository;

import com.marketplacerisk.model.RiskCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RiskCaseRepository extends JpaRepository<RiskCase, Long> {
    List<RiskCase> findByStatusOrderByCreatedAtDesc(String status);
    List<RiskCase> findBySellerIdOrderByCreatedAtDesc(String sellerId);
    List<RiskCase> findByRiskTierOrderByCreatedAtDesc(String riskTier);

    @Query("SELECT COUNT(c) FROM RiskCase c WHERE c.status = 'PENDING'")
    long countPendingCases();

    @Query("SELECT COUNT(c) FROM RiskCase c WHERE c.status = 'AUTO_ENFORCED'")
    long countAutoEnforcedCases();

    @Query("SELECT COUNT(c) FROM RiskCase c WHERE c.status = 'DISMISSED'")
    long countDismissedCases();
}
