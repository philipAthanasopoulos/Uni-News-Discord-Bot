package monitor;

import discord.BotListeners;
import scraper.UniversityNewsScraper;
import app.Unicodes;

public class NewsMonitor extends Thread {
    private final UniversityNewsScraper scraper;
    public BotListeners listeners;

    public NewsMonitor(UniversityNewsScraper scraper, BotListeners listeners) {
        this.scraper = scraper;
        this.listeners = listeners;
    }

    @Override
    public void run() {
        while (true) try {
            if (scraper.needsToSendUpdates()) {
                listeners.sendNewsInSlideShowToAllServers();
                scraper.setNeedToSendUpdates(false);
            }
        } catch (Exception e) {
            System.out.println(Unicodes.red + "Something went wrong while checking for news changes" + Unicodes.reset);
        }
    }
}
