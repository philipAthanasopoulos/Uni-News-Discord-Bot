package scraper;

import org.jsoup.nodes.Document;
import java.util.Set;

/**
 * Interface for the Scraper class , which scrapes data from a site using the Jsoup library(@see documentation).
 * 
 * @author Philip Athanasopoulos
 * @since 16-08-2023 (MM-DD-YYYY)
        
 */

public interface IScraper {
    Document scrapeSite(String link);

    /**
     * @param doc Document to scrape
     * @param tag to scrape from given document
     * @return the extracted text from the given tag
     */
    String scrapeTagFromDocument(Document doc , String tag);

    /**
     * Summarizes the scraped data. Composes a string containing the summary
     * 
     * @return a string with the summary of the scraped data
     * @throws Exception if no scraped data is available
     */
    String summarizeScrapedData() throws Exception;

    /**
     * Returns a list of the tags that have been scraped
     * @param doc the document to scrape
     * @return a set of the tags that have been scraped
     */
    Set<String> getListOfTags(Document doc);

    /**
     * @param doc the document to scrape
     * @return a set of the classes that have been scraped
     */
    Set<String> getListOfClasses(Document doc);
}