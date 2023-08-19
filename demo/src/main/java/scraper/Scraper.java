package scraper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import app.resources.UnicodeColors;

/**
 * This class implements the IScraper interface and provides methods to scrape HTML tags from websites.
 * It uses the Jsoup library to parse HTML and extract the text from the specified tag.
 * The scraped data is saved to a file in the specified directory.
 * The class provides methods to scrape tags from a single website, multiple websites, and multiple websites with multiple tags.
 * The class also includes color codes for console output to indicate success or failure of the scraping process.
 * 
 * @author Philip Athanasopoulos
 */

public class Scraper implements IScraper {

    public Scraper(){
        System.out.println("Scraper initialized!");
    }

    @Override
    public Document scrapeSite(String link) {
        Document doc = null;
        try {
            // Create a URL object for the website you want to scrape
            URI uri = URI.create(link);
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
            doc = Jsoup.parse(builder.toString());
        }
        catch (Exception e) {
            System.out.println("Error: " + UnicodeColors.red + e + UnicodeColors.reset);
        }
        return doc;

    }
    
    @Override
    public File scrapeTagFromSite(String site, String tag) {
        
        try {
            Document doc = scrapeSite(site);
            Set<String> tags = getListOfTags(doc);
            Set<String> classes = getListOfClasses(doc);
            for(String each: tags) System.out.println(each);
            for(String each: classes) System.out.println(each);

            Elements links = doc.select(tag);
            List<String> titles = new ArrayList<>();
            for (Element link : links) {
                titles.add(link.text());
            }
        
            // Create a file object to save the results
            String path = System.getProperty("user.dir") + "/demo/src/main/java/app/outputs/" + "output.txt";
            File file = new File(path);
            
            // Write the results to the file
            System.out.println("Saving results to " + file.getAbsolutePath());          
            FileWriter  writer = new FileWriter(file );
            for (String title : titles) {
                writer.write(title + "\n");
            }
            writer.close();
            
            // Check if file is empty
            if(!(file.length() > 0)) {
                writer.write("No results found!");
                throw new Exception("No results found!");
            } 
            
            System.out.println( UnicodeColors.green + "Scrape complete!" + UnicodeColors.reset);
            return file;
            
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Error: " + UnicodeColors.red + e + UnicodeColors.reset);
            return null;
        }
    }

    @Override
    public List<File> scrapeTagFromSite(List<String> sites , String tag) {
        List<File> results = new ArrayList<>();
        for(String site : sites){
            results.add(scrapeTagFromSite(site, tag)) ;
        }
        return results;
    }

    @Override
    public void scrapeTagFromSite(List<String> sites , List<String> tags) {
        // key = site , value = tag
        Map<String , String> map = new HashMap<>();
        for(int i = 0 ; i < sites.size() ; i++) map.put(sites.get(i), tags.get(i));

        for(String site : map.keySet()) scrapeTagFromSite(site, map.get(site));
    }

    @Override
    public String scrapeTagFromDocument(Document doc, String tag) {
        Elements links = doc.select(tag);
        List<String> titles = new ArrayList<>();
        for (Element link : links) {
            titles.add(link.text());
        }
        String result = "";
        for(String title : titles) result += title + "\n";
        return result;
    }

    @Override
    public String summarizeScrapedData() {
        System.out.println(UnicodeColors.yellow + "Summarizing scraped data..." + UnicodeColors.reset);
        String summary = "";
        try {
            String path = System.getProperty("user.dir") + "/demo/src/main/python/summarizer/summarizer.py";
            String[] command = {"python", path};
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();
            File summaryFile = new File(System.getProperty("user.dir") + "/demo/src/main/java/app/outputs/summary.txt");
            Scanner scanner = new Scanner(summaryFile);
            while(scanner.hasNextLine()) summary += scanner.nextLine() + "\n";
            scanner.close();
        } catch (IOException | InterruptedException e) {
            System.out.println("Error: " + UnicodeColors.red + e + UnicodeColors.reset);
        }
        System.out.println(UnicodeColors.green + "Summary complete!" + UnicodeColors.reset);
        return summary;
    }

    @Override
    public Set<String> getListOfTags(Document doc) {
        Set<String> tags = new TreeSet<String>();
        Elements elements = doc.getAllElements();
        for(Element element : elements) tags.add(element.tagName());
        return tags;
    }

    @Override
    public Set<String> getListOfClasses(Document doc) {
        Set<String> classes = new TreeSet<>();
        Elements elements = doc.getAllElements();
        for(Element element : elements) classes.add("." + element.className()); // Classes should start with a dot
        return classes;
    }


}
