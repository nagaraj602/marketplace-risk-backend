package com.marketplacerisk.model;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public class SellerSignal {

    @NotBlank(message = "sellerId is required")
    private String sellerId;

    private String sellerName;
    private int accountAgeDays = 30;
    private int listingsLastHour = 0;
    private int ordersLastHour = 0;
    private double positiveReviewsRatio = 0.90;
    private int uniqueDevicesCount = 1;
    private int distinctIpCount = 1;
    private double avgListingPriceUsd = 50.0;
    private boolean payoutAccountChangedRecently = false;
    private boolean geoMismatchCountry = false;

    public SellerSignal() {}

    public String getSellerId() { return sellerId; }
    public void setSellerId(String sellerId) { this.sellerId = sellerId; }

    public String getSellerName() { return sellerName; }
    public void setSellerName(String sellerName) { this.sellerName = sellerName; }

    public int getAccountAgeDays() { return accountAgeDays; }
    public void setAccountAgeDays(int accountAgeDays) { this.accountAgeDays = accountAgeDays; }

    public int getListingsLastHour() { return listingsLastHour; }
    public void setListingsLastHour(int listingsLastHour) { this.listingsLastHour = listingsLastHour; }

    public int getOrdersLastHour() { return ordersLastHour; }
    public void setOrdersLastHour(int ordersLastHour) { this.ordersLastHour = ordersLastHour; }

    public double getPositiveReviewsRatio() { return positiveReviewsRatio; }
    public void setPositiveReviewsRatio(double positiveReviewsRatio) { this.positiveReviewsRatio = positiveReviewsRatio; }

    public int getUniqueDevicesCount() { return uniqueDevicesCount; }
    public void setUniqueDevicesCount(int uniqueDevicesCount) { this.uniqueDevicesCount = uniqueDevicesCount; }

    public int getDistinctIpCount() { return distinctIpCount; }
    public void setDistinctIpCount(int distinctIpCount) { this.distinctIpCount = distinctIpCount; }

    public double getAvgListingPriceUsd() { return avgListingPriceUsd; }
    public void setAvgListingPriceUsd(double avgListingPriceUsd) { this.avgListingPriceUsd = avgListingPriceUsd; }

    public boolean isPayoutAccountChangedRecently() { return payoutAccountChangedRecently; }
    public void setPayoutAccountChangedRecently(boolean payoutAccountChangedRecently) { this.payoutAccountChangedRecently = payoutAccountChangedRecently; }

    public boolean isGeoMismatchCountry() { return geoMismatchCountry; }
    public void setGeoMismatchCountry(boolean geoMismatchCountry) { this.geoMismatchCountry = geoMismatchCountry; }
}
