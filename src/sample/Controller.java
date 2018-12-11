package sample;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javafx.animation.FillTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;
import javafx.util.Duration;
import javax.swing.Action;

public class Controller {

  //Variable which records the number of records in the database.
  private static int recordNumber = 1;

  //location of the database file
  final static String DATABASE_URL
      = "jdbc:derby:C:\\\\Users\\\\hillt\\\\IdeaProjects\\\\BooksDBFinalFX\\\\lib\\\\books\"";

  //Label for the first name listed in the data base.
  @FXML
  private Label lblFirst;

  //Label for the last name listed in the data base.
  @FXML
  private Label lblLast;

  //Text field which allows users to enter a first name.
  @FXML
  private TextField txtFirst;

  //Text field which allows users to enter a last name.
  @FXML
  private TextField txtLast;

  //Color picker object which sets the background color.
  @FXML
  private ColorPicker colorPicker;

  //Gridpane object which acts as the background of the window.
  @FXML
  private GridPane backgroundPage;

  //Image which is hidden to start and can be revealed by clicking the appear button.
  @FXML
  private ImageView hiddenImage;

  //Button which makes the hiddenImage appear or disappear,
  // the text of this button changes accordingly.
  @FXML
  private Button appearButton;

  @FXML
  private Shape circle;

  //When clicked the appear button set the hiddenImage opacity to 1
  // and sets the text of the appear button to say disappear.
  // Clicking disappear will set the text back to appear and the opacity back to 0.
  @FXML
  void imageAppear(ActionEvent event) {
    if (hiddenImage.getOpacity() == 0) {
      hiddenImage.setOpacity(1);
      appearButton.setText("Disappear");
    } else {
      hiddenImage.setOpacity(0.0);
      appearButton.setText("Appear");
    }
  }

  //This takes the color given by the colorPicker and set the background color to that color.
  @FXML
  void changeBackground(ActionEvent event) {
    Color color = colorPicker.getValue();
    backgroundPage.setStyle(
        "-fx-background-color: " + String.format("#%02X%02X%02X", (int) (color.getRed() * 255),
            (int) (color.getGreen() * 255), (int) (color.getBlue() * 255)));
  }

  //This creates a new record when the corresponding button is pressed.
  @FXML
  void createRecord(ActionEvent event) {
    String INSERT_QUERY = String.format("INSERT INTO AUTHORS(firstName, lastName)"
        + " VALUES ('%s', '%s')", txtFirst.getText(), txtLast.getText());
    // https://www.tutorialspoint.com/jdbc/preparestatement-object-example.htm
    connectAndExecute(INSERT_QUERY);
  }

  //This deletes a record when the corresponding button is pressed.
  @FXML
  void deleteRecord(ActionEvent event) {
    String DELETE_QUERY = String.format("DELETE FROM AUTHORS WHERE FIRSTNAME = '%s' AND "
        + "LASTNAME = '%s'", lblFirst.getText(), lblLast.getText());
    connectAndExecute(DELETE_QUERY);
  }

  //This sets the text labels to the next record in the directory when the corresponding button
  // is pressed.
  @FXML
  void nextRecord(ActionEvent event) {
    final String SELECT_QUERY =
        "SELECT authorID, firstName, lastName FROM authors";
    recordNumber++;

    // https://stackoverflow.com/questions/4085420/how-do-i-read-a-properties-file-and-connect-a-mysql-database
    Properties props = new Properties();
    http:
    //tutorials.jenkov.com/java-exception-handling/try-with-resources.html
    try (FileInputStream in = new FileInputStream("dir/db.properties")) {
      props.load(in);
    } catch (FileNotFoundException fileExcept) {
      fileExcept.printStackTrace();
    } catch (IOException ioexcept) {
      ioexcept.printStackTrace();
    }

    String username = props.getProperty("jdbc.username");
    String password = props.getProperty("jdbc.password");

    try (
        Connection connection = DriverManager.getConnection(DATABASE_URL, username, password);
        Statement statement = connection
            .createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

        ResultSet resultSet = statement.executeQuery(SELECT_QUERY)) {
      if (resultSet.next()) {
        resultSet.absolute(recordNumber);
      }

      if (resultSet.next()) {
        lblFirst.setText(resultSet.getString(2));
        lblLast.setText(resultSet.getString(3));
      } else {
        resultSet.first();
        recordNumber = 1;
        lblFirst.setText(resultSet.getString(2));
        lblLast.setText(resultSet.getString(3));
      }
    } catch (SQLException sqlException) {
      sqlException.printStackTrace();
    }
  }

  //this updates the displayed records when the corresponding button is pressed.
  @FXML
  void updateRecord(ActionEvent event) {
    String UPDATE_QUERY = String.format("UPDATE AUTHORS SET FIRSTNAME = '%s', LASTNAME = '%s' "
            + "WHERE FIRSTNAME = '%s' AND LASTNAME = '%s'", txtFirst.getText(), txtLast.getText(),
        lblFirst.getText(), lblLast.getText());
    connectAndExecute(UPDATE_QUERY);
  }

  //this connects to the database and executes the corresponding functions
  void connectAndExecute(String query) {
    Properties props = new Properties();
    try (FileInputStream in = new FileInputStream("dir/db.properties")) {
      props.load(in);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    String username = props.getProperty("jdbc.username");
    String password = props.getProperty("jdbc.password");

    try (Connection connection = DriverManager.getConnection(DATABASE_URL, username, password);
        Statement statement = connection.createStatement();) {
      try {
        statement.execute(query);
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    } catch (SQLException sqlExcept) {
      sqlExcept.printStackTrace();
    }

  }

  public void animateCircle() {
    FillTransition ft = new FillTransition(Duration.millis(3000), circle, Color.RED, Color.BLUE);
    ft.setCycleCount(Timeline.INDEFINITE);
    ft.play();
  }
}