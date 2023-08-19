package scraper;

import org.jsoup.nodes.Document;

import java.io.File;
import java.util.List;
import java.util.Set;

/**
 * Interface for the Scraper class , which scrapes data from a site using the Jsoup library(@see documentaion).
 * 
 * @author Philip Athanasopoulos
 * @since 16-08-2023 
 *        MM-DD-YYYY
 */

public interface IScraper {
    Document scrapeSite(String link);

    /**
     * Scrapes a tag from a site and return the new file containing the scraped data
     * 
     * @param site the URL of the site to scrape
     * @param tag the HTML tag to scrape
     * @return the file containing the scraped data
     */
    File scrapeTagFromSite(String site, String tag);

    /**
     * Scrapes a tag from multiple sites and return a list of files each containing the scraped data from each site
     * 
     * @param sites the URLs of the sites to scrape
     * @param tag the HTML tag to scrape
     * @return a list of files containing the scraped data from each site
     */
    List<File> scrapeTagFromSite(List<String> sites, String tag);

    /**
     * Scrapes multiple tags from multiple sites and return a list of files each containing the scraped data from each site
     * 
     * @param sites the URLs of the sites to scrape
     * @param tags the HTML tags to scrape
     * @return a list of files containing the scraped data from each site
     */
    void scrapeTagFromSite(List<String> sites, List<String> tags);

    /**
     * @param doc
     * @param tag
     * @return
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
     *
     * @return a list of the tags that have been scraped
     */
    Set<String> getListOfTags(Document doc);

    /**
     * @param doc
     * @return
     */
    Set<String> getListOfClasses(Document doc);
}