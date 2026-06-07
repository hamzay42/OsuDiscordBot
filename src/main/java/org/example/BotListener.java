package org.example;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class BotListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }
        if (event.getMessage().getContentRaw().startsWith("!ping")) {
            //event.getChannel().sendMessage("Pong! " + event.getAuthor().getEffectiveName()).queue(); //answers with pong if ping! is sent
            event.getChannel().sendMessage("Pong! " + "<@"+event.getAuthor().getId()+">").queue(); //answers with pong if ping! is sent
        }
    }
}