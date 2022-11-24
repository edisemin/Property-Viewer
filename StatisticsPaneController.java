import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;
import javafx.scene.control.ComboBox;
import javafx.event.ActionEvent;
import java.util.Arrays;

/**
 * This class represents a series of statistics based on the data set provided. The structure of the panel is 
 * split into four distinct sections which enable the user to see four statistics on display and allow them 
 * to navigate through each one accordingly. The core functionalities and calculations for statistics are handled 
 * within this class as well as the relevant validation checks. 
 *
 * @author David Wang (k19005122), Savraj Bassi (k19029720), Edis Emin (k1921655), Sean McGrory (k1922054)
 * @version 1.6
 */
public class StatisticsPaneController {

    // ComboBox allows a user to select a borough of their choice
    @FXML
    private ComboBox boroughComboBox;

    // Label for the statistics name at index 0
    @FXML
    private Label stat0Name;

    // Label for the statistics value at index 0
    @FXML
    private Label stat0Value;

    // Left button for the statistics at index 0
    @FXML
    private Button stat0Left;

    // Right button for the statistics at index 0
    @FXML
    private Button stat0Right;

    // Label for the statistics name at index 1
    @FXML
    private Label stat1Name;

    // Label for the statistics value at index 1
    @FXML
    private Label stat1Value;

    // Left button for the statistics at index 1
    @FXML
    private Button stat1Left;

    // Right button for the statistics at index 1
    @FXML
    private Button stat1Right;

    // Label for the statistics name at index 2
    @FXML
    private Label stat2Name;

    // Label for the statistics value at index 2
    @FXML
    private Label stat2Value;

    // Left button for the statistics at index 2
    @FXML
    private Button stat2Left;

    // Right button for the statistics at index 2
    @FXML
    private Button stat2Right;

    // Label for the statistics name at index 3
    @FXML
    private Label stat3Name;

    // Label for the statistics value at index 3
    @FXML
    private Label stat3Value;

    // Left button for the statistics at index 3
    @FXML
    private Button stat3Left;

    // Right button for the statistics at index 3
    @FXML
    private Button stat3Right;

    // Variables to keep track of which statistics are currently being viewed
    private Integer stat0;
    private Integer stat1;
    private Integer stat2;
    private Integer stat3;

    // Reference to the array of listings
    private ArrayList<AirbnbListing> listings;

    // Minimum and maximum price to filter by
    private int minPrice;
    private int maxPrice;

    // Array to hold statistic names
    private String[] statNames = { "Average number of reviews per property", "Total number of available properties",
    "The number of entire home and apartments (as opposed to private rooms)", "The most expensive borough",
    "Average property price", "Least expensive private room", "Least expensive shared room", "Most popular host" };
    
    // Array to hold statistic values
    private String[] statValues = { "0", "0", "0", "0", "0", "0", "0", "0" };
    
    // Ordered set to keep track of boroughs
    private TreeSet<String> boroughs;

    // Currently selected borough
    private String currentBorough;

    // Statistics for each borough
    HashMap<String, Integer[]> boroughStatistics;
    // Statistics for each host in each borough
    HashMap<String, HashMap<String, Integer>> boroughHostStatistics;

    /**
     * Initialize this pane with some initial statistics.
     */
    @FXML
    private void initialize() {
        // Set statistic button actions
        setButtons();

        // Set the four statistic views to the first four statistics
        stat0 = 0;
        stat1 = 1;
        stat2 = 2;
        stat3 = 3;
    }

    /** 
     * Add the listings array reference.
     * @param listings Array of listings to generate stats from.
     */
    public void addListings(ArrayList<AirbnbListing> listings) {
        // Set a reference to the array
        this.listings = listings;

        // Create an empty set of boroughs
        boroughs = new TreeSet<String>();
        
        // Add an "All" borough to represented no specific selection
        boroughs.add("All");

        // Iterate through all listings
        for (AirbnbListing listing : listings) {
            // Add their borough to the set of boroughs
            boroughs.add(listing.getNeighbourhood());
        }

        // Add boroughs to the borough selection combo box
        boroughComboBox.getItems().addAll(boroughs);

        // Set default value of borough selection to be "All"
        boroughComboBox.setValue("All");
        currentBorough = "All";

        // Create statistics array for each borough
        boroughStatistics = new HashMap<String, Integer[]>();

        // Create empty statistics for each borough in the set
        for (String borough : boroughs) {
            boroughStatistics.put(borough, new Integer[8]);
        }

        // Create host map for each borough
        boroughHostStatistics = new HashMap<String, HashMap<String, Integer>>();

        // Create empty statistics for each borough in the set
        for (String borough : boroughs) {
            boroughHostStatistics.put(borough, new HashMap<String, Integer>());
        }
    }

