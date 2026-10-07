package org.example;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;

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

            OsuUser osuUser = osuApiService.getUser(username);
            MessageEmbed embed = new EmbedBuilder()
                    .setTitle("Player Stats: " + osuUser.username())
                    .setColor(new Color(255, 102, 170))
                    .setThumbnail(osuUser.avatarUrl())
                    .addField("Global Rank", String.valueOf(osuUser.statistics().globalrank()), true)
                    .addField("PP", String.valueOf(osuUser.statistics().pp()), true)
                    .build();
            event.getChannel().sendMessageEmbeds(embed).queue();
        }else {
            event.getChannel().sendMessage("Bitte gib einen Spielernamen an: '!stats <username>'").queue();
        }
    }

}
