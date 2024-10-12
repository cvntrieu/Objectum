package lma.objectum.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

class hello {

}

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}