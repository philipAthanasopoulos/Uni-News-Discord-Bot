package discord;

import domain.Article;
import net.dv8tion.jda.api.entities.Message;
import scraper.UniversityNewsScraper;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * The News presenter is responsible for formatting the news articles for discord.
 * It uses MarkDown characters like "*" , ">" , "#" to format the messages for discord.
 * The News Presenter is the bridge between the scraper and the discord Bot.
 *
 * @author Philip Athanasopoulos
 */
public class DiscordNewsPresenter {
    private final UniversityNewsScraper scraper;

    public DiscordNewsPresenter(UniversityNewsScraper scraper) {
        this.scraper = scraper;
    }

    public String getArticleAsDiscordMessage(Article article) {
        StringBuilder stringBuilder = new StringBuilder()
                .append("## ")
                .append(article.title())
                .append("  \n")
                .append("> ")
                .append(article.contents())
                .append(" ")
                .append("\n");

        if (stringBuilder.length() > Message.MAX_CONTENT_LENGTH) trimArticle(stringBuilder, article.link());
        return stringBuilder.toString();
    }

    public DiscordSlideShow getNewsAsDiscordSlideShow() {
        DiscordSlideShow slideShow = new DiscordSlideShow();
        for (Article article : scraper.getArticles())
            slideShow.addSlide(getArticleAsDiscordMessage(article));
        return slideShow;
    }

    public ArrayList<String> getNewsAsDiscordMessages() {
        return scraper.getArticles().stream()
                .map(this::getArticleAsDiscordMessage)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public String getLatestNewsAsDiscordMessage() {
        return getArticleAsDiscordMessage(scraper.getLatestArticle());
    }

    private void trimArticle(StringBuilder stringBuilder, String link) {
        String redirectMessage = " ***....[Read more](" + link + ")***";
        stringBuilder.delete(Message.MAX_CONTENT_LENGTH - redirectMessage.length(), stringBuilder.length());
        stringBuilder.append(redirectMessage);
    }
}
