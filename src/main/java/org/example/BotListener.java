package org.example;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class BotListener extends ListenerAdapter {


    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        User author = event.getAuthor(); //author repleaces event.getAuthor() for cleaner code
        if (author.isBot()) { //bot check
            return;
        }
        if (event.getMessage().getContentRaw().startsWith("!ping")) {
            event.getChannel().sendMessage("Pong! " + "<@" + author.getId() + ">").queue(); //answers with pong if ping! is sent
        } else if (event.getMessage().getContentRaw().startsWith("!hey")) {
            event.getChannel().sendMessage("Hey! " + "<@" + author.getId() + ">").queue();
        } else if (event.getMessage().getContentRaw().startsWith("!bye")) {
            event.getChannel().sendMessage("Bye! " + "<@" + author.getId() + "> " + "https://tenor.com/view/bocchi-bocchi-the-rock-anime-girl-anime-nijika-gif-15470744972741145336").queue(); //answer with a gif
        } else if (event.getMessage().getContentRaw().startsWith("!stats")) {
            String[] parts = event.getMessage().getContentRaw().split(" ");
            if (parts.length == 2) {
                String botcall = new OsuApiService().FormattedUserStats(parts[1]);
                event.getChannel().sendMessage(botcall).queue();
            } else {
                event.getChannel().sendMessage("Request Player stats with: !stats <username>").queue();
            }


        }


    }
}