package org.example;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

public class Main {
    public static void main(String[] args) {
        String token = System.getenv("DISCORD_TOKEN"); //takes token from environment variable

        if (token == null) {
            IO.println("[ERROR]: No Discord token provided!");
            return;
        }

        IO.println("[INFO]: Testing Osu! Api Connection... ");


        // Cacheing von Accounts um API-Calls zu reduzieren.
        Cache<String, OsuUser> userCache = Caffeine
                .newBuilder()
                .maximumSize(100)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build();
        OsuApiService osuApiService = new OsuApiService(userCache);

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CommandManager commandManager = new CommandManager(executorService);
        commandManager.registerCommand(new StatsCommand(osuApiService));
        commandManager.registerCommand(new PingCommand());
        commandManager.registerCommand(new RockPaperScissors());

        IO.println("Starte den osu! Bot");

        try {
            JDABuilder.createDefault(token).enableIntents(GatewayIntent.MESSAGE_CONTENT)
                    .addEventListeners(new BotListener(commandManager)) //adds listener
                    .build(); //connects to discord
        } catch (Exception error) {
            IO.println("[ERROR]: Something went wrong: " + error.getMessage());
        }

        IO.println("Bot wurde erfolgreich gestarted!");


        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Beende Executioner Service...");
            executorService.shutdown();
            try {
                if(!executorService.awaitTermination(5, TimeUnit.SECONDS)){
                    executorService.shutdownNow();
                }
            }catch (InterruptedException error){
                executorService.shutdownNow();
            }
            System.out.println("ExecutionService erfolgreich gestoppt.");
        }));


    }

}