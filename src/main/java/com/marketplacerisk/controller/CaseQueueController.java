package com.marketplacerisk.controller;

import com.marketplacerisk.model.RiskCase;
import com.marketplacerisk.service.CaseQueueService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/cases")
public class CaseQueueController {

    private final CaseQueueService caseQueueService;

    public CaseQueueController(CaseQueueService caseQueueService) {
        this.caseQueueService = caseQueueService;
    }

    @GetMapping
    public ResponseEntity<List<RiskCase>> getCases(@RequestParam(value = "pendingOnly", defaultValue = "false") boolean pendingOnly) {
        if (pendingOnly) {
            return ResponseEntity.ok(caseQueueService.getPendingCases());
        }
        return ResponseEntity.ok(caseQueueService.getAllCases());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RiskCase> getCaseById(@PathVariable Long id) {
        return caseQueueService.getCaseById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/resolve")
    public ResponseEntity<RiskCase> resolveCase(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        String decision = payload.getOrDefault("decision", "RESOLVED");
        String reason = payload.getOrDefault("reason", "Analyst reviewed and confirmed");
        RiskCase updated = caseQueueService.resolveCase(id, decision, reason);
        return ResponseEntity.ok(updated);
    }
}
