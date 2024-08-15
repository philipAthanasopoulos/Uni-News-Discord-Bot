package scraper;

import app.Unicodes;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * This class implements the IScraper interface and provides methods to scrape HTML tags from websites.
 * It uses the Jsoup library to parse HTML and extract the text from the specified tag.
 * The scraped data is saved to a file in the specified directory.
 * The class provides methods to scrape tags from a single website, multiple websites, and multiple websites with multiple tags.
 * The class also includes color codes for console output to indicate success or failure of the scraping process.
 *
 * @author Philip Athanasopoulos
 */

public class Scraper implements IScraper {

    @Override
    public Document scrapeSite(String link) throws Exception {
        return Jsoup.connect(link).get();
    }

    @Override
    public String scrapeTagFromDocument(@NotNull Document document, String tag) {
        Elements links = document.select(tag);
        List<String> titles = new ArrayList<>();
        for (Element link : links) titles.add(link.text());
        StringBuilder result = new StringBuilder();
        for (String title : titles) result.append(title).append("\n");
        return result.toString();
    }

    @Override
    public Set<String> getListOfTags(@NotNull Document doc) {
        Set<String> tags = new TreeSet<>();
        for (Element element : doc.getAllElements()) tags.add(element.tagName());
        tags.remove("#root"); // Jsoup uses root to mark the beginning of the document , it's not an actual tag
        return tags;
    }

    @Override
    public Set<String> getListOfClasses(@NotNull Document doc) {
        Set<String> classes = new TreeSet<>();
        for (Element element : doc.getAllElements()) classes.add("." + element.className());
        classes.remove("."); // Classes should start with a dot
        // A dot by itself is not a class , not sure why Jsoup adds it
        return classes;
    }

    protected void printScraperInitializationMessage() {
        System.out.println(Unicodes.pink + "Scraper initialized!" + Unicodes.reset);
    }
}
