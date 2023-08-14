/**
 * This class implements the IScraper interface and provides methods to scrape HTML tags from websites.
 * It uses the Jsoup library to parse HTML and extract the text from the specified tag.
 * The scraped data is saved to a file in the specified directory.
 * The class provides three methods to scrape tags from a single website, multiple websites, and multiple websites with multiple tags.
 * The class also includes color codes for console output to indicate success or failure of the scraping process.
 */
package com.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Scraper implements IScraper {
    //green color unicode
    String green = "\u001B[32m";
    //red color unicode
    String red = "\u001B[31m";
    //reset color unicode
    String reset = "\u001B[0m";

    public Scraper(){
    }
    
    @Override
    public File scrapeTagFromSite(String site, String tag) {
        
        try {
            System.out.println("Scraping "+ site + " ...");
        
            // Create a URL object for the website you want to scrape
            URI uri = URI.create(site);
            URL url = uri.toURL();
            
            // Open a connection to the URL and send an HTTP request
            URLConnection connection = url.openConnection();
        
            // Read the HTML response from the connection
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            reader.close();
        
            // Parse the HTML and extract the text
            Document doc = Jsoup.parse(builder.toString());
            Elements links = doc.select(tag);
            List<String> titles = new ArrayList<String>();
            for (Element link : links) {
                titles.add(link.text());
            }
        
            // Create a file object to save the results
            String siteName = site.replace("https://", "")
                                    .replace("http://", "")
                                    .replace("www", "")
                                    .replace("/", "")
                                    .replace(".", "")
                                    .replace(":", "");


            File file = new File("C:\\Users\\Philip\\Desktop\\Java Projects\\WebScraper\\demo\\src\\main\\java\\com\\example\\output." + siteName + ".txt");
            
            /*
             * Write the results to the file.
             */
            FileWriter  writer = new FileWriter(file );
            for (String title : titles) {
                writer.write(title + "\n");
            }

            writer.close();
            
            /*
             * If the file is empty, delete it and throw an exception.
             */
            if(!(file.length() > 0)) {
                file.delete();
                throw new Exception("No results found!");

            } 
            
        
            System.out.println(green + "Scrape complete!" + reset);

            return file;
            
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Error: " + red + e + reset);
            return null;
        }

    }

    


    @Override
    public List<File> scrapeTagFromSite(List<String> sites , String tag) {
        List<File> results = new ArrayList<File>();
        for(String site : sites){
            results.add(scrapeTagFromSite(site, tag)) ;
        } 
        return results;
    }

    @Override
    public void scrapeTagFromSite(List<String> sites , List<String> tags) {
        //key = site , value = tag
        Map<String , String> map = new HashMap<String , String>();
        for(int i = 0 ; i < sites.size() ; i++) map.put(sites.get(i), tags.get(i));

        for(String site : map.keySet()) scrapeTagFromSite(site, map.get(site));

    }
    
}
