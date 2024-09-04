package scraper;

import domain.Article;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import javax.sql.rowset.BaseRowSet;

/**
 * @author Philip Athanasopoulos
 */
public class AuthNewsScraper extends UniversityNewsScraper{

    public AuthNewsScraper() {
        super("https://ece.auth.gr/anakoinoseis/");
        articleTitleClassname = ".elementor-heading-title";
        contentClassname = ".elementor-widget-theme-post-content";
        classnamesToBeRemoved = "a:contains(WordPress)," + "a:contains(online)," + "a:contains(free)," + "a:contains(course)," + "a:contains(udemy)";
    }
    @Override
    protected Elements selectExternalLinksFromArticle(Document document) {
        return document.select(".elementor-widget-container").select("a[target=_blank]");
    }


    @Override
    protected Elements selectNewsLinksFromDocument() {
        return latestNewsDocument.select(".ue-post-link-overlay").select("a[href]");
    }

    public static void main(String[] args) throws InterruptedException {
        AuthNewsScraper scraper = new AuthNewsScraper();
        Thread.sleep(20000);
        for(Article article: scraper.getArticles()) System.out.println(article.contents());
    }
}
