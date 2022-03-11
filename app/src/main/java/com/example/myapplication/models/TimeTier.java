package com.example.myapplication.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TimeTier {
    @SerializedName("tier")
    @Expose
    private String tier;
    @SerializedName("display_tier")
    @Expose
    private String displayTier;

    public TimeTier(String tier, String displayTier) {
        this.tier = tier;
        this.displayTier = displayTier;
    }

    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }

    public String getDisplayTier() {
        return displayTier;
    }

    public void setDisplayTier(String displayTier) {
        this.displayTier = displayTier;
    }
}
