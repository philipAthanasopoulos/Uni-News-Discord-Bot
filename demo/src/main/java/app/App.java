package app;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import javax.swing.*;
import java.awt.GridLayout;
import scraper.Scraper;


public class App {
    public static void main(String[] args) throws IOException {
        // Main window
        JFrame window = new JFrame("Web Scraper");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
        window.setLayout(new GridLayout(5, 1));
        window.setSize(500, 500);



        /*
         * Site link  input
         */
        JLabel siteLabel = new JLabel();
        siteLabel.setLayout(new GridLayout(1, 2));

        JLabel siteLabelText = new JLabel("Enter a site to scrape: ");
        siteLabelText.setFont(siteLabelText.getFont().deriveFont(20.0f));
        siteLabel.add(siteLabelText);

        JTextField siteLabelInput = new JTextField(40);
        siteLabelInput.setFont(siteLabelText.getFont().deriveFont(20.0f));
        siteLabel.add(siteLabelInput);
        window.add(siteLabel);


        /*
         * HTML tag input
         */
        JLabel tagLabel = new JLabel();
        tagLabel.setLayout(new GridLayout(1, 2));

        JLabel tagLabelText = new JLabel("Enter an HTML tag to scrape: ");
        tagLabelText.setFont(siteLabelText.getFont().deriveFont(20.0f));
        tagLabel.add(tagLabelText);

        JTextField tagLabelInput = new JTextField();
        tagLabelInput.setFont(siteLabelText.getFont().deriveFont(20.0f));
        tagLabel.add(tagLabelInput);
        window.add(tagLabel);



        // submit button
        JButton submitButton = new JButton("Submit");
        window.add(submitButton);
        JLabel scrapedDataLabel = new JLabel("Scraped data will appear here");
        window.add(scrapedDataLabel);
        //change dimensions
        submitButton.setSize(50, 10);
        submitButton.addActionListener(e -> {
            Scraper scraper = new Scraper();
            String siteLink = siteLabelInput.getText();
            String htmlTag = tagLabelInput.getText();
            File scrapedData = scraper.scrapeTagFromSite(siteLink, htmlTag);

            try (Scanner scanner = new Scanner(scrapedData)) {
                String scrapedDataString = "";
                while (scanner.hasNextLine()) {
                    scrapedDataString += scanner.nextLine() + " | ";                    
                }
                scanner.close();
                scrapedDataLabel.setText(scrapedDataString);
            } catch (FileNotFoundException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });
    }
}