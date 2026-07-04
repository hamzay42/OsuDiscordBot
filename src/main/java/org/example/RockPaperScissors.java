package org.example;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Random;

public class RockPaperScissors implements Command {

    @Override
    public String getname() {
        return "rps";
    }

    @Override
    public void execute(MessageReceivedEvent event, String[] args) {
        String autherID = event.getAuthor().getId();
        String choice = rps();
        event.getChannel().sendMessage(choice+"! <@" +autherID+">").queue();
    }

    public String rps() {
        int number = new Random().nextInt(3);

        if (number == 0) {
            return "Rock";
        } else if (number == 1) {
            return "Paper";
        } else {
            return "Scissors";
        }

    }
}