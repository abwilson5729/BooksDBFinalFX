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

public class Controller {

    private static int recordNumber = 1;
    final static String DATABASE_URL = "jdbc:derby:C:\\\\Users\\\\hillt\\\\IdeaProjects\\\\BooksDBFinalFX\\\\lib\\\\books\"";

    @FXML
    private Label lblFirst;

    @FXML
    private Label lblLast;

    @FXML
    private TextField txtFirst;

    @FXML
    private TextField txtLast;

    @FXML
    private ColorPicker colorPicker;

    @FXML
    private GridPane backgroundPage;

    @FXML
    private ImageView hiddenImage;

    @FXML
    private Button  appearButton;

    @FXML
    void imageAppear(ActionEvent event){
        if (hiddenImage.getOpacity() == 0) {
            hiddenImage.setOpacity(1);
            appearButton.setText("Disappear");
        } else {
            hiddenImage.setOpacity(0.0);
            appearButton.setText("Appear");
        }
    }
    @FXML
    void changeBackground(ActionEvent event){
        Color color = colorPicker.getValue();
        backgroundPage.setStyle("-fx-background-color: " + String.format( "#%02X%02X%02X", (int)( color.getRed() * 255 ),
                (int)( color.getGreen() * 255 ), (int)( color.getBlue() * 255 )));
    }
    @FXML
    void createRecord(ActionEvent event) {
        String INSERT_QUERY = String.format("INSERT INTO AUTHORS(firstName, lastName)"
                + " VALUES ('%s', '%s')", txtFirst.getText(), txtLast.getText());
        // https://www.tutorialspoint.com/jdbc/preparestatement-object-example.htm
        connectAndExecute(INSERT_QUERY);
    }

    @FXML
    void deleteRecord(ActionEvent event) {
        String DELETE_QUERY = String.format("DELETE FROM AUTHORS WHERE FIRSTNAME = '%s' AND "
                + "LASTNAME = '%s'", lblFirst.getText(), lblLast.getText());
        connectAndExecute(DELETE_QUERY);
    }

    @FXML
    void nextRecord(ActionEvent event) {
        final String SELECT_QUERY =
                "SELECT authorID, firstName, lastName FROM authors";
        recordNumber++;

        // https://stackoverflow.com/questions/4085420/how-do-i-read-a-properties-file-and-connect-a-mysql-database
        Properties props = new Properties();
        http://tutorials.jenkov.com/java-exception-handling/try-with-resources.html
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
        }
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    @FXML
    void updateRecord(ActionEvent event) {
        String UPDATE_QUERY = String.format("UPDATE AUTHORS SET FIRSTNAME = '%s', LASTNAME = '%s' "
                        + "WHERE FIRSTNAME = '%s' AND LASTNAME = '%s'", txtFirst.getText(), txtLast.getText(),
                lblFirst.getText(), lblLast.getText());
        connectAndExecute(UPDATE_QUERY);
    }


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
            }
            // AutoCloseable objects' close methods are called now
            catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }

    }
}