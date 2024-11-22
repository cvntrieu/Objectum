package lma.objectum;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    /**
     * Getting started.
     *
     * @param stage first stage
     * @throws IOException exception handling
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("fxml/App.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 850, 650);
        stage.setTitle("Objectum Library");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Main methods.
     *
     * @param args arguments.
     */
    public static void main(String[] args) {
        launch();
    }
}