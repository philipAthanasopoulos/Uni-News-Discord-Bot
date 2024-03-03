package scraper;

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
 * This is an abstract class responsible for fetching news from a specified website and
 * generate news article objects.
 *
 * @author Philip Athanasopoulos
 */
public abstract class UniversityNewsScraper extends Scraper {
    protected static String articleTitleClassname;
    protected static String contentClassname;
    protected String classnamesToBeRemoved;
    protected String newsLink;
    protected final ArrayList<Article> articles;
    protected final WebsiteMonitor websiteMonitor;
    protected Document latestNewsDocument = null;
    private volatile boolean needToSendUpdates = false;

    public UniversityNewsScraper(String newsLink) {
        this.newsLink = newsLink;
        articles = new ArrayList<>();
        websiteMonitor = new WebsiteMonitor(this);
        websiteMonitor.start();
        printScraperInitializationMessage();
    }

    @NotNull
    private static Elements getDocumentContents(@NotNull Document document) {
        return document.select(contentClassname);
    }

    @Nullable
    private static Element getDocumentTitle(@NotNull Document document) {
        return document.select(articleTitleClassname).first();
    }

    public ArrayList<Article> getArticles() {
        return articles;
    }

    public void refreshNewsDocuments() {
        try {
            Document freshNewsDocument = scrapeSite(newsLink);
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
            System.out.println(exception.getMessage());
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
        Element title = UniversityNewsScraper.getDocumentTitle(document);
        Elements contents = UniversityNewsScraper.getDocumentContents(document);
        String link = document.baseUri();
        Article article = new Article(title.text(), contents.text(), link);
        for (String url : getExternalLinksFromDocument(document)) article.appendLineToContent(url);
        return article;
    }

    protected TreeSet<String> getExternalLinksFromDocument(Document document) {
        var links = selectExternalLinksFromArticle(document);
        TreeSet<String> urls = new TreeSet<>();
        for (Element link : links)
            if (!link.text().isEmpty()) urls.add(link.attr("abs:href"));
        return urls;
    }

    protected abstract Elements selectExternalLinksFromArticle(Document document);

    protected ArrayList<String> getNewsLinks() {
        ArrayList<String> newsLinks = new ArrayList<>();
        Elements links = selecteNewsLinksFromDocument();
        links.forEach(link -> newsLinks.add(link.attr("abs:href")));
        return newsLinks;
    }

    private void removeUnwantedElements(@NotNull Document document) {
        Elements elementsToRemove = document.select(classnamesToBeRemoved).remove();
        elementsToRemove.forEach(Element::remove);
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

    protected abstract Elements selecteNewsLinksFromDocument();
}
