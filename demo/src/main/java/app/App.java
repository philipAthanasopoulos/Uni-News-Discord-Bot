package app;

import scraper.Scraper;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Set;
import java.util.TreeSet;

import org.jsoup.nodes.Document;

import app.resources.Unicodes;


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
        
        Button saveButton = new Button("Save " + Unicodes.saveEmoji);
        saveButton.getStyleClass().add("button");
        saveButton.setDisable(true);

        // Layout with all elements
        VBox layout = new VBox();
        layout.getStyleClass().add("vbox");
        layout.getChildren().addAll(siteLabel, siteLabelInput, scrapeDocumentButton, dropdown,
                                    scrapeTagButton, saveButton, scrapedDataLabel);

        // Submit button action
        scrapeDocumentButton.setOnAction(e -> {
            if(siteLabelInput.getText().isEmpty()) {
                //alert the user to enter a site
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Wrong input");
                alert.setHeaderText("Please enter a valid site URL");
                alert.showAndWait();
                return;
            }

            String siteLink = siteLabelInput.getText();
            globalDocument = scraper.scrapeSite(siteLink);
            Set<String> tagsAndClasses = new TreeSet<>();
            scraper.getListOfTags(globalDocument).forEach(htmlTag -> tagsAndClasses.add(htmlTag));
            scraper.getListOfClasses(globalDocument).forEach(htmlClass -> tagsAndClasses.add(htmlClass));

            //add them to the dropdown
            dropdown.getItems().clear();
            dropdown.getItems().addAll(tagsAndClasses);

            //enable the dropdown and the submit button
            dropdown.setDisable(false);
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
            if(save) {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("Document saved successfully");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Document could not be saved");
                alert.showAndWait();
            }
        });

        // Add layout to scene
        Scene scene = new Scene(layout, 600, 600);
        scene.getStylesheets().add(getClass().getResource("resources/styles/style.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();

        // Add logo
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("resources/img/logo.png")));
        scrapeDocumentButton.requestFocus();
    }

    public static void main(String[] args) {
        launch(args);
    }
}