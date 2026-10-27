package com.marketplacerisk;

import com.marketplacerisk.model.RiskEvaluationResult;
import com.marketplacerisk.model.SellerSignal;
import com.marketplacerisk.service.RiskAggregationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MarketplaceRiskApplicationTests {

    @Autowired
    private RiskAggregationService aggregationService;

    @Test
    void contextLoads() {
        assertNotNull(aggregationService);
    }

    @Test
    void testEvaluateLowRiskSeller() {
        SellerSignal signal = new SellerSignal();
        signal.setSellerId("seller_trusted_01");
        signal.setSellerName("Apex Electronics");
        signal.setAccountAgeDays(500);
        signal.setListingsLastHour(2);
        signal.setOrdersLastHour(5);
        signal.setUniqueDevicesCount(1);
        signal.setDistinctIpCount(1);

        RiskEvaluationResult result = aggregationService.evaluateSellerRisk(signal);
        assertNotNull(result);
        assertEquals("seller_trusted_01", result.getSellerId());
        assertTrue(result.getCompositeRiskScore() < 50.0);
        assertEquals("DISMISSED", result.getRoutingDecision());
    }

    @Test
    void testEvaluateHighRiskAbusiveSeller() {
        SellerSignal signal = new SellerSignal();
        signal.setSellerId("seller_fraud_99");
        signal.setSellerName("Flash Deal Bot");
        signal.setAccountAgeDays(2);
        signal.setListingsLastHour(80);
        signal.setUniqueDevicesCount(5);
        signal.setDistinctIpCount(6);
        signal.setPayoutAccountChangedRecently(true);
        signal.setGeoMismatchCountry(true);
        signal.setAvgListingPriceUsd(1200.0);

        RiskEvaluationResult result = aggregationService.evaluateSellerRisk(signal);
        assertNotNull(result);
        assertEquals("seller_fraud_99", result.getSellerId());
        assertTrue(result.getCompositeRiskScore() >= 80.0);
        assertEquals("AUTO_ENFORCE", result.getRoutingDecision());
        assertFalse(result.getEnforcementsTriggered().isEmpty());
    }
}
