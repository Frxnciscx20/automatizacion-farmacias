package cl.francisco.automatizacion.steps;

import cl.francisco.automatizacion.db.MedicamentoDAO;
import cl.francisco.automatizacion.pages.FarmaciasAhumadaPage;
import io.cucumber.java.en.*;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.math.BigDecimal;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.time.Duration;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import cl.francisco.automatizacion.utils.NotificacionClient;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class FarmaciasAhumadaStepDefinitions {

    private WebDriver driver;
    private FarmaciasAhumadaPage ahumadaPage;

    private String nombreProducto;
    private double precioPromocional;
    private double precioNormal;
    private String urlActual;
    private String imagenDelMedicamento;

    @Given("que el usuario navega al sitio de Farmacias Ahumada")
    public void que_el_usuario_navega_al_sitio_de_Farmacias_Ahumada() {
        WebDriver driver = Hooks.getDriver();
        driver.manage().window().maximize();
        driver.get("https://www.farmaciasahumada.cl");

        ahumadaPage = new FarmaciasAhumadaPage(driver);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(40));

        // Esperar que el documento esté completamente cargado
        wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                .executeScript("return document.readyState")
                .equals("complete"));

        // Esperar que el body sea visible
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("body")));

        // Manejo del banner de cookies si aparece
        try {
            if (ahumadaPage.getTextoUsoDeCookies().isDisplayed()) {
                ahumadaPage.clickBtnSiUsoDeCookies();
                System.out.println("🍪 Banner de cookies detectado y aceptado correctamente");
            }
        } catch (Exception e) {
            System.out.println("⚠️ No apareció el banner de cookies (posiblemente ya estaba aceptado).");
        }

        System.out.println("🟢 Navegación a Farmacias Ahumada completada");
    }




    @When("^el usuario en Farmacias Ahumada busca el medicamento \"([^\"]*)\"$")
    public void el_usuario_busca_el_medicamento(String medicamento) {
        Assert.assertTrue("No se visualiza", ahumadaPage.isVisibleInputBuscarMedicamento());
        ahumadaPage.sendKeysInputBuscarMedicamento(medicamento);
        ahumadaPage.clickBtnPrimeraOpcionMedicamento();

        if (ahumadaPage.isVisibleBtnCerrarAlertaOferta()) {
            ahumadaPage.clickBtnCerrarAlertaOferta();
        }
    }

    @Then("debería ver el nombre del producto en Farmacias Ahumada")
    public void deberia_ver_el_nombre_del_producto() {
        nombreProducto = ahumadaPage.getTextNombreProducto();
        System.out.println("Nombre producto: " + nombreProducto);
    }

    @And("debería ver el precio promocional del producto en Farmacias Ahumada")
    public void deberia_ver_el_precio_promocional_del_producto() {
        try {
            String precioTexto = ahumadaPage.getTextPrecioPromocionProducto();

            if (precioTexto == null || precioTexto.trim().isEmpty()) {
                precioPromocional = 0;
                System.out.println("Precio Promoción: 0 (no disponible)");
                return;
            }
            Pattern pattern = Pattern.compile("\\$\\d{1,3}(\\.\\d{3})*");
            Matcher matcher = pattern.matcher(precioTexto);

            if (matcher.find()) {
                String precioEncontrado = matcher.group(); // Ej: "$44.199"

                String soloNumeros = precioEncontrado.replaceAll("[^\\d]", ""); // Resultado: "44199"

                precioPromocional = Long.parseLong(soloNumeros);
                System.out.println("Precio Promoción: " + precioPromocional);
            } else {
                precioPromocional = 0;
                System.out.println("Precio Promoción: 0 (formato no encontrado)");
            }

        } catch (Exception e) {
            System.err.println("Error procesando precio promocional: " + e.getMessage());
            precioPromocional = 0;
        }
    }


    @And("debería ver el precio normal del producto en Farmacias Ahumada")
    public void deberia_ver_el_precio_normal_del_producto() {
        String precioNormalTexto = ahumadaPage.getTextPrecioNormalProducto().replaceAll("[^\\d]", "");
        precioNormal = precioNormalTexto.isEmpty() ? 0 : Double.parseDouble(precioNormalTexto);
        System.out.println("Precio Normal: $" + precioNormal);
    }

    @And("guardo la URL actual de la página en farmacias Ahumada")
    public void guardo_la_url_actual_de_la_pagina() {
        try {
            urlActual = driver.getCurrentUrl();
            System.out.println("URL actual: " + urlActual);

        } catch (Exception e) {
            System.err.println("Error obteniendo la URL actual: " + e.getMessage());
        }
    }


    @And("se guarda la información del medicamento en la base de datos")
    public void guardarInformacionEnBaseDeDatos() {
        WebDriver driver = Hooks.getDriver();
        try {
            int idFarmacia = 2;
            String nombreFarmacia = "Farmacias Ahumada";
            String urlWeb = "https://www.farmaciasahumada.cl";

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

            // 🔍 Obtener último precio antes de guardar
            double precioAnterior = MedicamentoDAO.obtenerUltimoPrecioPromocional(idMedicamento, idFarmacia);

            // 💾 Guardar nuevo precio
            boolean stock = true;
            MedicamentoDAO.guardarPrecio(idMedicamento, idFarmacia, precioPromocional, precioNormal);

            // 📩 Notificar si el precio bajó
            if (precioPromocional < precioAnterior) {
                NotificacionClient.notificarBajadaPrecio(idMedicamento, precioPromocional);
            }

            System.out.println("💾 Datos de Farmacias Ahumada guardados correctamente.");

        } catch (Exception e) {
            System.err.println("❌ Error al guardar los datos de Farmacias Ahumada: " + e.getMessage());
            e.printStackTrace();
        }
    }




    @And("guardo la URL de la imagen del medicamento en Farmacias Ahumada")
    public void guardoLaURLDeLaImagenDelMedicamentoEnFarmaciasAhumada() {
        imagenDelMedicamento = ahumadaPage.getUrlImagenMedicamento();
        System.out.println("URL IMAGEN DEL MEDICAMENTO: " + imagenDelMedicamento);
    }
}
