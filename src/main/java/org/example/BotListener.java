package org.example;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Random;


public class BotListener extends ListenerAdapter {

    private final CommandManager commandManager;

    public BotListener(CommandManager commandManager) {
        this.commandManager = commandManager;
    }


    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        User author = event.getAuthor(); //author repleaces event.getAuthor() for cleaner code

        if (author.isBot()) { //bot check
            return;
        }
        this.commandManager.handle(event);
    }
}