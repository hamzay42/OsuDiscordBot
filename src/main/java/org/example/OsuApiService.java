package org.example;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import okhttp3.*;
import com.google.gson.*;

import java.awt.*;
import java.io.IOException;

public class OsuApiService {
    private String accessToken;
    private OkHttpClient httpClient;
    private Gson gson;


    public OsuApiService(){
        this.httpClient = new OkHttpClient();
        this.gson = new Gson();
    }


    public String getAccessToken() {

        if(accessToken != null){
            return this.accessToken;
        }

        String url = "https://osu.ppy.sh/oauth/token";
        String osuClientId = System.getenv("OSU_CLIENT_ID");
        String osuClientSecret = System.getenv("OSU_CLIENT_SECRET");



        RequestBody formBody = new FormBody.Builder()
                .add("client_id", osuClientId != null ? osuClientId : "")
                .add("client_secret", osuClientSecret != null ? osuClientSecret : "")
                .add("grant_type", "client_credentials")
                .add("scope", "public")
                .build();

        Request request = new Request.Builder().url(url).post(formBody).build();

        try (Response response = this.httpClient.newCall(request).execute()) {
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


        Request request = new Request.Builder().url(url).header("Authorization", "Bearer " + this.accessToken).build();
        try (Response response = this.httpClient.newCall(request).execute()) {
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
        OsuUser user = getUser(username);

        if (user == null) {
            return new EmbedBuilder().setTitle("ERROR").
                setDescription("Username " + username + " not found").
                setColor(Color.RED)
            .build();
        }
        return  new EmbedBuilder().setTitle("Player Stats: " + username)
                .setColor(new Color(255,102,170))
                .setThumbnail(user.avatarUrl())
                .addField("Global Rank", String.valueOf(user.statistics().globalrank()),true)
                .addField("PP", String.valueOf(user.statistics().pp()),true)
                .build();


    }

    public OsuUser getUser(String username) {
        String data_raw = getUserStats(username);
        if(data_raw.startsWith("{")){
            Gson gson = new Gson();
            return gson.fromJson(data_raw,OsuUser.class);
        }
        return null;
    }
}
