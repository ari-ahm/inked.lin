package ir.doodmood.inkout.models;

import com.google.gson.annotations.SerializedName;

public enum PositionType {
    @SerializedName("full-time")
    FULL_TIME,
    @SerializedName("part-time")
    PART_TIME,
    @SerializedName("self-employed")
    SELF_EMPLOYED,
    @SerializedName("freelance")
    FREELANCE,
    @SerializedName("contract")
    CONTRACT,
    @SerializedName("internship")
    INTERNSHIP,
    @SerializedName("paid-internship")
    PAID_INTERNSHIP,
    @SerializedName("seasonal")
    SEASONAL
}