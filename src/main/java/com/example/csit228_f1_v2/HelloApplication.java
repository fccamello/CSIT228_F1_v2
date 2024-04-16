package com.example.csit228_f1_v2;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-view.fxml"));
//        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
//        stage.setTitle("Hello!");
//        stage.setScene(scene);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);


        Text txtExisting = new Text("Already Have An Account?");


        Text txtaddedUser = new Text("User added!");
        txtaddedUser.setVisible(false);

        Text txtWelcome = new Text("Register User");
        txtWelcome.setFont(Font.font("Chiller", FontWeight.EXTRA_BOLD, 69));
        txtWelcome.setFill(Color.RED);
//        grid.setAlignment();
        grid.setPadding(new Insets(20));
//        grid.
        txtWelcome.setTextAlignment(TextAlignment.CENTER);
        grid.add(txtWelcome, 0, 0, 3, 1);

        Label lbUsername = new Label("Username: ");
        lbUsername.setTextFill(Color.LIGHTSKYBLUE);
        lbUsername.setFont(Font.font(30));
        grid.add(lbUsername, 0, 1);

        TextField tfUsername = new TextField();
        grid.add(tfUsername, 1, 1);
        tfUsername.setFont(Font.font(30));
//        tfUsername.setMaxWidth(150);

        Label lbEmail = new Label("Email: ");
        lbEmail.setTextFill(Color.LIGHTSKYBLUE);
        lbEmail.setFont(Font.font(30));
        grid.add(lbEmail, 0, 2);

        TextField tfEmail = new TextField();
        grid.add(tfEmail, 1, 2);
        tfEmail.setFont(Font.font(30));


        Label lbPassword = new Label("Password");
        lbPassword.setFont(Font.font(30));
        lbPassword.setTextFill(Color.CHARTREUSE);
        grid.add(lbPassword, 0, 3);

        PasswordField pfPassword = new PasswordField();
        pfPassword.setFont(Font.font(30));
        grid.add(pfPassword, 1, 3);

        TextField tmpPassword = new TextField(pfPassword.getText());
        tmpPassword.setFont(Font.font(30));
        grid.add(tmpPassword, 1, 3);
        tmpPassword.setVisible(false);

        ToggleButton btnShow = new ToggleButton("( )");
//        btnShow.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent actionEvent) {
//                if (btnShow.isSelected()) {
//                    tmpPassword.setText(pfPassword.getText());
//                    tmpPassword.setVisible(true);
//                } else {
//                    tmpPassword.setVisible(false);
//                    pfPassword.setText(tmpPassword.getText());
//                }
//            }
//        });
        btnShow.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                tmpPassword.setText(pfPassword.getText());
                tmpPassword.setVisible(true);
            }
        });

        EventHandler<MouseEvent> release = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                tmpPassword.setVisible(false);
                pfPassword.setText(tmpPassword.getText());
            }
        };

        btnShow.setOnMouseReleased(release);
        btnShow.setOnMouseExited(release);
        grid.add(btnShow, 2,3);

        Button btnRegister = new Button("Register");
        btnRegister.setFont(Font.font(20));
        grid.add(btnRegister, 0, 4);

        Button btnLogin = new Button("Log In");
        btnLogin.setFont(Font.font(20));
        grid.add(txtExisting,0,5);

        grid.add(btnLogin, 0, 7);


        grid.add(txtaddedUser, 1, 4);


        btnLogin.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Parent p = null;
                try {
                    p = FXMLLoader.load(getClass().getResource("login-view.fxml"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Scene s = new Scene(p);
                    stage.setScene(s);
                    stage.show();
            }
        });

        btnRegister.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try (Connection c = MySQLConnection.getConnection();
                     PreparedStatement statement = c.prepareStatement(
                             "INSERT INTO user (name, email, password) VALUE (?,?,?)")
                )
                {
                    String name = tfUsername.getText();
                    String email = tfEmail.getText();
                    String password = pfPassword.getText();

                    statement.setString(1, name);
                    statement.setString(2, email);
                    statement.setString(3, password);
                    int rowsInserted = statement.executeUpdate();
                    System.out.println("Rows Inserted: " + rowsInserted);
                    txtaddedUser.setVisible(true);

//
                }
                catch (SQLException e){
                    throw new RuntimeException(e);
                }
//                try {
//                    System.out.println("Hello");
//                    Parent p = FXMLLoader.load(getClass().getResource("homepage.fxml"));
//                    Scene s = new Scene(p);
//                    stage.setScene(s);
//                    stage.show();
//                }
//                 catch (IOException e)
//                {
//                    e.printStackTrace();
//                }
            }
        });

        Scene scene = new Scene(grid, 700, 500, Color.BLACK);
        stage.setScene(scene);
        scene.setFill(Color.CORNFLOWERBLUE);
        stage.show();
        txtWelcome.minWidth(grid.getWidth());
    }

    public static void main(String[] args) {
        launch();
    }
}