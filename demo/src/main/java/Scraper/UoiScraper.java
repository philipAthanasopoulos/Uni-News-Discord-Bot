package Scraper;

import WebsiteMonitor.WebsiteMonitor;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
    private final int MAX_DISCORD_MESSAGE_LENGTH = 2000;
    private Document latestNewsDocument = null;
    private ArrayList<Document> newsAriclesDocuments = new ArrayList<>();
    private WebsiteMonitor websiteMonitor;

    public UoiScraper(){
        System.out.println(Unicodes.pink + "Scraper initialized!" + Unicodes.reset);
        websiteMonitor = new WebsiteMonitor(this);
        websiteMonitor.start();
    }

    public void scrapeNews(){
        ArrayList<Document> newsLinks = getNewsDocuments();
        System.out.println(presentNews(newsLinks));
    }

    public ArrayList<Document> getNewsDocuments(){
        return newsAriclesDocuments;
    }

    public void refreshNewsDocuments(){
        newsAriclesDocuments.clear();
        latestNewsDocument = scrapeSite(newsLink);
        getNewsLinks().forEach(link -> newsAriclesDocuments.add(scrapeSite(link)));
        removeUnwantedElements(newsAriclesDocuments);
    }

    public Document scrapeLatestNewsLink(){
        ArrayList<String> newsLinks = getNewsLinks();
        return scrapeSite(newsLinks.get(0));
    }

    public ArrayList<String> getNewsLinks(){
        ArrayList<String> newsLinks = new ArrayList<>();
        Elements links = latestNewsDocument.select(".cs-campus-info").select("h6").select("a[href]");
        links.forEach(link -> newsLinks.add(link.attr("abs:href")) );
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
        StringBuilder stringBuilder = new StringBuilder();
        Element title = getDocumentTitle(document);
        Elements contents = getDocumentContents(document);

        assert title != null;
        stringBuilder.append(Unicodes.green)
                        .append(title.text())
                        .append(Unicodes.reset)
                        .append("\n");

        for(Element element : contents)
            if (element.tagName().equals("a")) stringBuilder.append(element.attr("abs:href"));
            else stringBuilder.append(element.text());

        stringBuilder.append("\n");
        return stringBuilder.toString();
    }

    @NotNull
    private static Elements getDocumentContents(@NotNull Document document) {
        return document.select(".cs-editor-text");
    }

    @Nullable
    private static Element getDocumentTitle(@NotNull Document document) {
        return document.select(".cs-heading-sec").first();
    }

    public String presentDocumentForDiscord(@NotNull Document document){
        StringBuilder stringBuilder = new StringBuilder();
        Element title = getDocumentTitle(document);
        Elements contents = getDocumentContents(document);

        assert title != null;
        stringBuilder.append("## ")
                .append(title.text())
                .append("  \n")
                .append("> ")
                .append(contents.text())
                .append(" ")
                .append(contents.select("a").attr("abs:href"))
                .append("\n");

        if(stringBuilder.length() > MAX_DISCORD_MESSAGE_LENGTH)
            trimDocument(document, stringBuilder);

        return stringBuilder.toString();
    }

    private void trimDocument(Document document, StringBuilder sb) {
        String link = document.baseUri();
        String redirectMessage = " ***....[Read more](" + link + ")***";
        sb.delete(MAX_DISCORD_MESSAGE_LENGTH - redirectMessage.length(), sb.length());
        sb.append(redirectMessage);
    }

    public void presentNewsForDiscord(@NotNull TextChannel channel){
        long start = System.currentTimeMillis();

        Message preperationMessage = channel.sendMessage("Preparing news ... ").complete();
        channel.sendTyping().queue();
        ArrayList<String> messages = new ArrayList<>();
        ArrayList<Document> newsDocuments = getNewsDocuments();
        newsDocuments.forEach(doc -> messages.add(presentDocumentForDiscord(doc)));
        channel.deleteMessageById(preperationMessage.getId()).queue();

        try{
            messages.forEach(message -> channel.sendMessage(message).queue());
        }catch (Exception e){
            System.out.println("Something went wrong while presenting news for discord : \n" + e.getMessage() + "\n");
        }

        long end = System.currentTimeMillis();
        System.out.println("Time of scraping: " + (end - start) + "ms");
    }

    public ArrayList<String> printNewsInDiscordSlideShow(TextChannel channel){
        ArrayList<String> messages = new ArrayList<>();

        Message preperationMessage = channel.sendMessage("Preparing news ... ").complete();
        channel.sendTyping().queue();

        ArrayList<Document> newsDocuments = getNewsDocuments();
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

    public void setLatestNewsDocument(Document latestNewsDocument) {
        this.latestNewsDocument = latestNewsDocument;
    }
}
