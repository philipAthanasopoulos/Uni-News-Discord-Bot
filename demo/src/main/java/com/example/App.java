package com.example;

import java.io.IOException;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws IOException {
        Scraper scraper = new Scraper();
        Scanner scanner = new Scanner(System.in);
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