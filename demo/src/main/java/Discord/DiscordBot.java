package Discord;

import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.quartz.SchedulerException;

import javax.security.auth.login.LoginException;
import java.util.Optional;
import WebsiteMonitor.WebsiteMonitor;

public class DiscordBot {
    public static void main(String[] args) throws LoginException, SchedulerException, InterruptedException {

        JDA jda = JDABuilder.createDefault(DiscordToken.token, GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS))
                .setActivity(Activity.playing("with Scraper"))
                .build()
                .awaitReady();

        BotListeners listeners = new BotListeners(jda);
        jda.addEventListener(listeners);
    }
}
