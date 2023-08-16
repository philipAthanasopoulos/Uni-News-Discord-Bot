package scraper;

import java.io.File;
import java.util.List;

/**
 * Interface for the Scraper class , which scrapes data from a site using the Jsoup library(@see documentaion).
 * 
 * @author Philip Athanasopoulos
 * @since 16-08-2023 
 *        MM-DD-YYYY
 */

public interface IScraper {
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
     * Summarizes the scraped data. Composes a string containing the summary
     * 
     * @return a string with the summary of the scraped data
     * @throws Exception if no scraped data is available
     */
    String summarizeScrapedData() throws Exception;
}