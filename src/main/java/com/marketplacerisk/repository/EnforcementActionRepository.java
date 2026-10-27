package com.marketplacerisk.repository;

import com.marketplacerisk.model.EnforcementAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnforcementActionRepository extends JpaRepository<EnforcementAction, Long> {
    List<EnforcementAction> findBySellerIdOrderByExecutedAtDesc(String sellerId);
    List<EnforcementAction> findByStatus(String status);
    List<EnforcementAction> findByCaseId(Long caseId);
}
