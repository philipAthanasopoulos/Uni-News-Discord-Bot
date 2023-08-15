package app;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import scraper.Scraper;



public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Main window
        primaryStage.setTitle("Web Scraper");

        /*
         * Site link input
         */
        Label siteLabel = new Label("Enter a site to scrape:");
        siteLabel.setFont(Font.font(20));
        TextField siteLabelInput = new TextField();
        siteLabelInput.setFont(Font.font(20));
        GridPane.setConstraints(siteLabel, 0, 0);
        GridPane.setConstraints(siteLabelInput, 1, 0);

        /*
         * HTML tag input
         */
        Label tagLabel = new Label("Enter an HTML tag to scrape:");
        tagLabel.setFont(Font.font(20));
        TextField tagLabelInput = new TextField();
        tagLabelInput.setFont(Font.font(20));
        GridPane.setConstraints(tagLabel, 0, 1);
        GridPane.setConstraints(tagLabelInput, 1, 1);

        // submit button
        Button submitButton = new Button("Submit");
        submitButton.setFont(Font.font(20));
        Label scrapedDataLabel = new Label("Scraped data will appear here");
        scrapedDataLabel.setFont(Font.font(20));
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.getChildren().addAll(siteLabel, siteLabelInput, tagLabel, tagLabelInput, submitButton, scrapedDataLabel);
        layout.setAlignment(Pos.CENTER);

        // submit button action
        submitButton.setOnAction(e -> {
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
                e1.printStackTrace();
            }
        });

        Scene scene = new Scene(layout, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}