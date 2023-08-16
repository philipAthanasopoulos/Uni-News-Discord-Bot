package scraper;

import java.io.File;
import java.util.List;

public interface IScraper {
    /**
     * Scrape a tag from a site and return the new file containing the scraped data
     * 
     * @param site the URL of the site to scrape
     * @param tag the HTML tag to scrape
     * @return the file containing the scraped data
     * @author Philip Athanasopoulos
     */
    File scrapeTagFromSite(String site, String tag);

    /**
     * Scrape a tag from multiple sites and return a list of files each containing the scraped data from each site
     * 
     * @param sites the URLs of the sites to scrape
     * @param tag the HTML tag to scrape
     * @return a list of files containing the scraped data from each site
     */
    List<File> scrapeTagFromSite(List<String> sites, String tag);

    /**
     * Scrape multiple tags from multiple sites and return a list of files each containing the scraped data from each site
     * 
     * @param sites the URLs of the sites to scrape
     * @param tags the HTML tags to scrape
     */
    void scrapeTagFromSite(List<String> sites, List<String> tags);

    /**
     * Summarize the scraped data and return a string containing the summary
     * 
     * @return the summary of the scraped data
     * @throws Exception if no scraped data is available
     */
    String summarizeScrapedData() throws Exception;
}