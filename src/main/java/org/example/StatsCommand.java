package org.example;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class StatsCommand implements Command {
    private final OsuApiService osuApiService;

    public StatsCommand(OsuApiService osuApiService) {
        this.osuApiService = osuApiService;
    }

    @Override
    public String getname(){
        return "stats";
}

    @Override
    public void execute(MessageReceivedEvent event, String[] args) {
        if (args.length >= 0){
            String username = args[0];
            MessageEmbed embed = this.osuApiService.FormattedUserStats(username);
            event.getChannel().sendMessageEmbeds(embed).queue();
        }else {
            event.getChannel().sendMessage("Bitte gib einen Spielernamen an: '!stats <username>'").queue();
        }
    }

}
