import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * Write a description of JavaFX class WelcomePanel here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class MapPaneController
{
    @FXML
    private Label minimumPrice;

    @FXML
    private Label maximumPrice;

    @FXML
    private HBox priceRange;

    @FXML
    private void initialize() {
        priceRange.setVisible(false);
    }
    
    public void updatePriceLabel(String minPrice, String maxPrice)
    {
        minimumPrice.setText(minPrice);
        maximumPrice.setText(maxPrice);
        priceRange.setVisible(true);

    }

}
