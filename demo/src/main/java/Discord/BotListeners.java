package Discord;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import scraper.UoiScraper;

public class BotListeners extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        TextChannel general = event.getGuild().getTextChannelsByName("general", true).get(0);
        UoiScraper scraper = new UoiScraper();
        String news = scraper.scrapeNewsToString();
        general.sendMessage(news).queue();

    }
}
