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
        primaryStage.setResizable(false);
        

        /*
         * Site link input
         */
        Label siteLabel = new Label("Enter a site to scrape:");
        siteLabel.getStyleClass().add("site-label");
        TextField siteLabelInput = new TextField();
        siteLabelInput.getStyleClass().add("site-input");
        siteLabelInput.setPromptText("e.g. https://www.example.com");

        /*
         * HTML tag input
         */
        Label tagLabel = new Label("Enter an HTML tag to scrape:");
        tagLabel.getStyleClass().add("tag-label");
        TextField tagLabelInput = new TextField();
        tagLabelInput.getStyleClass().add("tag-input");
        tagLabelInput.setPromptText("e.g. h1");
        GridPane.setConstraints(tagLabel, 0, 1);
        GridPane.setConstraints(tagLabelInput, 1, 1);

        // submit button
        Button submitButton = new Button("Submit");
        submitButton.getStyleClass().add("submit-button");
        Label scrapedDataLabel = new Label("Scraped data will appear here");
        scrapedDataLabel.getStyleClass().add("scraped-data-label");
        VBox layout = new VBox();
        layout.getStyleClass().add("vbox");
        layout.getChildren().addAll(siteLabel, siteLabelInput, tagLabel, tagLabelInput, submitButton, scrapedDataLabel);
        // layout.setAlignment(Pos.CENTER);

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
        scene.getStylesheets().add(getClass().getResource("styles/style.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();

        submitButton.requestFocus();

    }

    public static void main(String[] args) {
        launch(args);
    }
}