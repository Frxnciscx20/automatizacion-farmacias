package cl.francisco.automatizacion.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {
    private static final String URL = "jdbc:postgresql://aws-0-us-east-1.pooler.supabase.com:5432/postgres?sslmode=require";
    private static final String USER = "postgres.kbtpsxbckhpoqiqpgcji"; // Usuario del pooler
    private static final String PASSWORD = "2c0tHKjUydH2Vglq"; // Tu contraseña real

    public static Connection getConnection() throws SQLException {
        System.setProperty("java.net.preferIPv4Stack", "true"); // Forzar IPv4 por si tu red lo requiere
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
