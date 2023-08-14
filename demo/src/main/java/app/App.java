package app;

import java.io.IOException;
import java.util.Scanner;
import scraper.Scraper;


public class App {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        Scraper scraper = new Scraper();
        String site;
        String htmlTag;

        System.out.println("Enter a site to scrape: ");
        site = scanner.nextLine();
        
        System.out.println("Enter an HTML tag to scrape: ");
        htmlTag = scanner.nextLine();

        scraper.scrapeTagFromSite(site, htmlTag);

        scanner.close();
        
    }
}