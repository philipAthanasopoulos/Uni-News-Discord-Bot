package monitor;

import Scraper.UoiScraper;
import app.Unicodes;

/**
 * The WebsiteMonitor class is responsible for refreshing the news articles.
 * It is a thread that runs in the background and refreshes the news articles every X minutes.
 * It is used to keep the news articles up to date.
 *
 * @authro Philip Athanasopoulos
 */
public class WebsiteMonitor extends Thread {
    private final UoiScraper scraper;

    public WebsiteMonitor(UoiScraper scraper) {
        this.scraper = scraper;
    }

    @Override
    public void run() {
        while (true) {
            scraper.refreshNewsDocuments();
            System.out.println("News refreshed at " + java.time.LocalTime.now());
            try {
                int refreshRateInMinutes = 10;
                Thread.sleep(60000 * refreshRateInMinutes); //minutes
            } catch (Exception e) {
                System.out.println(Unicodes.red + "WebsiteMonitor thread interrupted!" + Unicodes.reset);
            }
        }
    }
}