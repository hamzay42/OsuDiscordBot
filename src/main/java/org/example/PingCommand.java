package org.example;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class PingCommand implements Command {

    @Override
    public String getname() {
        return "ping";
    }

    public void execute(MessageReceivedEvent event, String[] args){
        String autherID = event.getAuthor().getId();
        event.getChannel().sendMessage("Pong! <@" +autherID+">").queue();
    }

}
