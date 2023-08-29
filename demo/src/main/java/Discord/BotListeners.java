package Discord;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import scraper.UoiScraper;


public class BotListeners extends ListenerAdapter {
    UoiScraper scraper = new UoiScraper();
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
                scraper.presentNewsForDiscord(channel);
                break;
            case "!latest news":
                scraper.presentLatestNewsForDiscord(channel);
                break;
            case "!help":
                //explain commands
                channel.sendMessage("Commands:\n" +
                        "**!ping** - Pong!\n" +
                        "**!news** - Get all news\n" +
                        "**!latest news** - Get latest news article\n" +
                        "**!help** - Get help")
                        .queue();
                break;
            default:
                break;
        }
    }
}
