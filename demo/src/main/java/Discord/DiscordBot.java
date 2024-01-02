package Discord;

import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class DiscordBot {
    public static void main(String[] args) throws InterruptedException {

        JDA jda = JDABuilder.createDefault(DiscordToken.token, GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS))
                .setActivity(Activity.playing("with the UoI website"))
                .build()
                .awaitReady();

        BotListeners listeners = new BotListeners(jda);
        jda.addEventListener(listeners);
    }
}
