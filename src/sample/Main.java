package sample;

import javafx.animation.FillTransition;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

  //Define the UI window for this program.
  @Override
  public void start(Stage primaryStage) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
    primaryStage.setTitle("Database Project");
    Scene scene = new Scene(root, 600, 500);
    primaryStage.setScene(scene);
    root.getStylesheets().add("myStyle.css");
    scene.getStylesheets().add("myStyle.css");
    primaryStage.show();
  }

  //Main method which launches the program.
  public static void main(String[] args) {
    launch(args);
  }
}