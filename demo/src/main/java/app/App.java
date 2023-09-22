package app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jsoup.nodes.Document;
import Scraper.Scraper;
import java.util.Set;
import java.util.TreeSet;


public class App extends Application {
    
    private Document globalDocument = null;
    private final Scraper scraper = new Scraper();

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

        // Scrape button    
        Button scrapeDocumentButton = new Button("Scrape site " + Unicodes.magnifyingGlassEmoji);
        scrapeDocumentButton.getStyleClass().add("button");

        // HTML tag dropdown menu
        ComboBox<String> dropdown = new ComboBox<>();
        dropdown.getStyleClass().add("dropdown");
        dropdown.setPromptText("Select a tag");
        dropdown.setDisable(true);

        // Add an event listener to the dropdown
        dropdown.setOnAction(e -> {
            String selectedOption = dropdown.getValue();
            System.out.println("Selected option: " + selectedOption);
        });
        
        // Submit button
        Button scrapeTagButton = new Button("Scrape specific tag " + Unicodes.htmlTagEmoji);
        scrapeTagButton.getStyleClass().add("button");
        scrapeTagButton.setDisable(true);

        // Scraped results
        Label scrapedDataLabel = new Label();
        scrapedDataLabel.getStyleClass().add("scraped-data-label");
        
        // Save button
        Button saveButton = new Button("Save " + Unicodes.saveEmoji);
        saveButton.getStyleClass().add("button");
        saveButton.setDisable(true);

        // Preview of website

        // Layout with all elements
        VBox layout = new VBox();
        layout.getStyleClass().add("vbox");
        layout.getChildren().addAll(siteLabel, siteLabelInput, scrapeDocumentButton, dropdown,
                                    scrapeTagButton, saveButton, scrapedDataLabel);

        // Submit button action
        scrapeDocumentButton.setOnAction(e -> {
            String siteLink = siteLabelInput.getText();
            globalDocument = scraper.scrapeSite(siteLink);

            if(siteLabelInput.getText().isEmpty() || globalDocument == null) {
                // Alert the user to enter a site
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Wrong input");
                alert.setHeaderText("Please enter a valid site URL");
                alert.showAndWait();
                return;             
            }
            Set<String> tagsAndClasses = new TreeSet<>();
            tagsAndClasses.addAll(scraper.getListOfTags(globalDocument));
            tagsAndClasses.addAll(scraper.getListOfClasses(globalDocument));
            // Add them to the dropdown
            dropdown.getItems().clear();
            dropdown.getItems().addAll(tagsAndClasses);
            // Enable the dropdown and the submit button
            dropdown.setDisable(false);
            dropdown.setPromptText("Select a tag");
            scrapeTagButton.setDisable(false);
        });

        scrapeTagButton.setOnAction(e -> {
            if(dropdown.getValue() == null) {
                //alert the user to select a tag
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Wrong input");
                alert.setHeaderText("Please enter a tag");
                alert.showAndWait();
                return;
            }
            String tag = dropdown.getValue();
            String result = scraper.scrapeTagFromDocument(globalDocument, tag);
            scrapedDataLabel.setText(result);
            saveButton.setDisable(false);
        });

        saveButton.setOnAction( e-> {
            String textToSave = scrapedDataLabel.getText();
            boolean save = scraper.saveScrapedText(textToSave);
            Alert alert;
            if(save) {
                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("Document saved successfully");
            } else {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Document could not be saved");
            }
            alert.showAndWait();
        });


        Scene scene = new Scene(layout, 600, 600);

        // Add stylesheet and logo
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        primaryStage.getIcons().add(new javafx.scene.image.Image(getClass().getResource("/img/green_logo.png").toExternalForm()));
        primaryStage.setScene(scene);
        primaryStage.show();

        // Take focus away from the site input
        scrapeDocumentButton.requestFocus();
    }

    public static void main(String[] args) {
        launch(args);
    }
}