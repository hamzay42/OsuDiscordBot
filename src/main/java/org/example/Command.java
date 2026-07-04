package org.example;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface Command {
    

    //gibt den namen des commands
    String getname();

    //eigentliche logik
    void execute(MessageReceivedEvent event,String[] args);




}
