package ir.doodmood.inkout.models;

import com.google.gson.annotations.SerializedName;

public enum ShowPolicy {
    @SerializedName("me")
    ONLY_ME,
    @SerializedName("connections")
    MY_CONNECTIONS,
    @SerializedName("network")
    MY_CONNECTION_NETWORK,
    @SerializedName("public")
    ALL_USERS
}
