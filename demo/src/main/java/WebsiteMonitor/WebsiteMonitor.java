package WebsiteMonitor;

import Scraper.UoiScraper;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
public class WebsiteMonitor extends Thread {
    private UoiScraper scraper;

    public WebsiteMonitor(UoiScraper scraper) {
        this.scraper = scraper;
    }

    @Override
    public void run() {
        while (true) {
            scraper.refreshNewsDocuments();
            System.out.println("News refreshed at " + java.time.LocalTime.now());
            try {
                Thread.sleep(60000*10); //minutes
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}