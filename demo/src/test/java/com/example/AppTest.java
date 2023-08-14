package com.example;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void testCommonSites() throws Exception
    {

        IScraper scraper = new Scraper();
        List<String> urls = new ArrayList<String>();
        urls.add("https://www.google.com");
        urls.add("https://www.yahoo.com");
        urls.add("https://www.bing.com");
        urls.add("https://www.wikipedia.org");

        List<File> files = scraper.scrapeTagFromSite(urls , "a");
        for(File file : files){
            assertNotNull(file);
            assertTrue(file.exists());
            assertTrue(file.length() > 0);
        }

        
    }

    @Test
    public void existingFileDoesNotGetDeleted() throws Exception {
        IScraper scraper = new Scraper();
        File file = new File("output.googlecom.txt");
        FileWriter writer = new FileWriter(file);
        writer.write("This is a test");
        writer.close();
        

        String url = "https://www.google.com";
        scraper.scrapeTagFromSite(url, "h1");
        assertTrue(file.exists());
    }

}
