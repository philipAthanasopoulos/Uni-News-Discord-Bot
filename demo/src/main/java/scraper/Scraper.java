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

import app.resources.Unicodes;
import javafx.stage.FileChooser;

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
            System.out.println("Error: " + Unicodes.red + e + Unicodes.reset);
        }
        return doc;
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
        System.out.println(Unicodes.yellow + "Summarizing scraped data..." + Unicodes.reset);
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
            System.out.println("Error: " + Unicodes.red + e + Unicodes.reset);
        }
        System.out.println(Unicodes.green + "Summary complete!" + Unicodes.reset);
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

    public boolean saveScrapedText(String text) {
       FileChooser fileChooser = new FileChooser();
       fileChooser.setTitle("Save scraped text");
       fileChooser.getExtensionFilters().addAll(
           new FileChooser.ExtensionFilter("Text Files", "*.txt"),
           new FileChooser.ExtensionFilter("All Files", "*.*")
       );
       File file = fileChooser.showSaveDialog(null);
       if(file != null) {
              try {
                FileWriter writer = new FileWriter(file);
                writer.write(text);
                writer.close();
                return true;
              } catch (IOException e) {
                System.out.println("Error: " + Unicodes.red + e + Unicodes.reset);
                return false;
              }
         } else {
              return false;
       }

    }


}
