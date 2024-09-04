package scraper;

import app.Unicodes;
import domain.Article;
import lombok.Getter;
import lombok.Setter;
import monitor.WebsiteMonitor;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.TreeSet;
import java.util.stream.Collectors;

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
    @Getter
    protected final ArrayList<Article> articles;
    protected final WebsiteMonitor websiteMonitor;
    @Setter
    protected Document latestNewsDocument = null;
    @Setter
    @Getter
    private volatile boolean needToSendUpdates = false;

    public UniversityNewsScraper(String newsLink) {
        this.newsLink = newsLink;
        articles = new ArrayList<>();
        websiteMonitor = new WebsiteMonitor(this);
        websiteMonitor.start();
        printScraperInitializationMessage();
    }

    private static Elements getDocumentContents(Document document) {
        return document.select(contentClassname);
    }

    private static Element getDocumentTitle(Document document) {
        return document.select(articleTitleClassname).first();
    }

    public void refreshNewsDocuments() {
        try {
            Document freshNewsDocument = scrapeSite(newsLink);
            if (newsHaveChanged(freshNewsDocument)) {
                setLatestNewsDocument(freshNewsDocument);
                clearArticles();
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
                case "connectException" -> printErrorMessage("Could not connect to CSE website");
                case "socketException" -> printErrorMessage("Something went wrong while trying to access socket");
                case "socketTimeoutException" -> printErrorMessage("Timed out while trying to receive data");
                default -> printErrorMessage("Something went wrong while refreshing the news");
            }
            printAttentionMessage();
            refreshNewsDocuments();
        }
    }

    private void clearArticles() {
        articles.clear();
    }

    private void printErrorMessage(String message) {
        System.out.println(Unicodes.red + message + Unicodes.reset);
    }

    private void printAttentionMessage() {
        System.out.println(Unicodes.yellow + "Retrying news refresh..." + Unicodes.reset);
    }

    private boolean newsHaveChanged(Document freshNewsDocument) {
        return latestNewsDocument == null || !freshNewsDocument.text().equals(latestNewsDocument.text());
    }

    private Article getArticleFromDocument(Document document) {
        StringBuilder contents = new StringBuilder(UniversityNewsScraper.getDocumentContents(document).text());
        getExternalLinksFromDocument(document).forEach(contents::append);

        String title = UniversityNewsScraper.getDocumentTitle(document).text();
        String link = document.baseUri();
        return new Article(title, contents.toString(), link);
    }

    protected TreeSet<String> getExternalLinksFromDocument(Document document) {
        return selectExternalLinksFromArticle(document).stream()
                .filter(link -> !link.text().isEmpty())
                .map(link -> link.attr("abs:href"))
                .collect(Collectors.toCollection(TreeSet::new));
    }

    protected abstract Elements selectExternalLinksFromArticle(Document document);

    protected ArrayList<String> getNewsLinks() {
        return selectNewsLinksFromDocument().stream().map(link -> link.attr("abs:href")).collect(Collectors.toCollection(ArrayList::new));
    }

    private void removeUnwantedElements(Document document) {
        Elements elementsToRemove = document.select(classnamesToBeRemoved).remove();
        elementsToRemove.forEach(Element::remove);
    }

    public Article getLatestArticle() {
        return this.articles.get(0);
    }

    protected abstract Elements selectNewsLinksFromDocument();
}
