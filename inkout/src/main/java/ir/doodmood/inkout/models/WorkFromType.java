package ir.doodmood.inkout.models;

import com.google.gson.annotations.SerializedName;

public enum WorkFromType {
    @SerializedName("onsite")
    ONSITE,
    @SerializedName("hybrid")
    HYBRID,
    @SerializedName("remote")
    REMOTE
}
