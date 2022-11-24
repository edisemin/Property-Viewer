import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeSet;
import java.util.HashMap;

import javafx.scene.layout.AnchorPane;

/**
 * The test class StatisticsPaneControllerTest.
 *
 * @author David Wang (k19005122), Savraj Bassi (k19029720), Edis Emin (k1921655), Sean McGrory (k1922054)
 * @version 1.6
 */
public class StatisticsPaneControllerTest
{

    // Hold controller for the tests
    private StatisticsPaneController statisticsPaneController;
    
    /**
     * Contructor for the test controller.
     */
    public StatisticsPaneControllerTest() {
        // Load FXML file so that initialization runs in statisticsPaneController
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("StatisticsPane.fxml"));
            loader.load();
            statisticsPaneController = loader.getController();
        } catch (IOException e) {
        }
    }

    /**
     * Test that listings are correctly added to the statistics controller.
    */
    @Test
    public void testListings() {
        AirbnbDataLoader dataLoader = new AirbnbDataLoader();
        ArrayList<AirbnbListing> listings = dataLoader.load();
        statisticsPaneController.addListings(listings);
        assertEquals(listings, statisticsPaneController.testListings());
    }
    
    /**
     * Test that minimum price is correctly updated.
     */
    @Test
    public void testMinPrice() {
        AirbnbDataLoader dataLoader = new AirbnbDataLoader();
        ArrayList<AirbnbListing> listings = dataLoader.load();
        statisticsPaneController.addListings(listings);
        statisticsPaneController.updateStatisticsValues(20, 1000);
        assertEquals(20, statisticsPaneController.testMinPrice());
    }
    
    /**
     * Test that maximum price is correctly updated.
     */
    @Test
    public void testMaxPrice() {
        AirbnbDataLoader dataLoader = new AirbnbDataLoader();
        ArrayList<AirbnbListing> listings = dataLoader.load();
        statisticsPaneController.addListings(listings);
        statisticsPaneController.updateStatisticsValues(20, 1000);
        assertEquals(1000, statisticsPaneController.testMaxPrice());
    }
    
    /**
     * Test initial statistics are set correctly.
     */
    @Test
    public void testStatsInitial() {
        ArrayList<Integer> initialStats = new ArrayList<Integer> (Arrays.asList(0, 1, 2, 3));
        assertEquals(initialStats, statisticsPaneController.testStatsInitial());
    }
    
    /**
     * Test that borough set is correctly calculated and ordered.
     */
    @Test
    public void testBoroughSet() {
        AirbnbDataLoader dataLoader = new AirbnbDataLoader();
        ArrayList<AirbnbListing> listings = dataLoader.load();
        TreeSet<String> boroughs = new TreeSet<String>();  
        boroughs.add("All");
        for (AirbnbListing listing : listings) {
            boroughs.add(listing.getNeighbourhood());
        }

        statisticsPaneController.addListings(listings);
        assertEquals(boroughs, statisticsPaneController.testBoroughSet());
    }

    /**
     * Test that borough statistics are correctly initialized.
     */
    @Test
    public void testBoroughStatistics() {
        AirbnbDataLoader dataLoader = new AirbnbDataLoader();
        ArrayList<AirbnbListing> listings = dataLoader.load();

        TreeSet<String> boroughs = new TreeSet<String>();  

        boroughs.add("All");

        for (AirbnbListing listing : listings) {
            boroughs.add(listing.getNeighbourhood());
        }

        HashMap<String, Integer[]> boroughStatistics = new HashMap<String, Integer[]>();
        
        for (String borough : boroughs) {
            boroughStatistics.put(borough, new Integer[8]);
        }

        statisticsPaneController.addListings(listings);

        HashMap<String, Integer[]> testBoroughStatistics =  statisticsPaneController.testBoroughStatistics();
        
        for (String borough : boroughs) {
            assertArrayEquals(boroughStatistics.get(borough), testBoroughStatistics.get(borough));
        }
    }

    /**
     * Test that host statistics are correctly initialized.
     */
    @Test
    public void testBoroughHostStatistics() {
        AirbnbDataLoader dataLoader = new AirbnbDataLoader();
        ArrayList<AirbnbListing> listings = dataLoader.load();

        TreeSet<String> boroughs = new TreeSet<String>();  
        
        boroughs.add("All");
        
        for (AirbnbListing listing : listings) {
            boroughs.add(listing.getNeighbourhood());
        }
        
        HashMap<String, HashMap<String, Integer>> boroughHostStatistics = new HashMap<String, HashMap<String, Integer>>();
        
        for (String borough : boroughs) {
            boroughHostStatistics.put(borough, new HashMap<String, Integer>());
        }

        statisticsPaneController.addListings(listings);
        assertEquals(boroughHostStatistics, statisticsPaneController.testBoroughHostStatistics());
    }

    /**
     * Test that initial statistics are correctly set.
     */
    @Test
    public void testStatisticsNames() {
        ArrayList<String> statNames = new ArrayList<String> (Arrays.asList( "Average number of reviews per property", "Total number of available properties","The number of entire home and apartments (as opposed to private rooms)", "The most expensive borough"));
        assertEquals(statNames, statisticsPaneController.testStatisticsNames());
    }
    
    /**
     * Test that initial statistic values are correctly set.
     */
    @Test
    public void testStatisticsValues() {
        ArrayList<String> statValues = new ArrayList<String> (Arrays.asList("0", "0", "0", "0"));
        assertEquals(statValues, statisticsPaneController.testStatisticsValues());
    }
}
