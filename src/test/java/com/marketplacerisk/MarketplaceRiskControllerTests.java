package com.marketplacerisk;

import com.marketplacerisk.controller.CaseQueueController;
import com.marketplacerisk.controller.EnforcementController;
import com.marketplacerisk.controller.RiskEvaluationController;
import com.marketplacerisk.controller.TelemetryController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class MarketplaceRiskControllerTests {

    @Autowired
    private RiskEvaluationController riskEvaluationController;

    @Autowired
    private CaseQueueController caseQueueController;

    @Autowired
    private EnforcementController enforcementController;

    @Autowired
    private TelemetryController telemetryController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(
                riskEvaluationController,
                caseQueueController,
                enforcementController,
                telemetryController
        ).build();
    }

    @Test
    void testTelemetryEndpoint() throws Exception {
        mockMvc.perform(get("/api/v1/risk/telemetry"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.detectionPipelineHealth").value("HEALTHY"));
    }

    @Test
    void testGetCasesEndpoint() throws Exception {
        mockMvc.perform(get("/api/v1/cases"))
                .andExpect(status().isOk());
    }

    @Test
    void testEvaluateSignalEndpoint() throws Exception {
        String payload = """
            {
                "sellerId": "seller_api_test",
                "sellerName": "API Test Store",
                "accountAgeDays": 60,
                "listingsLastHour": 10,
                "ordersLastHour": 5,
                "uniqueDevicesCount": 1,
                "distinctIpCount": 1
            }
        """;

        mockMvc.perform(post("/api/v1/risk/evaluate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sellerId").value("seller_api_test"))
                .andExpect(jsonPath("$.compositeRiskScore").isNumber());
    }

    @Test
    void testEnforcementExecuteEndpoint() throws Exception {
        String payload = """
            {
                "sellerId": "seller_api_test",
                "actionType": "TAKEDOWN_LISTINGS",
                "reason": "Test takedown"
            }
        """;

        mockMvc.perform(post("/api/v1/enforcement/execute")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.actionType").value("TAKEDOWN_LISTINGS"));
    }
}
