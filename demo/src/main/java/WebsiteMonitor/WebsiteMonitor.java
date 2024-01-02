package WebsiteMonitor;

import Scraper.UoiScraper;
import app.Unicodes;

/**
 * The WebsiteMonitor class is responsible for refreshing the news articles.
 * It is a thread that runs in the background and refreshes the news articles every X minutes.
 * It is used to keep the news articles up to date.
 */
public class WebsiteMonitor extends Thread {
    private final UoiScraper scraper;
    private int refreshRateInMinutes = 10;

    public WebsiteMonitor(UoiScraper scraper) {
        this.scraper = scraper;
    }

    @Override
    public void run() {
        while(true) {
            scraper.refreshNewsDocuments();
            System.out.println("News refreshed at " + java.time.LocalTime.now());
            try {
                Thread.sleep(60000L * refreshRateInMinutes); //minutes
            } catch (InterruptedException e) {
                System.out.println(Unicodes.red + "WebsiteMonitor thread interrupted!" + Unicodes.reset);
            }
        }
    }
}