package Twenty48;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Twenty48App extends Application {

    public static void main(String[] args) {
        Twenty48App.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("2048");
        primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("Twenty48StartPage.fxml"))));
        primaryStage.show();
    }

    

}
