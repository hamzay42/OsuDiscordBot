package org.example;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class Main {
    public static void main(String[] args) {
        String token = System.getenv("DISCORD_TOKEN"); //takes token from environment variable

        if (token == null) {
            IO.println("[ERROR]: No Discord token provided!");
            return;
        }

        IO.println("[INFO]: Testing Osu! Api Connection... ");
        OsuApiService osuApiService = new OsuApiService();
        String accessToken = osuApiService.getAccessToken();
        IO.println("Osu! Api Connection: " + accessToken);
        String spielerStats = osuApiService.getUserStats("cluwuwu");
        IO.println("cluwuwu Stats: " + spielerStats);
        IO.println("Starte den osu! Bot");
        String botcall = osuApiService.FormattedUserStats("cluwuwu");
        IO.println(botcall);
        try {
            JDABuilder.createDefault(token).enableIntents(GatewayIntent.MESSAGE_CONTENT)
                    .addEventListeners(new BotListener()) //adds listener


                    .build(); //connects to discord
        } catch (Exception error) {
            IO.println("[ERROR]: Something went wrong: " + error.getMessage());
        }

        IO.println("Bot wurde erfolgreich gestarted!");


    }

}