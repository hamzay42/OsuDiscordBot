package org.example;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class Main {
    public static void main(String[] args) {
        String token = System.getenv("DISCORD_TOKEN");

        if (token == null){
            IO.println("[ERROR]: No Discord token provided!");
            return;
        }

        IO.println("Starte den osu! Bot");

        try{
            JDABuilder.createDefault(token).enableIntents(GatewayIntent.MESSAGE_CONTENT).build();
        }
        catch(Exception error){
            IO.println("[ERROR]: Something went wrong: " + error.getMessage());
        }

        IO.println("Bot wurde erfolgreich gestarted!");





    }
}