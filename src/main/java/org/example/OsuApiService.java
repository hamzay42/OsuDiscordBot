package org.example;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import okhttp3.*;

import java.awt.*;
import java.io.IOException;

public class OsuApiService {
    private String accessToken;

    public String getAccessToken() {
        String url = "https://osu.ppy.sh/oauth/token";
        String osu_client_id = System.getenv("OSU_CLIENT_ID");
        String osu_client_secret = System.getenv("OSU_CLIENT_SECRET");


        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("client_id", osu_client_id)
                .add("client_secret", osu_client_secret)
                .add("grant_type", "client_credentials")
                .add("scope", "public")
                .build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String jsonAntwort = response.body().string();
                JsonObject jsonObject = JsonParser.parseString(jsonAntwort).getAsJsonObject();
                this.accessToken = jsonObject.get("access_token").getAsString();
                return this.accessToken;
            } else {
                return "Server Error: " + response.code();
            }
        } catch (Exception e) {
            return "Network Error: " + e.getMessage();
        }


    }

    public String getUserStats(String username) {
        if (this.accessToken == null) {
            getAccessToken();
        }
        String url = "https://osu.ppy.sh/api/v2/users/" + username + "/osu";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).header("Authorization", "Bearer " + this.accessToken).build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return response.body().string();
            } else {
                return "Fehler beim Spieler-Abruf: " + response.code();
            }
        } catch (IOException e) {
            return "Netzwerkfehler: " + e.getMessage();
        }
    }

    public MessageEmbed FormattedUserStats(String username) {
        String data_raw = getUserStats(username);
        JsonObject jsonObject = new JsonParser().parse(data_raw).getAsJsonObject();
        JsonObject stats = jsonObject.get("statistics").getAsJsonObject();

        double pp = stats.get("pp").getAsDouble();
        double acc = stats.get("hit_accuracy").getAsDouble();
        int G_rank = stats.get("global_rank").getAsInt();
        String avatar = jsonObject.get("avatar_url").getAsString();
        int player_count = jsonObject.get("beatmap_playcounts_count").getAsInt();

        EmbedBuilder embed = new EmbedBuilder().setTitle("Player Stats: " + username)
                .setColor(Color.PINK)
                .setThumbnail(avatar)
                .addField("Global Rank",String.valueOf(G_rank),false )
                .addField("PP",String.valueOf(pp),false )
                .addField("accuracy", String.valueOf(acc),false)
                .addField("Beatmap Count",String.valueOf(player_count),false);



        return embed.build();


    }

}
