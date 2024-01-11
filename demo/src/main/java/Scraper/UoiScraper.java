package Scraper;

import WebsiteMonitor.WebsiteMonitor;
import app.Unicodes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * This class is responsible for scraping the news from the CSE department of the University of Ioannina.
 * Methods provided use MarkDown characters like "*" , ">" , "#" to format the messages for Discord.
 *
 * @author Philip Athanasopoulos
 */
public class UoiScraper extends Scraper {
    private final String CSElink = "https://www.cse.uoi.gr/";
    private final String newsEndpoint = CSElink + "nea/";
    private Document latestNewsDocument = null;
    private final ArrayList<Article> articles;
    private final WebsiteMonitor websiteMonitor;

    public UoiScraper() {
        articles = new ArrayList<>();
        System.out.println(Unicodes.pink + "Scraper initialized!" + Unicodes.reset);
        websiteMonitor = new WebsiteMonitor(this);
        websiteMonitor.start();
    }

    public ArrayList<Article> getArticles() {
        return articles;
    }

    public void refreshNewsDocuments() {

        try{
            latestNewsDocument = scrapeSite(newsEndpoint);
            articles.clear();
            for (String link : getNewsLinks()) {
                Document articleDocument = scrapeSite(link);
                removeUnwantedElements(articleDocument);
                articles.add(getArticleFromDocument(articleDocument));
            }
        }catch (Exception e) {
            System.out.println(Unicodes.red + "Something went wrong while refreshing the news!" + Unicodes.reset);
        }
    }

    private Article getArticleFromDocument(Document document) {
        Element title = getDocumentTitle(document);
        Elements contents = getDocumentContents(document);
        String link = document.baseUri();
        return new Article(title.text(), contents.text(), link);
    }

    private ArrayList<String> getNewsLinks() {
        ArrayList<String> newsLinks = new ArrayList<>();
        Elements links = latestNewsDocument.select(".cs-campus-info").select("h6").select("a[href]");
        links.forEach(link -> newsLinks.add(link.attr("abs:href")));
        return newsLinks;
    }

    private void removeUnwantedElements(@NotNull Document document) {
        Elements elementsToRemove = document.select("a:contains(WordPress)," +
                        "a:contains(online)," +
                        "a:contains(free)," +
                        "a:contains(course)," +
                        "a:contains(udemy)")
                .remove();
        elementsToRemove.forEach(Element::remove);
    }

    @NotNull
    private static Elements getDocumentContents(@NotNull Document document) {
        return document.select(".cs-editor-text");
    }

    @Nullable
    private static Element getDocumentTitle(@NotNull Document document) {
        return document.select(".cs-heading-sec").first();
    }

    public Article getLatestArticle() {
        return this.articles.get(0);
    }
}
