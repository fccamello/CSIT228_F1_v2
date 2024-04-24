package com.example.csit228_f1_v2;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateTable {

    public void userTable(){
        Connection c = MySQLConnection.getConnection();
        String query = "CREATE TABLE IF NOT EXISTS tblLibrarian (" +
                "libid INT PRIMARY KEY AUTO_INCREMENT," +
                "firstname VARCHAR(50) NOT NULL," +
                "lastname VARCHAR(50) NOT NULL," +
                "username VARCHAR(50) NOT NULL," +
                "password VARCHAR(50) NOT NULL)";
        try{
            Statement statement  = c.createStatement();
            statement.execute(query);
            System.out.println("Table has been created.");
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            try {
                c.close();
            }
            catch (SQLException e){
                throw new RuntimeException(e);
            }
        }
    }

    public void booksTable(){
        Connection c = MySQLConnection.getConnection();
        String query = "CREATE TABLE IF NOT EXISTS tblBooks (" +
                "bookid INT PRIMARY KEY AUTO_INCREMENT," +
                "title VARCHAR(50) NOT NULL," +
                "author VARCHAR(50) NOT NULL," +
                "date_pub DATE NOT NULL," +
                "description VARCHAR(100) NOT NULL," +
                "libid INT NOT NULL," +
                "FOREIGN KEY (libid) REFERENCES tblLibrarian(libid) ON DELETE CASCADE ON UPDATE CASCADE" +
                ")";

        try{
            Statement statement  = c.createStatement();
            statement.execute(query);
            System.out.println("Table book has been created.");
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            try {
                c.close();
            }
            catch (SQLException e){
                throw new RuntimeException(e);
            }
        }
    }

}
