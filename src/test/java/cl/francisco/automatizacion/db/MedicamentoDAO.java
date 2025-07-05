package cl.francisco.automatizacion.db;

import java.sql.*;
import java.time.LocalDateTime;

public class MedicamentoDAO {

    public static boolean existeFarmacia(int idFarmacia) {
        String sql = "SELECT 1 FROM farmacia WHERE id_farmacia = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idFarmacia);
            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (Exception e) {
            System.err.println("Error al verificar existencia de farmacia: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static void guardarFarmacia(int idFarmacia, String nombre, String urlWeb) {
        String sql = "INSERT INTO farmacia (id_farmacia, nombre, url_web) VALUES (?, ?, ?)";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idFarmacia);
            stmt.setString(2, nombre);
            stmt.setString(3, urlWeb);

            stmt.executeUpdate();
            System.out.println("Farmacia guardada correctamente.");

        } catch (Exception e) {
            System.err.println("Error al guardar farmacia: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static int buscarIdMedicamentoPorNombre(String nombre) {
        String sql = "SELECT id_medicamento FROM medicamento WHERE nombre = ?";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nombre);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("id_medicamento");
            }

        } catch (Exception e) {
            System.err.println("Error al buscar medicamento: " + e.getMessage());
            e.printStackTrace();
        }

        return -1;
    }

    public static int insertarMedicamento(String nombre, String descripcion, String laboratorio, String presentacion, String url_medicamento, String imagen_url) {
        String sql = "INSERT INTO medicamento (nombre, descripcion, laboratorio, presentacion, url_medicamento, imagen_url) VALUES (?, ?, ?, ?, ?, ?) RETURNING id_medicamento";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nombre);
            stmt.setString(2, descripcion);
            stmt.setString(3, laboratorio);
            stmt.setString(4, presentacion);
            stmt.setString(5, url_medicamento);
            stmt.setString(6, imagen_url);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int idGenerado = rs.getInt("id_medicamento");
                System.out.println("Medicamento insertado con ID: " + idGenerado);
                return idGenerado;
            }

        } catch (Exception e) {
            System.err.println("Error al insertar medicamento: " + e.getMessage());
            e.printStackTrace();
        }

        return -1;
    }


    public static void guardarPrecio(int idMedicamento, int idFarmacia, double precioActual, double precioNormal) {
        String sql = "INSERT INTO precio (id_medicamento, id_farmacia, precio_actual, precio_normal, fecha_actualizacion) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idMedicamento);
            stmt.setInt(2, idFarmacia);
            stmt.setDouble(3, precioActual);
            stmt.setDouble(4, precioNormal);
            stmt.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));

            stmt.executeUpdate();
            System.out.println("Precio guardado correctamente.");

        } catch (Exception e) {
            System.err.println("Error al guardar precio: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static double obtenerUltimoPrecioPromocional(int idMedicamento, int idFarmacia) {
        String sql = "SELECT precio_actual FROM precio " +
                "WHERE id_medicamento = ? AND id_farmacia = ? " +
                "ORDER BY fecha_actualizacion DESC LIMIT 1";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idMedicamento);
            stmt.setInt(2, idFarmacia);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("precio_actual");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el último precio promocional: " + e.getMessage());
        }
        return Double.MAX_VALUE; // Retornar un número muy alto si no hay precios anteriores
    }
}
