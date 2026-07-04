package org.example;

import com.google.gson.annotations.SerializedName;

public record OsuUser( String username,
                       @SerializedName("avatar_url") String avatarUrl,
                       Statistics statistics) {

public record Statistics(
        double pp,
        @SerializedName("hit_accuracy") double accuracy,
        @SerializedName("global_rank") int globalrank

) {}

}
