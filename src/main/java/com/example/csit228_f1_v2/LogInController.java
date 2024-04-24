package com.example.csit228_f1_v2;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LogInController {

    public TextField lgpass;
    public TextField lguname;
    public AnchorPane pnMain;

    static String loggedUserName;
    static String loggedFname;
    static String loggedLname;
    static String loggedPassword;
    public Button btnReg;

    public void login() {
        String uname = lguname.getText();
        String pass = lgpass.getText();

        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement statement = c.prepareStatement("SELECT * FROM tblLibrarian WHERE username = ? AND password = ?")) {
            statement.setString(1, uname);
            statement.setString(2, pass);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("Login successful");

                    loggedUserName = resultSet.getString("username");
                    loggedFname = resultSet.getString("firstname");
                    loggedLname = resultSet.getString("lastname");
                    loggedPassword = resultSet.getString("password");

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("accset-page.fxml"));
                    Parent root = loader.load();
                    AccountSettingsController controller = loader.getController();
                    controller.printInfo(loggedFname, loggedLname, loggedUserName, loggedPassword);

                    AnchorPane x = pnMain;
                    x.getChildren().clear();
                    x.getChildren().add(root);
                } else {
                    System.out.println("Login failed");
                    showAlert("Invalid Username or Password!");

                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("LOGIN ERROR");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void reg(ActionEvent actionEvent) throws IOException {
        AnchorPane x = pnMain;
        Parent p = FXMLLoader.load(getClass().getResource("register-view.fxml"));
        x.getChildren().clear();
        x.getChildren().add(p);
    }
}