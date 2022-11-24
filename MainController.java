import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The main contoller class is responsible for starting and running the application. It handles core functionalities
 * such as loading all relevant data and initializing left and right button clicks. Price range values are added 
 * and validation checks are also handled. The pane for each class is initialized in a specific order along with 
 * corresponding layout files created in 'Scene Builder'. The animation transition for each pane is calculated
 * and set within this class.  
 *
 * @author David Wang (k19005122), Savraj Bassi (k19029720), Edis Emin (k1921655), Sean McGrory (k1922054)
 * @version 1.6
 */
public class MainController extends Application {
    @FXML//ComboBox allows a user to select a minimum price range on the pane
    private ComboBox minimumPriceComboBox;

    @FXML//ComboBox allows a user to select a maximum price range on the pane
    private ComboBox maximumPriceComboBox;

    @FXML//Allows a user to navigate to the previous pane
    private Button leftButton;

    @FXML//Allows a user to navigate to next pane
    private Button rightButton;

    @FXML//A layout pane variable which allows child nodes to be placed in the form of a stack
    private StackPane stackPane;

    // A list which stores all the AirbnbListing properties
    private ArrayList<AirbnbListing> listings;

    // A pane for the Welcome pane
    private Pane welcomePane;

    // A pane for the Welcome pane controller
    private WelcomePaneController welcomePaneController;

    // A pane for the Map pane
    private Pane mapPane;

    // A pane for the Map pane controller
    private MapPaneController mapPaneController;

    // A pane for the Statistics pane
    private Pane statisticsPane;

    // A pane for the Statistics pane controller
    private StatisticsPaneController statisticsPaneController;

    // A pane for the Challenge pane
    private Pane challengePane;

    // A pane for the Challenge pane controller
    private ChallengePaneController challengePaneController;

    private boolean buttonsDisabled = true;
    private Integer minPrice = null;
    private Integer maxPrice = null;

    private Pane front;
    private Pane right;
    private Pane back;
    private Pane left;

    /**
     * The start method is the main entry point for every JavaFX application. It is
     * called after the init() method has returned and after the system is ready for
     * the application to begin running.
     *
     * @param stage the primary stage for this application.
     */
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("London Property Viewer");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method loads all the relevant data from the 'AirbnbDataLoader' class.
     * Get methods are used to retrieve minimum and maximum price ranges.
     * Left and right button clicks for the user are enabled by setting the setOnAction method.
     */
    @FXML
    public void initialize() {
        // Load listings into an array
        AirbnbDataLoader dataLoader = new AirbnbDataLoader();
        listings = dataLoader.load();

        // Add minimum price values
        minimumPriceComboBox.getItems().addAll("£0", "£10", "£20", "£50", "£100", "£200", "£500", "£1000", "£2500", "£5000");

        // Add maximum price values
        maximumPriceComboBox.getItems().addAll("£10", "£20", "£50", "£100", "£200", "£500", "£1000", "£2500", "£5000", "£10000");

        // Set up left and right button actions
        leftButton.setOnAction(this::leftButtonClick);
        rightButton.setOnAction(this::rightButtonClick);

        // Initialize all panes and then the stack pane.
        initializeWelcomePane();
        initializeMapPane();
        initializeStatisticsPane();
        initializeChallengePane();
        initializeStackPane();
    }

    /**
     * The action which takes place when a user modifies the price range.
     * @param event ActionEvent results when a user alters the price range
     */
    @FXML
    private void priceRangeModified(ActionEvent event) {
        // Check if minimum price value is not empty
        if (minimumPriceComboBox.getValue() != null) {
            // Get the minimum price value as a number
            String minPriceString = (String) minimumPriceComboBox.getValue();
            minPrice = Integer.valueOf(minPriceString.substring(1));
        }
        
        // Check if maximum price value is not empty
        if (maximumPriceComboBox.getValue() != null) {
            String maxPriceString = (String) maximumPriceComboBox.getValue();
            // Get the maximum price value as a number
            maxPrice = Integer.valueOf(maxPriceString.substring(1));
        }
        
        // Only attempt to check that the prices are valid if both are not null
        if (minPrice != null && maxPrice != null) {
            // If minimum price is greater than maximum price
            if (minPrice > maxPrice) {
                // Raise error for invalid price range
                ComboBox combobox = (ComboBox) event.getSource();
                combobox.hide();
                invalidPriceRange();
            }
            else {
                // Since the price range is not invalid get the min and max values
                String minPrice = (String) minimumPriceComboBox.getValue();
                String maxPrice = (String) maximumPriceComboBox.getValue();
                // Update the minimum and maximum price labels and make them visible
                welcomePaneController.updatePriceLabel(minPrice, maxPrice);

                // Turn the min and max values into numbers
                int minPriceValue = Integer.parseInt(minPrice.substring(1, minPrice.length()));
                int maxPriceValue = Integer.parseInt(maxPrice.substring(1, maxPrice.length()));

                // Update statistics for this price range
                statisticsPaneController.updateStatisticsValues(minPriceValue, maxPriceValue);

                // Enable navigation buttons if not already enabled
                if (buttonsDisabled) {
                    enableButtons();
                }
            }
        }
        
    }
    /**
     * A function which checks whether the user has entered an ivalid price range,
     * if so an error mesaage is displayed.
     */
    private void invalidPriceRange() {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Invalid price range entered");
        alert.setContentText("The minimum price cannot exceed the maximum price.");
        alert.showAndWait();

    }

