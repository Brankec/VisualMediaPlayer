import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws IOException {
        Globals.primaryStage = primaryStage;
        Globals.primaryStage.setTitle("VMP");
        Globals.primaryStage.setResizable(false);
        Parent fxml = FXMLLoader.load(getClass().getResource("FXML/Menu.fxml"));

        Scene scene = new Scene(fxml, 1920/2, 1080/1.8);
        scene.getStylesheets().addAll("CSS/Menu.css");
        Globals.primaryStage.setScene(scene);
        Globals.primaryStage.show();

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });
    }
}
