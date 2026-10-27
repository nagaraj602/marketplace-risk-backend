package com.marketplacerisk.service;

import com.marketplacerisk.model.SellerSignal;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VelocityRuleEngine {

    public static class VelocityEvaluation {
        private final double score;
        private final List<String> ruleHits;

        public VelocityEvaluation(double score, List<String> ruleHits) {
            this.score = score;
            this.ruleHits = ruleHits;
        }

        public double getScore() { return score; }
        public List<String> getRuleHits() { return ruleHits; }
    }

    public VelocityEvaluation evaluate(SellerSignal signal) {
        List<String> hits = new ArrayList<>();
        double score = 0.0;

        // Rule 1: Listings per hour velocity threshold (> 30 is suspicious, > 60 is abusive)
        if (signal.getListingsLastHour() > 50) {
            hits.add("RULE_EXCESSIVE_LISTINGS_PER_HOUR");
            score += 40.0;
        } else if (signal.getListingsLastHour() > 20) {
            hits.add("RULE_ELEVATED_LISTINGS_PER_HOUR");
            score += 20.0;
        }

        // Rule 2: Multiple accounts sharing same device footprint or IP address
        if (signal.getUniqueDevicesCount() >= 4) {
            hits.add("RULE_MULTI_ACCOUNT_DEVICE_COLLISION");
            score += 30.0;
        } else if (signal.getDistinctIpCount() >= 5) {
            hits.add("RULE_PROXY_TOR_IP_ROTATION");
            score += 25.0;
        }

        // Rule 3: High value items with brand new account
        if (signal.getAccountAgeDays() <= 5 && signal.getAvgListingPriceUsd() > 1000.0) {
            hits.add("RULE_NEW_ACCOUNT_HIGH_VALUE_EXPOSURE");
            score += 30.0;
        }

        // Rule 4: Critical Account Takeover (ATO) indicator: Payout method changed
        if (signal.isPayoutAccountChangedRecently()) {
            hits.add("RULE_RAPID_PAYOUT_MODIFICATION");
            score += 25.0;
        }

        // Rule 5: Geolocation mismatch between IP and registered seller country
        if (signal.isGeoMismatchCountry()) {
            hits.add("RULE_GEO_IP_LOCATION_MISMATCH");
            score += 20.0;
        }

        // Rule 6: Review rating manipulation
        if (signal.getOrdersLastHour() > 10 && signal.getPositiveReviewsRatio() < 0.30) {
            hits.add("RULE_ABNORMAL_REVIEW_DISCONTENT");
            score += 20.0;
        }

        return new VelocityEvaluation(Math.min(100.0, score), hits);
    }
}
