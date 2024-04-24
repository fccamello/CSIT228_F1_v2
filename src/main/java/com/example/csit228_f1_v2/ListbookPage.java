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
import java.time.LocalDate;

public class ListbookPage {

    public Label librarianUn;
    public TextField tftitle;
    public TextField tfauthor;
    public TextField tfyrpub;
    public TextField tfdesc;
    public Button btnListBook;
    public Label hideLibId;
    public DatePicker dpyear;
    public Button btnViewList;
//    public ListView<String> bookListView;
//    public TableView bookTableView;
//    public TableColumn libcol;
//    public TableColumn titlecol;
//    public TableColumn authorcol;
//    public TableColumn yrcol;
//    public TableColumn desccol;
    public GridPane bookListGrid;
    public Button btnLogout;
    public AnchorPane pnMain;





    public void initialize() {
        displayBookList(hideLibId.getText());
        System.out.println(hideLibId.getText() + "mao ni i check");
    }

    public void namelistBook(String username) {
        hideLibId.setText(username);
        librarianUn.setText("Hello, " + username + "! Listing a book? ");
        initialize();
    }

    public int foreignLibID(String username) throws SQLException {
        int librarianId = -1;

        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT libid FROM tbllibrarian WHERE username = ?")) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                librarianId = resultSet.getInt("libid");
            }
        }

        return librarianId;
    }

    public void btnListBookOnClick() throws SQLException {
        int paraLibId = foreignLibID(hideLibId.getText());
        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement statement = c.prepareStatement("INSERT INTO tblbooks (title, author, date_pub, description, libid) VALUES (?,?,?,?,?)")) {
            String title = tftitle.getText();
            String author = tfauthor.getText();
            String year = dpyear.getValue().toString();
            String desc = tfdesc.getText();


            statement.setString(1, title);
            statement.setString(2, author);
            statement.setString(3, year);
            statement.setString(4, desc);
            statement.setInt(5, paraLibId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        displayBookList(hideLibId.getText());
    }

    public void displayBookList(String currentUsername) {
        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT title, author, date_pub, description, libid FROM tblbooks")) {
            ResultSet resultSet = statement.executeQuery();
            bookListGrid.getChildren().clear();

            Label librarianLabel = new Label("Librarian");
            Label titleLabel = new Label("Title");
            Label authorLabel = new Label("Author");
            Label yearLabel = new Label("Date Published");
            Label descriptionLabel = new Label("Description");
            bookListGrid.add(librarianLabel, 0, 0);
            bookListGrid.add(titleLabel, 1, 0);
            bookListGrid.add(authorLabel, 2, 0);
            bookListGrid.add(yearLabel, 3, 0);
            bookListGrid.add(descriptionLabel, 4, 0);
            bookListGrid.add(new Label("Actions"), 5, 0);

            int row = 1;
            while (resultSet.next()) {
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                String year = resultSet.getString("date_pub");
                String description = resultSet.getString("description");
                int libId = resultSet.getInt("libid");

                String libun = getlibname(libId);

                Label librarianValueLabel = new Label(libun);
                Label titleValueLabel = new Label(title);
                Label authorValueLabel = new Label(author);
                Label yearValueLabel = new Label(year);
                Label descriptionValueLabel = new Label(description);
                bookListGrid.add(librarianValueLabel, 0, row);
                bookListGrid.add(titleValueLabel, 1, row);
                bookListGrid.add(authorValueLabel, 2, row);
                bookListGrid.add(yearValueLabel, 3, row);
                bookListGrid.add(descriptionValueLabel, 4, row);

                Button deleteButton = new Button("Delete");
                deleteButton.setOnAction(event -> {
                    try {
                        String bookLibrarianUsername = getlibname(libId);
                        if (currentUsername.equals(bookLibrarianUsername)) {
                            deleteBook(title);
                            displayBookList(currentUsername);
                        } else {

                            System.out.println(currentUsername);
                            showAlert("You are not the Librarian. You cannot delete this book!");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });

                Button updateButton = new Button("Update");
                updateButton.setOnAction(event -> {
                    try {
                        String bookLibrarianUsername = getlibname(libId);
                        if (currentUsername.equals(bookLibrarianUsername)) {
                            updateBook(title, author, year, description);
                        } else {
                            System.out.println(currentUsername);
                            showAlert("You are not the Librarian. You cannot update this book!");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });

                bookListGrid.add(deleteButton, 5, row);
                bookListGrid.add(updateButton, 6, row);

                row++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    private String getlibname(int libId) throws SQLException {
        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT username FROM tbllibrarian WHERE libid = ?")) {
            statement.setInt(1, libId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next())
            {
                return resultSet.getString("username");
            }
        }
        return "";
    }


    public void updateBook(String title, String author, String year, String description) throws SQLException {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        TextField titleTextField = new TextField(title);
        TextField authorTextField = new TextField(author);
        DatePicker yearDatePicker = new DatePicker(LocalDate.parse(year));
        TextField descriptionTextField = new TextField(description);

        gridPane.add(new Label("Title:"), 0, 0);
        gridPane.add(titleTextField, 1, 0);
        gridPane.add(new Label("Author:"), 0, 1);
        gridPane.add(authorTextField, 1, 1);
        gridPane.add(new Label("Year:"), 0, 2);
        gridPane.add(yearDatePicker, 1, 2);
        gridPane.add(new Label("Description:"), 0, 3);
        gridPane.add(descriptionTextField, 1, 3);

        // Create an Alert dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Update Book");
        alert.setHeaderText(null);
        alert.getDialogPane().setContent(gridPane);

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                String updatedTitle = titleTextField.getText();
                String updatedAuthor = authorTextField.getText();
                String updatedYear = yearDatePicker.getValue().toString();
                String updatedDescription = descriptionTextField.getText();

                try (Connection connection = MySQLConnection.getConnection();
                     PreparedStatement statement = connection.prepareStatement(
                             "UPDATE tblbooks SET title = ?, author = ?, date_pub = ?, description = ? WHERE title = ?")) {

                    statement.setString(1, updatedTitle);
                    statement.setString(2, updatedAuthor);
                    statement.setString(3, updatedYear);
                    statement.setString(4, updatedDescription);
                    statement.setString(5, title);

                    statement.executeUpdate();
                    System.out.println("Book Updated");

                    displayBookList(hideLibId.getText());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void deleteBook(String title) throws SQLException {
        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM tblbooks WHERE title = ?")) {
            statement.setString(1, title);
            statement.executeUpdate();

        }
    }

    public void logoutClick() throws IOException {
        AnchorPane x = pnMain;
        Parent p = FXMLLoader.load(getClass().getResource("login-view.fxml"));
        x.getChildren().clear();
        x.getChildren().add(p);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText(message);
        alert.showAndWait();
    }


}











