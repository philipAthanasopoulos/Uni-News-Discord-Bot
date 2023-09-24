package Scraper;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import app.Unicodes;
import java.util.ArrayList;


/**
 * This class is responsible for scraping the news from the CSE department of the University of Ioannina.
 * Methods provided use MarkDown characters like "*" , ">" , "#" to format the messages for Discord.
 * @author Philip Athanasopoulos
 */


public class UoiScraper extends Scraper{
    private final String CSElink = "https://www.cse.uoi.gr/";
    private final String newsLink = CSElink + "nea/";

    public void scrapeNews(){
        ArrayList<Document> newsLinks = scrapeNewsLinks();
        System.out.println(presentNews(newsLinks));
    }

    public ArrayList<Document> scrapeNewsLinks(){
        ArrayList<String> newsLinks = getNewsLinks();
        ArrayList<Document> documents = new ArrayList<>();
        newsLinks.forEach(link -> documents.add(scrapeSite(link)));
        removeUnwantedElements(documents);
        return documents;
    }

    public Document scrapeLatestNewsLink(){
        ArrayList<String> newsLinks = getNewsLinks();
        return scrapeSite(newsLinks.get(0));
    }

    public ArrayList<String> getNewsLinks(){
        Document doc = scrapeSite(newsLink);
        ArrayList<String> newsLinks = new ArrayList<>();
        Elements links = doc.select(".cs-campus-info").select("h6").select("a[href]");
        for(Element link : links) newsLinks.add(link.attr("abs:href"));
        return newsLinks;
    }



    public void removeUnwantedElements(@NotNull ArrayList<Document> documents){
        for(Document doc : documents){
            Elements elementsToRemove = doc.select("a:contains(WordPress)," +
                    "a:contains(online)," +
                    "a:contains(free)," +
                    "a:contains(course)," +
                    "a:contains(udemy)")
                    .remove();
            elementsToRemove.forEach(Element::remove);
        }
    }

    public String presentNews(@NotNull ArrayList<Document> newsDocuments){
        StringBuilder sb = new StringBuilder();
        newsDocuments.forEach(doc -> sb.append(presentDocument(doc)));
        return sb.toString();
    }

    public String presentDocument(@NotNull Document document){
        StringBuilder sb = new StringBuilder();
        Element title = document.select(".cs-heading-sec").first();
        Elements contents = document.select(".cs-post-panel");
        assert title != null;
        sb.append(Unicodes.green)
                .append(title.text())
                .append(Unicodes.reset)
                .append("\n");
        for(Element element : contents){
            if (element.tagName().equals("a")){
                sb.append(element.attr("abs:href"));
            } else {
                sb.append(element.text());
            }
        }
        sb.append("\n");
        return sb.toString();
    }

    public String presentDocumentForDiscord(@NotNull Document document){
        StringBuilder sb = new StringBuilder();
        Element title = document.select(".cs-heading-sec").first();
        Elements contents = document.select(".cs-editor-text");
        assert title != null;
        sb.append("## ")
                .append(title.text())
                .append("  \n");
        sb.append("> ")
                .append(contents.text())
                .append(" ")
                .append(contents.select("a").attr("abs:href"))
                .append("\n");

        // If the message is too long for Discord, delete enough extra characters + some space for the redirect message
        int MAX_DISCORD_MESSAGE_LENGTH = 2000;
        if(sb.length() > MAX_DISCORD_MESSAGE_LENGTH){
            String link = document.baseUri();
            String redirectMessage = " ***....[Read more](" + link + ")***";
            sb.delete(MAX_DISCORD_MESSAGE_LENGTH - redirectMessage.length(), sb.length());
            sb.append(redirectMessage);
        }
        return sb.toString();
    }

    public void presentNewsForDiscord(@NotNull TextChannel channel){
        long start = System.currentTimeMillis();
        Message preperationMessage = channel.sendMessage("Preparing news ... ").complete();
        channel.sendTyping().queue();
        ArrayList<String> messages = new ArrayList<>();
        ArrayList<Document> newsDocuments = scrapeNewsLinks();
        newsDocuments.forEach(doc -> messages.add(presentDocumentForDiscord(doc)));
        channel.deleteMessageById(preperationMessage.getId()).queue();
        try{
            messages.forEach(message -> channel.sendMessage(message).queue());

        }catch (Exception e){
            System.out.println("Something went wrong while presenting news for discord : \n" + e.getMessage() + "\n");
        }
        long end = System.currentTimeMillis();
        System.out.println("Time elapsed: " + (end - start) + "ms");
    }

    public ArrayList<String> printNewsInDiscordSlideShow(TextChannel channel){
        Message preperationMessage = channel.sendMessage("Preparing news ... ").complete();
        channel.sendTyping().queue();
        ArrayList<String> messages = new ArrayList<>();
        ArrayList<Document> newsDocuments = scrapeNewsLinks();
        newsDocuments.forEach(doc -> messages.add(presentDocumentForDiscord(doc)));
        channel.deleteMessageById(preperationMessage.getId()).queue();
        return messages;
    }

    public void presentArticleForDiscordSlideShow(String messageText,TextChannel channel){
        Button deleteButton = Button.secondary("delete-article",Unicodes.redXEmoji);
        Button nextButton = Button.primary("next-article","➡️");
        Button previousButton = Button.primary("previous-article","⬅️");
        channel.sendMessage(messageText)
                .setActionRow(deleteButton,previousButton,nextButton)
                .complete();
    }

    public void presentLatestNewsForDiscord(@NotNull TextChannel channel){
        long start = System.currentTimeMillis();
        channel.sendMessage("Preparing news ... ").complete();
        channel.sendTyping().queue();
        Document latestNewsDocument = scrapeLatestNewsLink();
        String message = presentDocumentForDiscord(latestNewsDocument);
        channel.sendMessage(message).queue();
        long end = System.currentTimeMillis();
        System.out.println("Time elapsed: " + (end - start) + "ms");
    }

    public static void main(String[] args) {
        UoiScraper scraper = new UoiScraper();
        scraper.scrapeNews();
    }



}
