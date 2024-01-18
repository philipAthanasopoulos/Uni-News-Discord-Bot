package Scraper;

import WebsiteMonitor.WebsiteMonitor;
import app.Unicodes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

/**
 * This class is responsible for scraping the news from the CSE department of the University of Ioannina.
 *
 * @author Philip Athanasopoulos
 */
public class UoiScraper extends Scraper {
    private final String CSElink = "https://www.cse.uoi.gr/";
    private final String newsEndpoint = CSElink + "nea/";
    private Document latestNewsDocument = null;
    private final ArrayList<Article> articles;
    private final WebsiteMonitor websiteMonitor;
    private volatile boolean needToSendUpdates = false;

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
        try {
            Document freshNewsDocument = scrapeSite(newsEndpoint);
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
        } catch (ConnectException connectException) {
            System.out.println(Unicodes.red + "Could not connect to CSE website" + Unicodes.reset);
        } catch (SocketException socketException) {
            System.out.println(Unicodes.red + "Something went wrong while trying to access socket" + Unicodes.reset);
        } catch (SocketTimeoutException socketTimeoutException) {
            System.out.println(Unicodes.red + "Timed out while trying to receive data" + Unicodes.reset);
        } catch (Exception e) {
            System.out.println(Unicodes.red + "Something went wrong while refreshing the news!" + Unicodes.reset);
        } finally {
            refreshNewsDocuments();
        }
    }

    private boolean newsHaveChanged(Document freshNewsDocument) {
        return latestNewsDocument == null || !freshNewsDocument.text().equals(latestNewsDocument.text());
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
