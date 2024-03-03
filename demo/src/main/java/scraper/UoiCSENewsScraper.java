package scraper;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * This class is responsible for scraping the news from the CSE department of the University of Ioannina.
 *
 * @author Philip Athanasopoulos
 */
public class UoiCSENewsScraper extends UniversityNewsScraper {

    public UoiCSENewsScraper() {
        super("https://www.cse.uoi.gr/nea/");
        articleTitleClassname = ".cs-heading-sec";
        contentClassname = ".cs-editor-text";
        classnamesToBeRemoved = "a:contains(WordPress)," + "a:contains(online)," + "a:contains(free)," + "a:contains(course)," + "a:contains(udemy)";
    }

    @Override
    protected Elements selectExternalLinksFromArticle(Document document) {
        return document.select(".cs-post-panel").select("a[target=_blank]");
    }


    @Override
    protected Elements selecteNewsLinksFromDocument() {
        return latestNewsDocument.select(".cs-campus-info").select("h6").select("a[href]");
    }

    public static void main(String[] args) throws InterruptedException {
        UoiCSENewsScraper scraper = new UoiCSENewsScraper();
        System.out.println(scraper.getArticles().size());
        System.out.println(scraper.getLatestArticle());
    }
}
