package cl.francisco.automatizacion.utils;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class NotificacionClient {

    public static void notificarBajadaPrecio(int idMedicamento, double nuevoPrecio) {
        try {
            URL url = new URL("http://localhost:3000/api/notificar-bajada"); // usa tu dominio si está en producción
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);

            String body = String.format("{\"id_medicamento\": %d, \"nuevo_precio\": %.0f}", idMedicamento, nuevoPrecio);

            try (OutputStream os = con.getOutputStream()) {
                byte[] input = body.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = con.getResponseCode();
            System.out.println("Llamada a notificar-bajada, código respuesta: " + responseCode);
        } catch (Exception e) {
            System.err.println("Error al notificar bajada de precio: " + e.getMessage());
        }
    }
}