    /**
     * A function to enable buttons if the minimum and maximum values are not null
     */
    private void enableButtons() {
        // Check if min and max price values are not empty
        if (minimumPriceComboBox.getValue() != null && maximumPriceComboBox.getValue() != null) {
            // Enable the buttons
            leftButton.setDisable(false);
            rightButton.setDisable(false);
            buttonsDisabled = false;
        }
    }

    /**
     * On left button click action. Create transitions between different panes.
     */
    private void leftButtonClick(ActionEvent event) {
        // Get new front, back, left, right panes for left turn
        front = (Pane) stackPane.getChildren().get(0);
        left  = (Pane) stackPane.getChildren().get(1);
        back  = (Pane) stackPane.getChildren().get(2);
        right = (Pane) stackPane.getChildren().get(3);

        // Get stack pane width
        Double paneWidth = stackPane.getWidth();

        // Set left and back to be invisible as they won't be seen
        left.setVisible(false);
        back.setVisible(false);
        right.setVisible(true);
        front.setVisible(true);

        // Clear stack pane children
        stackPane.getChildren().clear();
        
        // Insert children in new order (left, back, right, front)
        stackPane.getChildren().add(left);
        stackPane.getChildren().add(back);
        stackPane.getChildren().add(right);
        stackPane.getChildren().add(front);

        // Create transition for front pane to move to the right
        TranslateTransition rightTransition = new TranslateTransition(Duration.millis(300), right);
        rightTransition.setFromX(0);
        rightTransition.setToX(paneWidth);
        
        // Create transition for left pane to move to the center
        TranslateTransition frontTransition = new TranslateTransition(Duration.millis(300), front);
        frontTransition.setFromX(-paneWidth);
        frontTransition.setToX(0);
        
        // Play transitions
        rightTransition.play();
        frontTransition.play();
    }

    private void rightButtonClick(ActionEvent event) {
        // Get new front, back, left, right panes for left turn
        back  = (Pane) stackPane.getChildren().get(0);
        right = (Pane) stackPane.getChildren().get(1);
        front = (Pane) stackPane.getChildren().get(2);
        left  = (Pane) stackPane.getChildren().get(3);

        // Get stack pane width
        Double paneWidth = stackPane.getWidth();

        // Set right and back to be invisible as they won't be seen
        left.setVisible(true);
        back.setVisible(false);
        right.setVisible(false);
        front.setVisible(true);

        // Clear stack pane children
        stackPane.getChildren().clear();
        
        // Insert children in new order (left, back, right, front)
        stackPane.getChildren().add(left);
        stackPane.getChildren().add(back);
        stackPane.getChildren().add(right);
        stackPane.getChildren().add(front);

        // Create transition for front pane to move to the left
        TranslateTransition leftTransition = new TranslateTransition(Duration.millis(300), left);
        leftTransition.setFromX(0);
        leftTransition.setToX(-paneWidth);

        // Create transition for right pane to move to the center
        TranslateTransition frontTransition = new TranslateTransition(Duration.millis(300), front);
        frontTransition.setFromX(paneWidth);
        frontTransition.setToX(0);

        // Play transitions
        frontTransition.play();
        leftTransition.play();
    }

    /**
     * Initialize welcome pane with it's layout file.
     */
    private void initializeWelcomePane() {
        // Try loading FXML, otherwise raise exception
        try {
            // Load FXML file and get the controller for the stage
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("WelcomePane.fxml"));
            welcomePane = (AnchorPane) loader.load();
            welcomePaneController = loader.getController();

        } catch (IOException e) {
        }
    }

    /**
     * Initialize map pane with it's layout file.
     */
    private void initializeMapPane() {
        // Try loading FXML, otherwise raise exception
        try {
            // Load FXML file and get the controller for the stage
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("MapPane.fxml"));
            mapPane = (AnchorPane) loader.load();
            mapPaneController = loader.getController();

        } catch (IOException e) {
        }
    }

    /**
     * Initialize statistics pane with it's layout file
     */
    private void initializeStatisticsPane() {
        // Try loading FXML, otherwise raise exception
        try {
            // Load FXML file and get the controller for the stage
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("StatisticsPane.fxml"));
            statisticsPane = (AnchorPane) loader.load();
            statisticsPaneController = loader.getController();

            // Add reference to listings array for generating statistics
            statisticsPaneController.addListings(listings);
        } catch (IOException e) {
        }
    }

    /**
     * Initialize challenge pane with it's layout file
     */
    private void initializeChallengePane() {
        // Try loading FXML, otherwise raise exception
        try {
            // Load FXML file and get the controller for the stage
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("ChallengePane.fxml"));
            challengePane = (AnchorPane) loader.load();
            challengePaneController = loader.getController();

        } catch (IOException e) {
        }
    }

    /**
     * Initialize stack pane with all the panes in order and in correct relative positions
     */
    private void initializeStackPane() {
        // Add panes to stack pane in reverse order of priority
        stackPane.getChildren().add(challengePane);
        stackPane.getChildren().add(statisticsPane);
        stackPane.getChildren().add(mapPane);
        stackPane.getChildren().add(welcomePane);

        // Set pane visibility
        challengePane.setVisible(false);
        statisticsPane.setVisible(false);
        mapPane.setVisible(true);
        welcomePane.setVisible(true);
    }
}
