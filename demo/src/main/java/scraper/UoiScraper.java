package scraper;

import app.resources.Unicodes;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class UoiScraper extends Scraper{

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

    public void removeUnwantedElements(ArrayList<Document> documents){
        for(Document doc : documents){
            Elements elementsToRemove = doc.select("a:contains(WordPress)," +
                    "a:contains(online free course)," +
                    "a:contains(udemy)")
                    .remove();
            elementsToRemove.forEach(Element::remove);
        }
    }

    public String presentNews(ArrayList<Document> newsDocuments){
        StringBuilder sb = new StringBuilder();
        newsDocuments.forEach(doc -> sb.append(presentDocument(doc)));
        return sb.toString();
    }

    public String presentDocument(Document document){
        StringBuilder sb = new StringBuilder();
        Element title = document.select(".cs-heading-sec").first();
        Elements contents = document.select(".cs-editor-text");
        assert title != null;
        sb.append(Unicodes.green)
                .append(title.text())
                .append(Unicodes.reset)
                .append("\n");
        sb.append(contents.text())
                .append("\n");
        return sb.toString();
    }

    public String presentDocumentForDiscord(Document document){
        StringBuilder sb = new StringBuilder();
        Element title = document.select(".cs-heading-sec").first();
        Elements contents = document.select(".cs-editor-text");
        assert title != null;
        sb.append("## ")
                .append(title.text())
                .append(Unicodes.reset)
                .append("\n");
        sb.append("> ")
                .append(contents.text())
                .append("\n");
        return sb.toString();
    }

    public void presentNewsForDiscord(TextChannel channel){
        Message preperationMessage = channel.sendMessage("Preparing news ... ").complete();
        ArrayList<String> messages = new ArrayList<>();
        ArrayList<Document> newsDocuments = scrapeNewsLinks();
        newsDocuments.forEach(doc -> messages.add(presentDocumentForDiscord(doc)));
        channel.deleteMessageById(preperationMessage.getId()).queue();
        for(String message : messages) channel.sendMessage(message).queue();
    }


    public static void main(String[] args) {
        UoiScraper scraper = new UoiScraper();
        scraper.scrapeNews();
    }



}
