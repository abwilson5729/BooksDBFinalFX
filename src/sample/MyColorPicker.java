package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MyColorPicker extends Application {

  //test file for how the Color Picker works. This file is not used in the final product.
  @Override
  public void start(Stage primaryStage) {
    VBox box = new VBox();
    ColorPicker cp = new ColorPicker();
    box.getChildren().add(cp); // Error: cp Cannot be converted to Node
    Scene scene = new Scene(box, 300, 200);

    primaryStage.setTitle("colorPicker");
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}