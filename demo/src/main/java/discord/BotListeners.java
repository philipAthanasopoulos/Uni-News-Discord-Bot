package discord;

import app.Unicodes;
import monitor.NewsMonitor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;
import scraper.AuthNewsScraper;
import scraper.UniversityNewsScraper;
import scraper.UoiCSENewsScraper;

import java.util.Objects;

/**
 * The BotListeners class is responsible for listening to the discord server and responding to events like
 * messages, button clicks etc.
 *
 * @author Philip Athanasopoulos
 */
public class BotListeners extends ListenerAdapter {
    private final JDA jda;
    private final NewsMonitor newsMonitor;
    private DiscordSlideShow slideShow;
    private final DiscordNewsPresenter newsPresenter;

    public BotListeners(JDA jda) {
        UniversityNewsScraper scraper = new UoiCSENewsScraper();
        this.jda = jda;
        this.newsPresenter = new DiscordNewsPresenter(scraper);
        this.newsMonitor = new NewsMonitor(scraper, this);
        startNewsMonitor();
    }

    public void onGuildJoin(GuildJoinEvent event) {
        String serverName = event.getGuild().getName();
        System.out.println("Joined server: " + Unicodes.green + serverName + Unicodes.reset);

        TextChannel channel = (TextChannel) event.getGuild().getDefaultChannel();
        channel.sendMessage("Hello! I am the UoI bot. Type **!help** to see my commands.").complete();
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        String serverName = event.getGuild().getName();
        TextChannel channel = event.getChannel().asTextChannel();
        String message = event.getMessage().getContentRaw();

        switch (message) {
            case "!ping" -> playPingPong(channel);

            case "!news" -> sendAllNewsMessage(channel);

            case "!latest news" -> sendLatestNewsMessage(channel);

            case "!slide" -> sendNewsInSlideShow(channel);

            case "!clear" -> deleteAllBotMessages(channel);

            case "!help" -> displayHelpMenu(channel);
        }
    }

    private void displayHelpMenu(TextChannel channel) {
        channel.sendMessage("Commands:\n" + "**!ping** - Pong!\n" + "**!news** - Get all news\n" + "**!latest news** - Get latest news article\n" + "**!slide** - Get all news in a slide show\n" + "**!clear** - Clear all bot messages\n" + "**!help** - Get help").queue();
    }

    private void deleteAllBotMessages(TextChannel channel) {
        for (Message oldMessage : channel.getIterableHistory())
            if (oldMessage.getAuthor().equals(jda.getSelfUser())) oldMessage.delete().queue();
    }

    private void playPingPong(TextChannel channel) {
        channel.sendMessage("Pong! 🏓").queue();
    }

    private void sendNewsInSlideShow(TextChannel channel) {
        this.slideShow = newsPresenter.getNewsAsDiscordSlideShow();
        Button deleteButton = Button.secondary("delete-article", Unicodes.redXEmoji);
        Button nextButton = Button.primary("next-article", "➡️");
        Button previousButton = Button.primary("previous-article", "⬅️");
        channel.sendMessage(slideShow.getCurrentSlide()).setActionRow(deleteButton, previousButton, nextButton).complete();
    }

    public void sendNewsInSlideShowToAllServers() {
        for (Guild server : jda.getGuilds()) sendNewsInSlideShow((TextChannel) server.getDefaultChannel());
        System.out.println(Unicodes.green + "Sent update to all servers" + Unicodes.reset);
    }

    private void sendAllNewsMessage(TextChannel channel) {
        for (String message : newsPresenter.getNewsAsDiscordMessages())
            channel.sendMessage(message).queue();
    }

    private void sendLatestNewsMessage(TextChannel channel) {
        channel.sendMessage(newsPresenter.getLatestNewsAsDiscordMessage()).queue();
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        if (event.getUser().isBot()) return;

        String button_id = event.getButton().getId();

        switch (button_id) {
            case "next-article" -> {
                slideShow.moveToNextSlide();
                event.getMessage().editMessage(slideShow.getCurrentSlide()).queue();
            }
            case "previous-article" -> {
                slideShow.moveToPreviousSlide();
                event.getMessage().editMessage(slideShow.getCurrentSlide()).queue();
            }
            case "delete-article" -> event.getMessage().delete().queue();
        }
        event.deferEdit().queue();
    }

    public void startNewsMonitor() {
        newsMonitor.start();
    }
}
