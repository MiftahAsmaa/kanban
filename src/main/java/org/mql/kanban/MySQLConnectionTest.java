package org.mql.kanban;
import java.sql.Connection;
import java.sql.DriverManager;

public class MySQLConnectionTest {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/ecommerce";
        String username = "root";
        String password = "";
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            System.out.println("Connexion réussie !");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
