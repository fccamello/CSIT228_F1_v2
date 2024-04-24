package com.example.csit228_f1_v2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.example.csit228_f1_v2.LogInController.loggedUserName;

public class AccountSettingsController {

    public Label lblFirstName;
    public Label lblUsername;
    public Label lblLastName;
    public Label lblPassword;
    public Label lblflName;
    public AnchorPane pnMain;

    public void printInfo(String firstName, String lastName, String username, String password) {
        lblflName.setText(firstName + " " + lastName + " ! ");
        lblFirstName.setText(firstName);
        lblLastName.setText(lastName);
        lblUsername.setText(username);
        lblPassword.setText(password);
    }


    @FXML
    public void updateAccount() {
        TextField fn = new TextField(lblFirstName.getText());
        TextField ln = new TextField(lblLastName.getText());
        TextField un = new TextField(lblUsername.getText());
        TextField pass = new TextField(lblPassword.getText());

        GridPane gridPane = new GridPane();
        gridPane.addRow(0, new Label("First Name:"), fn);
        gridPane.addRow(1, new Label("Last Name:"), ln);
        gridPane.addRow(2, new Label("Username:"), un);
        gridPane.addRow(3, new Label("Password:"), pass);
        gridPane.setVgap(10);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Update Account");
        alert.getDialogPane().setContent(gridPane);

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                String updatedUsername = un.getText();
                if (!updatedUsername.equals(lblUsername.getText()) && usernameExists(updatedUsername)) {
                    showAlert("Username already exists.");
                    return;
                }

                lblflName.setText(fn.getText() + " " + ln.getText() + " ! ");
                lblUsername.setText(updatedUsername);
                lblPassword.setText(pass.getText());

                try (Connection connection = MySQLConnection.getConnection()) {
                    connection.setAutoCommit(false);
                    try (PreparedStatement statement = connection.prepareStatement("UPDATE tblLibrarian SET firstname = ?, lastname = ?, username = ?, password = ?")) {

                        statement.setString(1, fn.getText());
                        statement.setString(2, ln.getText());
                        statement.setString(3, updatedUsername);
                        statement.setString(4, pass.getText());

                        statement.executeUpdate();
                        System.out.println("Updated na");
                        connection.commit();
                    } catch (SQLException e) {
                        connection.rollback();
                        e.printStackTrace();
                    } finally {
                        connection.setAutoCommit(true);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void deleteAccount() {
        try (Connection connection = MySQLConnection.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement("DELETE FROM tblLibrarian WHERE username = ?")) {

                String todelete = lblUsername.getText();
                statement.setString(1, todelete);

                statement.executeUpdate();
                System.out.println("Deleted " + todelete);
                connection.commit();

                AnchorPane x = pnMain;
                Parent p = FXMLLoader.load(getClass().getResource("login-view.fxml"));
                x.getChildren().clear();
                x.getChildren().add(p);

            } catch (SQLException | IOException e) {
                connection.rollback();
                e.printStackTrace();
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void listBook() throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("listbook-page.fxml"));
        Parent root = loader.load();
        ListbookPage controller2 = loader.getController();
        controller2.namelistBook(loggedUserName);

        AnchorPane x = pnMain;
        x.getChildren().clear();
        x.getChildren().add(root);
    }


    private boolean usernameExists(String username) {
        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM tblLibrarian WHERE username = ?")) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);
            return count > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText(message);
        alert.showAndWait();
    }


}