    /**
     * Action if borough selection has been changed.
     */
    @FXML
    private void boroughModified(ActionEvent event) {
        // Get selected borough
        currentBorough = (String) boroughComboBox.getValue();
        
        // Calculate and update statistics
        calculateStatisticsValues();
        updateStatisticsText();
    }

    /**
     * Set button actions for each statistics left and right button.
     */
    private void setButtons() {
        // Set first statistic left button action
        stat0Left.setOnAction(event -> {
            // Decrease currently selected statistic and wrap around if negative
            stat0 = (stat0 + 7) % 8;
            // While new statistic is already in the list
            while (stat0 == stat1 || stat0 == stat2 || stat0 == stat3) {
                // Move to previous statistic
                stat0 = (stat0 + 7) % 8;
            }
            // Once valid statistic has been selected, update statistics text
            updateStatisticsText();
        });
        
        // Set first statistic right button action
        stat0Right.setOnAction(event -> {
            // Increase to next statistic and wrap around if greater than number of statistics
            stat0 = (stat0 + 1) % 8;
            // While new statistic is already in the list
            while (stat0 == stat1 || stat0 == stat2 || stat0 == stat3) {
                // Move to next statistic
                stat0 = (stat0 + 1) % 8;
            }
            // Once valid statistic has been selected, update statistics text
            updateStatisticsText();
        });
        
        // Set second statistic left button action
        stat1Left.setOnAction(event -> {
            stat1 = (stat1 + 7) % 8;
            while (stat1 == stat0 || stat1 == stat2 || stat1 == stat3) {
                stat1 = (stat1 + 7) % 8;
            }
            updateStatisticsText();
        });

        // Set second statistic right button action
        stat1Right.setOnAction(event -> {
            stat1 = (stat1 + 1) % 8;
            while (stat1 == stat0 || stat1 == stat2 || stat1 == stat3) {
                stat1 = (stat1 + 1) % 8;
            }
            updateStatisticsText();
        });

        // Set third statistic left button action
        stat2Left.setOnAction(event -> {
            stat2 = (stat2 + 7) % 8;
            while (stat2 == stat0 || stat2 == stat1 || stat2 == stat3) {
                stat2 = (stat2 + 7) % 8;
            }
            updateStatisticsText();
        });

        // Set third statistic right button action
        stat2Right.setOnAction(event -> {
            stat2 = (stat2 + 1) % 8;
            while (stat2 == stat0 || stat2 == stat1 || stat2 == stat3) {
                stat2 = (stat2 + 1) % 8;
            }
            updateStatisticsText();
        });

        // Set fourth statistic left button action
        stat3Left.setOnAction(event -> {
            stat3 = (stat3 + 7) % 8;
            while (stat3 == stat0 || stat3 == stat1 || stat3 == stat2) {
                stat3 = (stat3 + 7) % 8;
            }
            updateStatisticsText();
        });

        // Set fourth statistic right button action
        stat3Right.setOnAction(event -> {
            stat3 = (stat3 + 1) % 8;
            while (stat3 == stat0 || stat3 == stat1 || stat3 == stat2) {
                stat3 = (stat3 + 1) % 8;
            }
            updateStatisticsText();
        });
    }

    /**
     * Update statistics values based on given min and max price.
     * @param minPrice The minimum price for the price range.
     * @param maxPrice The maximum price for the price range.
     */
    public void updateStatisticsValues(int minPrice, int maxPrice) {
        // Set min and max price for statistics
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;

        // Calculate statistics values
        calculateStatisticsValues();
        // Update statistics text
        updateStatisticsText();
    }

    /**
     * Update text of statistic labels.
     */
    private void updateStatisticsText() {
        // Update statistic names
        stat0Name.setText(statNames[stat0]);
        stat1Name.setText(statNames[stat1]);
        stat2Name.setText(statNames[stat2]);
        stat3Name.setText(statNames[stat3]);

        // Update statistic values
        stat0Value.setText(statValues[stat0]);
        stat1Value.setText(statValues[stat1]);
        stat2Value.setText(statValues[stat2]);
        stat3Value.setText(statValues[stat3]);
    }

