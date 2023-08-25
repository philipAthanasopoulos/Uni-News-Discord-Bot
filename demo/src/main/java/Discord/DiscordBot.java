package Discord;

import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.channel.attribute.IGuildChannelContainer;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
import java.util.Objects;

public class DiscordBot {
    public static void main(String[] args) throws LoginException {

        JDA jda = JDABuilder.createDefault("MTE0NDY3OTgwMjk5ODEwNDE1Ng.GlEn4-.YZV2TqAAGR13-bKbPZMw5DblPfGDDPd4CncXd8")
                .setActivity(Activity.playing("with Scraper"))
                .addEventListeners(new BotListeners())
                .build();
    }




}
