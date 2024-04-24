package com.example.csit228_f1_v2;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegisterController {

    public TextField tfFirstname;
    public TextField tfLastname;
    public TextField tfUsername;
    public PasswordField tfPass;
    public Button btnSignIn;
    public Button btnRegister;
    public Label lblAdded;
    public TextField lgpass;
    public AnchorPane pnMain;

    public void insert(){
        try(Connection c = MySQLConnection.getConnection();
            PreparedStatement checkStatement = c.prepareStatement("SELECT COUNT(*) FROM tblLibrarian WHERE username = ?");
            PreparedStatement insertStatement = c.prepareStatement("INSERT INTO tblLibrarian (firstname, lastname, username, password) VALUES (?,?,?,?)"))
        {
            String fname = tfFirstname.getText();
            String lname = tfLastname.getText();
            String uname = tfUsername.getText();
            String pass = tfPass.getText();

            if (fname.isEmpty() || lname.isEmpty() || uname.isEmpty() || pass.isEmpty()) {
                showAlert("Please fill in all the fields.");
                return;
            }

            checkStatement.setString(1, uname);
            ResultSet resultSet = checkStatement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);
            if(count > 0) {
                System.out.println("Username existing na!");
                showAlert("Username already exists");
                return;
            }

            insertStatement.setString(1, fname);
            insertStatement.setString(2, lname);
            insertStatement.setString(3, uname);
            insertStatement.setString(4, pass);

            int rowsInserted = insertStatement.executeUpdate();
            System.out.println("Rows Inserted: " + rowsInserted);
            System.out.println("Username existing na!");

            showAlertSuccess("Registration Successful! \nHello Librarian " + uname + " ! ");
            btnSignOnClick();

        }
        catch (SQLException e){
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void btnSignOnClick() throws IOException {
        AnchorPane x = pnMain;
        Parent p = FXMLLoader.load(getClass().getResource("login-view.fxml"));
        x.getChildren().clear();
        x.getChildren().add(p);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Username Already Exists!");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showAlertSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Register Succcess!");
        alert.setContentText(message);
        alert.showAndWait();
    }



}
