package Discord;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import scraper.UoiScraper;

import java.util.function.ToDoubleBiFunction;

public class BotListeners extends ListenerAdapter {
    /**
     * TODO: Optimize for multiple channels (multithreading)
     * TODO: Add help command
     * TODO: Add command for getting news
     */
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if(event.getAuthor().isBot()) return;
        if(event.getMessage().getContentRaw().equals("!news")){
            TextChannel channel = event.getChannel().asTextChannel();
            UoiScraper scraper = new UoiScraper();
            Message preperationMessage = channel.sendMessage("Preparing news ... ").complete();
            String news = scraper.scrapeNewsForDiscord();
            channel.deleteMessageById(preperationMessage.getId()).queue();
            channel.sendMessage(news).queue();
        }
    }
}
