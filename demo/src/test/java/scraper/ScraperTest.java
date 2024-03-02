package scraper;

import app.Unicodes;

import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;


public class ScraperTest {

    private static Scraper scraper = new Scraper();

    @Test
    public void scrapeSiteTest() throws Exception {
        ArrayList<String> links = new ArrayList<>();
        links.add("https://www.example.com");
        links.add("https://www.google.com");
        links.add("https://www.github.com");
        links.add("https://www.stackoverflow.com");
        links.add("https://www.wikipedia.org");
        links.add("https://www.nytimes.com");
        links.add("https://www.microsoft.com");
        links.add("https://www.apple.com");
        links.add("https://www.reddit.com");

        for (String linkToTest: links) {
            Assertions.assertNotNull(scraper.scrapeSite(linkToTest), Unicodes.red + "Failed to scrape " + linkToTest + Unicodes.reset);
        }
    }

    @Test
    public void scrapeTagFromDocumentTest() throws Exception {
        String link = "https://www.example.com";
        Document doc = scraper.scrapeSite(link);
        String tag = "title";
        String expectedText = "Example Domain" + "\n";
        String actualText = scraper.scrapeTagFromDocument(doc, tag);
        Assertions.assertEquals(expectedText, actualText);
    }

    @Test
    public void getListOfTagsTest() throws Exception {
        String link = "https://www.example.com";
        Document doc = scraper.scrapeSite(link);
        Set<String> tags = scraper.getListOfTags(doc);
        Set<String> expectedTags = new java.util.HashSet<>(Arrays.asList("a", "body", "div", "h1", "head", "html", "meta", "p", "style", "title"));
        Assertions.assertEquals(expectedTags, tags);
    }

    @Test
    public void getListOfClassesTest() throws Exception {
        String link = "https://www.example.com";
        Document doc = scraper.scrapeSite(link);
        Set<String> classes = scraper.getListOfClasses(doc);
        Set<String> expectedClasses = new java.util.TreeSet<>();
        Assertions.assertEquals(expectedClasses, classes);
    }
}