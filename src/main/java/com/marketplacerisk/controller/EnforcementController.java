package com.marketplacerisk.controller;

import com.marketplacerisk.model.EnforcementAction;
import com.marketplacerisk.service.EnforcementOrchestrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/enforcement")
public class EnforcementController {

    private final EnforcementOrchestrationService enforcementService;

    public EnforcementController(EnforcementOrchestrationService enforcementService) {
        this.enforcementService = enforcementService;
    }

    @PostMapping("/execute")
    public ResponseEntity<EnforcementAction> executeAction(@RequestBody Map<String, Object> payload) {
        Long caseId = payload.get("caseId") != null ? Long.valueOf(payload.get("caseId").toString()) : null;
        String sellerId = (String) payload.get("sellerId");
        String actionType = (String) payload.get("actionType");
        String reason = (String) payload.getOrDefault("reason", "Manual analyst enforcement");
        String initiatedBy = (String) payload.getOrDefault("initiatedBy", "ANALYST");

        EnforcementAction action = enforcementService.executeAction(caseId, sellerId, actionType, reason, initiatedBy);
        return ResponseEntity.ok(action);
    }

    @GetMapping("/actions")
    public ResponseEntity<List<EnforcementAction>> getActions(@RequestParam(value = "sellerId", required = false) String sellerId) {
        if (sellerId != null && !sellerId.isBlank()) {
            return ResponseEntity.ok(enforcementService.getActionsForSeller(sellerId));
        }
        return ResponseEntity.ok(enforcementService.getAllActions());
    }
}
