package app;

import scraper.Scraper;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {

        // Main window
        primaryStage.setTitle("Web Scraper");

        // Window expansion constraints
        primaryStage.setResizable(true);
        primaryStage.setMinWidth(600);
        primaryStage.setMinHeight(600);

        // Site link input
        Label siteLabel = new Label("Enter a site to scrape:");
        siteLabel.getStyleClass().add("site-label");
        TextField siteLabelInput = new TextField();
        siteLabelInput.getStyleClass().add("site-input");
        siteLabelInput.setPromptText("e.g. https://www.example.com");

        // HTML tag input
        Label tagLabel = new Label("Enter an HTML tag to scrape:");
        tagLabel.getStyleClass().add("tag-label");
        TextField tagLabelInput = new TextField();
        tagLabelInput.getStyleClass().add("tag-input");
        tagLabelInput.setPromptText("e.g. h1");

        // Submit button
        Button submitButton = new Button("Submit");
        submitButton.getStyleClass().add("submit-button");
        Label scrapedDataLabel = new Label("Scraped data will appear here");
        scrapedDataLabel.getStyleClass().add("scraped-data-label");

        // Layout with all elements
        VBox layout = new VBox();
        layout.getStyleClass().add("vbox");
        layout.getChildren().addAll(siteLabel, siteLabelInput, tagLabel, tagLabelInput,
                                    submitButton, scrapedDataLabel);

        // Submit button action
        submitButton.setOnAction(e -> {
            Scraper scraper = new Scraper();
            String siteLink = siteLabelInput.getText();
            String htmlTag = tagLabelInput.getText();
            scraper.scrapeTagFromSite(siteLink, htmlTag);
            String summary = scraper.summarizeScrapedData();
            scrapedDataLabel.setText(summary);
        });

        // If ENTER is pressed execute submit button action
        layout.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case ENTER:
                    submitButton.fire();
                    break;
                default:
                    break;
            }
        });

        // Add layout to scene
        Scene scene = new Scene(layout, 600, 600);
        scene.getStylesheets().add(getClass().getResource("resources/styles/style.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();

        // Add logo
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("resources/img/logo.png")));
        submitButton.requestFocus();
    }

    public static void main(String[] args) {
        launch(args);
    }
}