package com.marketplacerisk.controller;

import com.marketplacerisk.model.RiskEvaluationResult;
import com.marketplacerisk.model.SellerSignal;
import com.marketplacerisk.service.RiskAggregationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/risk")
public class RiskEvaluationController {

    private final RiskAggregationService aggregationService;

    public RiskEvaluationController(RiskAggregationService aggregationService) {
        this.aggregationService = aggregationService;
    }

    @PostMapping("/evaluate")
    public ResponseEntity<RiskEvaluationResult> evaluateSeller(@Valid @RequestBody SellerSignal signal) {
        RiskEvaluationResult result = aggregationService.evaluateSellerRisk(signal);
        return ResponseEntity.ok(result);
    }
}
