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
     * Todo: Add command for getting announcements
     */
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if(event.getAuthor().isBot()) return;

        TextChannel channel = event.getChannel().asTextChannel();
        String message = event.getMessage().getContentRaw();

        switch(message){
            case "!ping":
                channel.sendMessage("Pong!").queue();
                break;

            case "!news":
                UoiScraper scraper = new UoiScraper();
                Message preperationMessage = channel.sendMessage("Preparing news ... ").complete();
                String news = scraper.scrapeNewsForDiscord();
                channel.deleteMessageById(preperationMessage.getId()).queue();
                channel.sendMessage(news).queue();
                break;

            case "!help":
                channel.sendMessage("***Commands***:\n" +
                        "**!ping** - Pong!\n" +
                        "**!news** - Get the latest news from the CSE department").queue();
                break;
        }
    }
}
