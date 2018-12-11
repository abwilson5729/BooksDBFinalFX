package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

  //Define the UI window for this program.
  @Override
  public void start(Stage primaryStage) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
    primaryStage.setTitle("Database Project");
    primaryStage.setScene(new Scene(root, 600, 250));
    primaryStage.show();
  }

  //Main method which launches the program.
  public static void main(String[] args) {
    launch(args);
  }
}