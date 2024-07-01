package ir.doodmood.inkout.models;

import com.google.gson.annotations.SerializedName;

public enum UserGoal {
    @SerializedName("open_to_work")
    OPEN_TO_WORK,
    @SerializedName("hiring")
    HIRING,
    @SerializedName("serving")
    SERVING
}
