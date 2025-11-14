package cl.francisco.automatizacion.steps;

import cl.francisco.automatizacion.db.MedicamentoDAO;
import cl.francisco.automatizacion.pages.cruzVerdePage;
import cl.francisco.automatizacion.utils.NotificacionClient;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class cruzVerdeDefinition {

    private WebDriver driver;
    private cruzVerdePage cruzVerdePage;

    private String nombreProducto;
    private double precioPromocional;
    private double precioNormal;
    private String urlActual;
    private String imagenDelMedicamento;

    @Given("que el usuario navega al sitio de Cruz Verde")
    public void que_el_usuario_navega_al_sitio_de_CruzVerde() {
        WebDriver driver = Hooks.getDriver();
        driver.manage().window().maximize();
        driver.get("https://www.cruzverde.cl");

        cruzVerdePage = new cruzVerdePage(driver);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(40));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("body")));

        // Manejo de alerta de ubicación si aparece
        try {
            if (cruzVerdePage.isVisibleAlertaLocacion()) {
                cruzVerdePage.clickBtnAlertaLocacion();
                System.out.println("📍 Alerta de ubicación aceptada");
            }
        } catch (Exception e) {
            System.out.println("⚠️ No apareció la alerta de ubicación");
        }

        System.out.println("🟢 Navegación a Cruz Verde completada");
    }



    @When("^el usuario en Cruz Verde busca el medicamento \"([^\"]*)\"$")
    public void el_usuario_busca_el_medicamento(String medicamento) {
        Assert.assertTrue("No se visualiza", cruzVerdePage.isVisibleInputBuscarMedicamentoCruzVerde());
        cruzVerdePage.sendKeysInputBuscarMedicamentoCruzVerde(medicamento);
        cruzVerdePage.clickBtnPrimeraOpcionMedicamentoSalcoCruzVerde(medicamento);
    }

    @Then("debería ver el nombre del producto en Cruz Verde")
    public void deberia_ver_el_nombre_del_producto() {
        nombreProducto = cruzVerdePage.getTextNombreProductoCruzVerde();
        System.out.println("Nombre producto: " + nombreProducto);
    }

    @And("debería ver el precio promocional del producto en Cruz Verde")
    public void deberia_ver_el_precio_promocional_del_producto() {
        try {
            String precioTexto = cruzVerdePage.getTextPrecioPromocionProductoCruzVerde();
            System.out.println("Texto capturado desde la página: '" + precioTexto + "'");

            if (precioTexto == null || precioTexto.trim().isEmpty()) {
                precioPromocional = 0;
                System.out.println("⚠️ Precio Promoción: 0 (texto vacío o elemento no encontrado)");
                return;
            }

            // Patrón actualizado: acepta espacio entre $ y número (ej: "$ 23.352")
            Pattern pattern = Pattern.compile("\\$\\s?\\d{1,3}(\\.\\d{3})*");
            Matcher matcher = pattern.matcher(precioTexto);

            if (matcher.find()) {
                String precioEncontrado = matcher.group(); // Ej: "$ 23.352"
                System.out.println("Texto encontrado por regex: " + precioEncontrado);

                // Elimina todo excepto los dígitos
                String soloNumeros = precioEncontrado.replaceAll("[^\\d]", "");
                precioPromocional = Long.parseLong(soloNumeros);
                System.out.println("✅ Precio Promoción detectado: " + precioPromocional);
            } else {
                precioPromocional = 0;
                System.out.println("⚠️ Precio Promoción: 0 (formato no coincidió con regex)");
            }

        } catch (Exception e) {
            System.err.println("❌ Error procesando precio promocional: " + e.getMessage());
            precioPromocional = 0;
        }
    }


    @And("debería ver el precio normal del producto en Cruz Verde")
    public void deberia_ver_el_precio_normal_del_producto() {
        String precioNormalTexto = cruzVerdePage.getTextPrecioNormalProductoCruzVerde().replaceAll("[^\\d]", "");
        precioNormal = precioNormalTexto.isEmpty() ? 0 : Double.parseDouble(precioNormalTexto);
        System.out.println("Precio Normal: $" + precioNormal);
    }

    @And("guardo la URL actual de la página en Cruz Verde")
    public void guardo_la_url_actual_de_la_pagina() {
        try {
            urlActual = driver.getCurrentUrl();
            System.out.println("URL actual: " + urlActual);

        } catch (Exception e) {
            System.err.println("Error obteniendo la URL actual: " + e.getMessage());
        }
    }


    @And("se guarda la información del medicamento Cruz Verde en la base de datos")
    public void guardarInformacionCruzVerdeEnBaseDeDatos() {
        WebDriver driver = Hooks.getDriver();
        try {
            int idFarmacia = 4;
            String nombreFarmacia = "Cruz Verde";
            String urlWeb = "https://www.cruzverde.cl";

            if (!MedicamentoDAO.existeFarmacia(idFarmacia)) {
                MedicamentoDAO.guardarFarmacia(idFarmacia, nombreFarmacia, urlWeb);
            }

            String descripcion = "Sin descripción";
            String laboratorio = "Desconocido";
            String presentacion = "Desconocida";

            int idMedicamento = MedicamentoDAO.buscarIdMedicamentoPorNombre(nombreProducto);
            if (idMedicamento == -1) {
                idMedicamento = MedicamentoDAO.insertarMedicamento(
                        nombreProducto,
                        descripcion,
                        laboratorio,
                        presentacion,
                        urlActual,
                        imagenDelMedicamento
                );
            }

            // 🔍 Obtener precio anterior
            double precioAnterior = MedicamentoDAO.obtenerUltimoPrecioPromocional(idMedicamento, idFarmacia);

            // 💾 Guardar nuevo precio
            boolean stock = true;
            MedicamentoDAO.guardarPrecio(idMedicamento, idFarmacia, precioPromocional, precioNormal);

            // 📩 Notificar si el precio bajó
            if (precioPromocional < precioAnterior) {
                NotificacionClient.notificarBajadaPrecio(idMedicamento, precioPromocional);
            }

            System.out.println("💾 Datos de Cruz Verde guardados correctamente.");

        } catch (Exception e) {
            System.err.println("❌ Error al guardar los datos de Cruz Verde: " + e.getMessage());
            e.printStackTrace();
        }
    }




    @And("guardo la URL de la imagen del medicamento en Cruz Verde")
    public void guardoLaURLDeLaImagenDelMedicamentoEnFarmaciasAhumada() {
        imagenDelMedicamento = cruzVerdePage.getUrlImagenMedicamento();
        System.out.println("URL IMAGEN DEL MEDICAMENTO: " + imagenDelMedicamento);
    }
}
