package Scraper;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
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

    final int MAX_DISCORD_MESSAGE_LENGTH = 2000;

    public void scrapeNews(){
        ArrayList<Document> newsLinks = scrapeNewsLinks();
        System.out.println(presentNews(newsLinks));
    }

    public ArrayList<Document> scrapeNewsLinks(){
        String CSElink = "https://www.cse.uoi.gr/";
        String newsLink = CSElink + "nea/";
        Document doc = scrapeSite(newsLink);
        ArrayList<String> newsLinks = new ArrayList<>();
        Elements links = doc.select(".cs-campus-info").select("h6").select("a[href]");
        for(Element link : links) newsLinks.add(link.attr("abs:href"));
        ArrayList<Document> documents = new ArrayList<>();
        for(String link : newsLinks) documents.add(scrapeSite(link));
        removeUnwantedElements(documents);
        return documents;
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
        if(sb.length() > MAX_DISCORD_MESSAGE_LENGTH){
            String link = document.baseUri();
            String redirectMessage = " ***....[Read more](" + link + ")***";
            sb.delete(MAX_DISCORD_MESSAGE_LENGTH - redirectMessage.length(), sb.length());
            sb.append(redirectMessage);
        }
        return sb.toString();
    }

    public void presentNewsForDiscord(@NotNull TextChannel channel){
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

    }

    public void presentLatestNewsForDiscord(@NotNull TextChannel channel){
        Message preperationMessage = channel.sendMessage("Preparing news ... ").complete();
        channel.sendTyping().queue();
        ArrayList<String> messages = new ArrayList<>();
        ArrayList<Document> newsDocuments = new ArrayList<>();
        newsDocuments.add(scrapeNewsLinks().get(0));
        messages.add(presentDocumentForDiscord(newsDocuments.get(0)));
        channel.deleteMessageById(preperationMessage.getId()).queue();
        channel.sendMessage(messages.get(0)).queue();
    }

    public static void main(String[] args) {
        UoiScraper scraper = new UoiScraper();
        scraper.scrapeNews();
    }



}
