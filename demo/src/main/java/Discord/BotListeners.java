package Discord;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import net.dv8tion.jda.api.*;
import org.quartz.*;
import Scraper.UoiScraper;

import java.util.ArrayList;

public class BotListeners extends ListenerAdapter {

    private JDA jda = null;
    private UoiScraper scraper;
    private ArrayList<String> currentSlideShow = new ArrayList<>();
    private int currentSlideShowIndex = 0;

    public BotListeners(JDA jda) throws SchedulerException {
        this.jda = jda;
        scraper = new UoiScraper();
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        String serverName = event.getGuild().getName();
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
            case "!slide":
                printSlideShow(channel);
                break;
            case "!clear":
                //delete bots messages
                for(Message oldMessage : channel.getIterableHistory())
                    if(oldMessage.getAuthor().equals(jda.getSelfUser()))
                        oldMessage.delete().queue();
            case "!help":
                channel.sendMessage("Commands:\n" +
                        "**!ping** - Pong!\n" +
                        "**!news** - Get all news\n" +
                        "**!latest news** - Get latest news article\n" +
                        "**!slide** - Get all news in a slide show\n" +
                        "**!clear** - Clear all bot messages\n" +
                        "**!help** - Get help")
                        .queue();
                break;
            default:
                break;
        }
    }

    private void printSlideShow(TextChannel channel) {
        currentSlideShow = scraper.printNewsInDiscordSlideShow(channel);
        scraper.presentArticleForDiscordSlideShow( currentSlideShow.get(currentSlideShowIndex), channel);
        currentSlideShowIndex = 0;
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event){
        if(event.getUser().isBot()) return;

        String button_id = event.getButton().getId();
        switch(button_id){
            case "next-article":
                if(currentSlideShowIndex == currentSlideShow.size() - 1) break;
                event.getMessage().editMessage(currentSlideShow.get(++currentSlideShowIndex)).queue();
                break;
            case "previous-article":
                if(currentSlideShowIndex == 0) break;
                event.getMessage().editMessage(currentSlideShow.get(--currentSlideShowIndex)).queue();
                break;
            case "delete-article":
                event.getMessage().delete().queue();
                break;
            default:
                break;
        }
        event.deferEdit().queue();
    }
}
