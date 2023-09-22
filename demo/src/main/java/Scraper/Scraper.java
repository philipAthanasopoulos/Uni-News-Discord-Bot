package Scraper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.util.*;

import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import app.Unicodes;

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
        System.out.println(Unicodes.pink + "Scraper initialized!" + Unicodes.reset);
    }

    @Override
    public Document scrapeSite(String link) {
        try {
            Document doc;
            doc = Jsoup.connect(link).get();
            return doc;
        }
        catch (Exception e) {
            System.out.println("Error: " + Unicodes.red + e + Unicodes.reset);
            return null;
        }
    }

    @Override
    public String scrapeTagFromDocument(@NotNull Document doc, String tag) {
        Elements links = doc.select(tag);
        List<String> titles = new ArrayList<>();
        links.forEach(link -> titles.add(link.text()));
        StringBuilder result = new StringBuilder();
        for(String title : titles) result.append(title).append("\n");
        return result.toString();
    }

    @Override
    public String summarizeScrapedData() {
        System.out.println(Unicodes.yellow + "Summarizing scraped data..." + Unicodes.reset);
        StringBuilder summary = new StringBuilder();
        try {
            String path = System.getProperty("user.dir") + "/demo/src/main/python/summarizer/summarizer.py";
            String[] command = {"python", path};
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();
            File summaryFile = new File(System.getProperty("user.dir") + "/demo/src/main/java/app/outputs/summary.txt");
            Scanner scanner = new Scanner(summaryFile);
            while(scanner.hasNextLine()) summary.append(scanner.nextLine()).append("\n");
            scanner.close();
        } catch (IOException | InterruptedException e) {
            System.out.println("Error: " + Unicodes.red + e + Unicodes.reset);
        }
        System.out.println(Unicodes.green + "Summary complete!" + Unicodes.reset);
        return summary.toString();
    }

    @Override
    public Set<String> getListOfTags(@NotNull Document doc) {
        Set<String> tags = new TreeSet<>();
        Elements elements = doc.getAllElements();
        elements.forEach(element -> tags.add(element.tagName()));
        tags.remove("#root"); // Jsoup uses root to mark the beginning of the document , it's not an actual tag
        return tags;
    }

    @Override
    public Set<String> getListOfClasses(@NotNull Document doc) {
        Set<String> classes = new TreeSet<>();
        Elements elements = doc.getAllElements();
        elements.forEach(element -> classes.add("." + element.className())); // Classes should start with a dot
        classes.remove("."); // A dot by itself is not a class , not sure why Jsoup adds it
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

    public static void main(String[] args) {     
    }
}
