package com.marketplacerisk.service;

import com.marketplacerisk.model.RiskCase;
import com.marketplacerisk.repository.RiskCaseRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CaseQueueService {

    private final RiskCaseRepository caseRepository;

    public CaseQueueService(RiskCaseRepository caseRepository) {
        this.caseRepository = caseRepository;
    }

    public List<RiskCase> getPendingCases() {
        return caseRepository.findByStatusOrderByCreatedAtDesc("PENDING");
    }

    public List<RiskCase> getAllCases() {
        return caseRepository.findAll();
    }

    public Optional<RiskCase> getCaseById(Long id) {
        return caseRepository.findById(id);
    }

    public RiskCase resolveCase(Long id, String decision, String reason) {
        RiskCase rCase = caseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Case not found with id: " + id));

        rCase.setStatus(decision); // RESOLVED, DISMISSED, ACTION_CONFIRMED
        rCase.setDecisionReason(reason);
        rCase.setUpdatedAt(LocalDateTime.now());
        return caseRepository.save(rCase);
    }
}
