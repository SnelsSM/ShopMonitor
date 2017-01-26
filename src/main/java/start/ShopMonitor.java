package start;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import threads.SQLThread;

public class ShopMonitor extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/views/MainWindow.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.setMinHeight(500);
        primaryStage.setMinWidth(700);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