    /**
     * Calculate each of the statistics for each borough.
     */
    private void calculateStatisticsValues() {
        // Set statistics for each borough to defaults to begin with.
        for (String borough : boroughs) {
            // Get statistics array
            Integer[] stats = boroughStatistics.get(borough);
            // Set to default values
            Arrays.fill(stats, Integer.valueOf(0));
            stats[5] = Integer.MAX_VALUE;
            stats[6] = Integer.MAX_VALUE;

            // Clear host popularity in this borough
            boroughHostStatistics.get(borough).clear();
        }

        // Iterate through all the listings
        for (AirbnbListing listing : listings) {
            // Get the minimum price of listing as the price per night multiplied by the minimum number of nights
            Integer listingPrice = listing.getPrice() * listing.getMinimumNights();

            // Check if listing price lies within the price range
            if (minPrice <= listingPrice && listingPrice < maxPrice) {
                 // Get reference to all borough statistics
                Integer[] statsAll = boroughStatistics.get("All");

                // Get the borough of this listing
                String listingBorough = listing.getNeighbourhood();
                // Get reference to the statistics for this listing's borough
                Integer[] statsBorough = boroughStatistics.get(listingBorough);

                // Get the number of reviews for this listing
                Integer listingReviews = listing.getNumberOfReviews();
                
                // Add this listing's reviews to the total reviews for all boroughs
                statsAll[0] += listingReviews;
                // Add this listing's reviews to the total reviews for this borough
                statsBorough[0] += listingReviews;

                // Increment total number of properties for all boroughs
                statsAll[1] += 1;
                // Increment total number of properties for this borough
                statsBorough[1] += 1;

                // Get the room type for this listing
                String listingRoom = listing.getRoom_type();
                // Check if listing room is an entire home or apartment
                if (listingRoom.equals("Entire home/apt")) {
                    // Increment total number of entire homes/apartments for all boroughs
                    statsAll[2] += 1;
                    // Increment total number of entire homes/apartments for this borough
                    statsBorough[2] += 1;
                }
                // Check if listing room is a private room
                else if (listingRoom.equals("Private room")) {
                    // If listing price is less than current lowest private room cost for all boroughs
                    if (listingPrice < statsAll[5]) {
                        // Update lowest cost private room
                        statsAll[5] = listingPrice;
                    }
                    // If listing price is less than current lowest private room cost for this borough
                    if (listingPrice < statsBorough[5]) {
                        // Update lowest cost private room
                        statsBorough[5] = listingPrice;
                    }
                }
                // Check if listing room is a shared room
                else if (listingRoom.equals("Shared room")) {
                    // If listing price is less than current lowest shared room cost for all boroughs
                    if (listingPrice < statsAll[6]) {
                        // Update lowest cost shared room
                        statsAll[6] = listingPrice;
                    }
                    // If listing price is less than current lowest shared room cost for this borough
                    if (listingPrice < statsBorough[6]) {
                        // Update lowest cost shared room
                        statsBorough[6] = listingPrice;
                    }
                }

                // Add listing price to total prices for all boroughs
                statsAll[3] += listingPrice;
                // Add listing price to total prices for this borough
                statsBorough[3] += listingPrice;

                // Get host ID of this listing
                String listingHostID = listing.getHost_id();

                // Get the hosts for all boroughs
                HashMap<String, Integer> hostStatsAll = boroughHostStatistics.get("All");
                // Increase hosts reviews across all boroughs
                hostStatsAll.put(listingHostID, hostStatsAll.getOrDefault(listingHostID, 0) + listingReviews);
                
                // Get the hosts for this borough
                HashMap<String, Integer> hostStatsBorough = boroughHostStatistics.get(listingBorough);
                // Increase hosts reviews for this borough
                hostStatsBorough.put(listingHostID, hostStatsBorough.getOrDefault(listingHostID, 0) + listingReviews);
            }
        }

        // Set most expensive price to default
        Integer mostExpensivePrice = 0;
        // Set most expensive borough to default
        String mostExpensiveBorough = "All";
        
        // Iterate through all boroughs
        for (String borough : boroughs) {
            // Get this boroughs statistics
            Integer[] statistics = boroughStatistics.get(borough);
            // Get specific values from statistics
            Integer totalReviews = statistics[0];
            Integer totalProperties = statistics[1];
            Integer totalPrice = statistics[3];

            // Check if there are any properties in this borough (for this price range)
            if (totalProperties > 0) {
                // Calculate average reviews
                statistics[0] = totalReviews / totalProperties;
                // Calculate average price
                statistics[3] = totalPrice / totalProperties;
                
                // If average price for this borough is greater than the most expensive borough price
                if (statistics[3] > mostExpensivePrice) {
                    // Replace most expensive price
                    mostExpensivePrice = statistics[3];
                    // Update most expensive borough to this one
                    mostExpensiveBorough = borough;
                }
            }
            
            // Set highest reviews to start at minimum
            Integer highestReviews = 0;
            // Set popular host to be default
            String highestReviewsHost = "0";
            
            // Get this boroughs host popularity
            HashMap<String, Integer> hostStatsBorough = boroughHostStatistics.get(borough);

            // Go through all hosts
            for (String host : hostStatsBorough.keySet()) {
                // If host is more popular than current most popular
                if (hostStatsBorough.get(host) > highestReviews) {
                    // Update highest reviews
                    highestReviews = hostStatsBorough.get(host);
                    // Update most popular host
                    highestReviewsHost = host;
                }
            }

            // Save most popular host ID as a number
            statistics[7] = Integer.parseInt(highestReviewsHost);
        }

        // Get the statistics for the currently selected borough
        Integer[] currentStatistics = boroughStatistics.get(currentBorough);

        // Average reviews
        String averageReviews = currentStatistics[0].toString();
        // Total properties
        String totalProperties = currentStatistics[1].toString();
        // Total homes/apartments
        String totalHomes = currentStatistics[2].toString();
        // Most expensive borough
        String mostExpensive = mostExpensiveBorough;
        // Average cost
        String averageCost = currentStatistics[3].toString();
        // Minimum cost of private room
        String minimumPrivate = currentStatistics[5].toString();
        // Minimum cost of shared room
        String minimumShared = currentStatistics[6].toString();
        // Most popular host
        String popularHost = currentStatistics[7].toString();
        
        // If minimum value is still max
        if (minimumPrivate.equals("2147483647")) {
            // Give error message
            minimumPrivate = "No available private rooms";
        }
        
        // If minimum value is still max
        if (minimumShared.equals("2147483647")) {
            // Give error message
            minimumShared = "No available shared rooms";
        }
        
        // Add pound symbol for cost
        averageCost = "£" + averageCost;

        // If no properties available
        if (totalProperties.equals("0")) {
            // Give error messages
            popularHost = "No hosts available";
            averageReviews = "No reviews available";
            averageCost = "No costs available";
            totalHomes = "No homes available";
        }
        
        // Update statistic values for display
        statValues[0] = averageReviews;
        statValues[1] = totalProperties;
        statValues[2] = totalHomes;
        statValues[3] = mostExpensive;
        statValues[4] = averageCost;
        statValues[5] = minimumPrivate;
        statValues[6] = minimumShared;
        statValues[7] = popularHost;
    }

    public ArrayList<AirbnbListing> testListings() {
        return listings;
    }

    public int testMinPrice() {
        return minPrice;
    }

    public int testMaxPrice() {
        return maxPrice;
    }

    public ArrayList<Integer> testStatsInitial() {
        return new ArrayList<Integer> (Arrays.asList(stat0, stat1, stat2, stat3));
    }

    public TreeSet<String> testBoroughSet() {
        return boroughs;
    }

    public HashMap<String, Integer[]> testBoroughStatistics() {
        return boroughStatistics;
    }

    public HashMap<String, HashMap<String, Integer>> testBoroughHostStatistics() {
        return boroughHostStatistics;
    }

    public ArrayList<String> testStatisticsNames() {
        return new ArrayList<String> (Arrays.asList(statNames[stat0], statNames[stat1], statNames[stat2], statNames[stat3]));
    }

    public ArrayList<String> testStatisticsValues() {
        return new ArrayList<String> (Arrays.asList(statValues[stat0],statValues[stat1],statValues[stat2],statValues[stat3]));
    }

}
