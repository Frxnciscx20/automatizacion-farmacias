package cl.francisco.automatizacion.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {
    private static final String URL = "jdbc:postgresql://db.kbtpsxbckhpoqiqpgcji.supabase.co:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "2c0tHKjUydH2Vglq";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}