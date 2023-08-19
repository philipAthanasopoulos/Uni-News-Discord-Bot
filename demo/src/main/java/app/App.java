package app;

import scraper.Scraper;

import javafx.scene.input.KeyCode;
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

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


import org.jsoup.nodes.Document;

import app.resources.UnicodeColors;

public class App extends Application {
    
    private Document globalDocument = new Document("");
    private Scraper scraper = new Scraper();

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
        Button scrapeDocumentButton = new Button("Scrape");
        scrapeDocumentButton.getStyleClass().add("button");

        // HTML tag dropdown menu
        ComboBox<String> dropdown = new ComboBox<>();
        dropdown.getStyleClass().add("dropdown");
        dropdown.getItems().addAll("a", "h1", "h6");
        dropdown.setPromptText("Select a tag");

        // Add an event listener to the dropdown
        dropdown.setOnAction(e -> {
            String selectedOption = dropdown.getValue();
            System.out.println("Selected option: " + selectedOption);
        });
        
        // Submit button
        Button scrapeTagButton = new Button("Scrape specific tag");
        scrapeTagButton.getStyleClass().add("button");

        // Scraped results
        Label scrapedDataLabel = new Label("Scraped data will appear here");
        scrapedDataLabel.getStyleClass().add("scraped-data-label");

        // Layout with all elements
        VBox layout = new VBox();
        layout.getStyleClass().add("vbox");
        layout.getChildren().addAll(siteLabel, siteLabelInput, scrapeDocumentButton, dropdown,
                                    scrapeTagButton, scrapedDataLabel);

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
            Set<String> tagsAndClasses = new HashSet<>();
            tagsAndClasses.addAll(scraper.getListOfTags(globalDocument));
            tagsAndClasses.addAll(scraper.getListOfClasses(globalDocument));
            //add them to the dropdown
            dropdown.getItems().clear();
            dropdown.getItems().addAll(tagsAndClasses);
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

    

    
    
    
    public Document getDoc() {
        return globalDocument;
    }
    
    public void setDoc(Document doc) {
        this.globalDocument = doc;
    }

    public static void main(String[] args) {
        launch(args);
    }
}