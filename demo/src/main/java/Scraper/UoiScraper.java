package Scraper;

import app.Unicodes;
import domain.Article;
import monitor.WebsiteMonitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.TreeSet;

/**
 * This class is responsible for scraping the news from the CSE department of the University of Ioannina.
 *
 * @author Philip Athanasopoulos
 */
public class UoiScraper extends Scraper {
    private final String cseNewsLink = "https://www.cse.uoi.gr/nea/";
    private Document latestNewsDocument = null;
    private final ArrayList<Article> articles;
    private final WebsiteMonitor websiteMonitor;
    private volatile boolean needToSendUpdates = false;

    public UoiScraper() {
        articles = new ArrayList<>();
        printScraperInitializationMessage();
        websiteMonitor = new WebsiteMonitor(this);
        websiteMonitor.start();
    }

    public ArrayList<Article> getArticles() {
        return articles;
    }

    public void refreshNewsDocuments() {
        try {
            Document freshNewsDocument = scrapeSite(cseNewsLink);
            if (newsHaveChanged(freshNewsDocument)) {
                latestNewsDocument = freshNewsDocument;
                articles.clear();
                for (String newsArticleLink : getNewsLinks()) {
                    Document articleDocument = scrapeSite(newsArticleLink);
                    removeUnwantedElements(articleDocument);
                    articles.add(getArticleFromDocument(articleDocument));
                }
                setNeedToSendUpdates(true);
            }
        } catch (Exception exception) {
            switch (exception.getClass().getSimpleName()) {
                case "connectException":
                    printErrorMessage("Could not connect to CSE website");
                    break;
                case "socketException":
                    printErrorMessage("Something went wrong while trying to access socket");
                    break;
                case "socketTimeoutException":
                    printErrorMessage("Timed out while trying to receive data");
                    break;
                default:
                    printErrorMessage("Something went wrong while refreshing the news");
            }
            printAttentionMessage("Retrying news refresh...");
            refreshNewsDocuments();
        }
    }

    private void printErrorMessage(String message) {
        System.out.println(Unicodes.red + message + Unicodes.reset);
    }

    private void printAttentionMessage(String message) {
        System.out.println(Unicodes.yellow + message + Unicodes.reset);
    }


    private boolean newsHaveChanged(Document freshNewsDocument) {
        return latestNewsDocument == null || !freshNewsDocument.text().equals(latestNewsDocument.text());
    }

    private Article getArticleFromDocument(Document document) {
        Element title = getDocumentTitle(document);
        Elements contents = getDocumentContents(document);
        String link = document.baseUri();
        Article article = new Article(title.text(), contents.text(), link);
        for (String url : getExternalLinksFromDocument(document)) article.appendLineToContent(url);
        return article;
    }

    private TreeSet<String> getExternalLinksFromDocument(Document document) {
        var links = document.select(".cs-post-panel").select("a[target=_blank]");
        TreeSet<String> urls = new TreeSet<>();
        for (Element link : links) {
            if (!link.text().isEmpty()) {
                urls.add(link.attr("abs:href"));
                System.out.println(link);
            }
        }
        return urls;
    }

    private ArrayList<String> getNewsLinks() {
        ArrayList<String> newsLinks = new ArrayList<>();
        Elements links = latestNewsDocument.select(".cs-campus-info").select("h6").select("a[href]");
        links.forEach(link -> newsLinks.add(link.attr("abs:href")));
        return newsLinks;
    }

    private void removeUnwantedElements(@NotNull Document document) {
        Elements elementsToRemove = document.select("a:contains(WordPress)," + "a:contains(online)," + "a:contains(free)," + "a:contains(course)," + "a:contains(udemy)").remove();
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

    public boolean needsToSendUpdates() {
        return needToSendUpdates;
    }

    public void setNeedToSendUpdates(boolean needToSendUpdates) {
        this.needToSendUpdates = needToSendUpdates;
    }
}
