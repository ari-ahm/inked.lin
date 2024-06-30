package ir.doodmood.inkout.models;

import com.google.gson.annotations.SerializedName;

public enum PhoneType {
    @SerializedName("mobile")
    MOBILE,
    @SerializedName("home")
    HOME,
    @SerializedName("work")
    WORK
}
