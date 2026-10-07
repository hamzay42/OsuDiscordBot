package org.example;

import com.google.gson.Gson;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class OsuApiService {

    private final OkHttpClient httpClient;
    private final Gson gson;
    private final String clientId;
    private final String clientSecret;

    // Cache für das OAuth2-Token
    private String accessToken;

    public OsuApiService() {
        this.httpClient = new OkHttpClient();
        this.gson = new Gson();
        this.clientId = System.getenv("OSU_CLIENT_ID");
        this.clientSecret = System.getenv("OSU_CLIENT_SECRET");
    }

    /**
     * Holt ein neues OAuth2 Access Token von der osu! API oder nutzt das gecachte Token.
     */
    private String getAccessToken() {
        if (this.accessToken != null) {
            return this.accessToken;
        }

        RequestBody formBody = new FormBody.Builder()
                .add("client_id", this.clientId)
                .add("client_secret", this.clientSecret)
                .add("grant_type", "client_credentials")
                .add("scope", "public")
                .build();

        Request request = new Request.Builder()
                .url("https://osu.ppy.sh/oauth/token")
                .post(formBody)
                .build();

        try (Response response = this.httpClient.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String jsonResponse = response.body().string();
                Map<?, ?> map = this.gson.fromJson(jsonResponse, Map.class);
                this.accessToken = (String) map.get("access_token");
                return this.accessToken;
            }
        } catch (IOException e) {
            System.err.println("[ERROR]: Error fetching osu! API token: " + e.getMessage());
        }

        return null;
    }

    /**
     * Synchroner API-Call: Holt die User-Daten aus der osu! v2 API.
     */
    public OsuUser getUser(String username) {
        String token = getAccessToken();
        if (token == null) {
            return null;
        }

        Request request = new Request.Builder()
                .url("https://osu.ppy.sh/api/v2/users/" + username + "/osu")
                .addHeader("Authorization", "Bearer " + token)
                .addHeader("Accept", "application/json")
                .build();

        try (Response response = this.httpClient.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String json = response.body().string();
                return this.gson.fromJson(json, OsuUser.class);
            }
        } catch (IOException e) {
            System.err.println("[ERROR]: Error fetching user stats: " + e.getMessage());
        }

        return null;
    }

    /**
     * Asynchroner API-Call: Führt getUser() in einem Background-Thread aus.
     * Gibt ein CompletableFuture zurück, das den Main/Discord-Thread nicht blockiert.
     */
    public CompletableFuture<OsuUser> getUserAsync(String username) {
        return CompletableFuture.supplyAsync(() -> getUser(username));
    }
}