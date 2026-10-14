package org.example;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CommandManager {

    private final ExecutorService executorService;

    public CommandManager(ExecutorService executorService) {
        this.executorService = executorService;
    }

    private final Map<String, Command> commands = new HashMap<>();
public void registerCommand(Command command) {
    commands.put(command.getname().toLowerCase(), command);
}


public void handle(MessageReceivedEvent event) {

    String raw = event.getMessage().getContentRaw();

    if (!raw.startsWith("!")) {
        return;
    }


    String[] parts = raw.substring(1).split(" ");
    String commandName = parts[0].toLowerCase();

    String[] args = new String[parts.length - 1];
    System.arraycopy(parts, 1, args, 0, args.length);

    Command command = commands.get(commandName);

if (command != null) {
    executorService.submit(() -> {
        try {
            command.execute(event,args);
        } catch (Exception e) {
            System.err.println("Fehler bei der Befehlsausfuehrung: " + e.getMessage());
            e.printStackTrace();
        }
    });

}

}








}
