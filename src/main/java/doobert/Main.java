package doobert;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * A GUI for Doobert using FXML.
 */
public class Main extends Application {

    private Doobert doobert = new Doobert("./data/doobert.txt");

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            fxmlLoader.<MainWindow>getController().setDoobert(doobert);  // inject the Doobert instance
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

