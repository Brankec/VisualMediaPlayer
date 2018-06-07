import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws IOException {
        Globals.primaryStage = primaryStage;
        Globals.primaryStage.setTitle("VMP");
        //Globals.primaryStage.setResizable(false);
        Parent fxml = FXMLLoader.load(getClass().getResource("FXML/Menu.fxml"));

        Globals.primaryStage.setScene(new Scene(fxml, 1920/2, 1080/2));
        Globals.primaryStage.show();
    }
}
