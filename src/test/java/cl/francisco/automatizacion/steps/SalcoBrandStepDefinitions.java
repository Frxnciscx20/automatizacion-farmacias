package cl.francisco.automatizacion.steps;

import cl.francisco.automatizacion.db.MedicamentoDAO;
import cl.francisco.automatizacion.pages.SalcoBrandPage;
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


import java.nio.file.Paths;
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SalcoBrandStepDefinitions {

    private WebDriver driver;
    private SalcoBrandPage salcoBrandPage;

    private String nombreProducto;
    private double precioPromocional;
    private double precioNormal;
    private String urlActual;
    private String imagenDelMedicamento;

    @Given("que el usuario navega al sitio de SalcoBrand")
    public void que_el_usuario_navega_al_sitio_de_SalcoBrand() {
        WebDriver driver = Hooks.getDriver();
        driver.manage().window().maximize();
        driver.get("https://salcobrand.cl");

        salcoBrandPage = new SalcoBrandPage(driver);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(40));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("body")));

        System.out.println("🟢 Navegación a SalcoBrand completada");
    }



    @When("^el usuario en Farmacias SalcoBrand busca el medicamento \"([^\"]*)\"$")
    public void el_usuario_busca_el_medicamento(String medicamento) {
        Assert.assertTrue("No se visualiza el campo de búsqueda", salcoBrandPage.isVisibleInputBuscarMedicamentos());
        salcoBrandPage.sendKeysInputBuscarMedicamentoSalcoBrandPage(medicamento);
        salcoBrandPage.clickBtnPrimeraOpcionMedicamentoSalcoBrandPage(medicamento);
    }

    @Then("debería ver el nombre del producto en Farmacias SalcoBrand")
    public void deberia_ver_el_nombre_del_producto() {
        Assert.assertTrue("No se visualiza el producto", salcoBrandPage.isVisibleInputBuscarMedicamentos());
        nombreProducto = salcoBrandPage.getTextNombreProductoSalcoBrandPage();
        System.out.println("Nombre producto: " + nombreProducto);
    }

    @And("debería ver el precio promocional del producto en SalcoBrand")
    public void deberia_ver_el_precio_promocional() {
        try {
            String precioTexto = salcoBrandPage.getTextPrecioPromocionProductoSalcoBrandPage();

            if (precioTexto == null || precioTexto.trim().isEmpty()) {
                precioPromocional = 0;
                System.out.println("Precio Promoción: 0 (no disponible)");
                return;
            }

            Pattern pattern = Pattern.compile("\\$\\d{1,3}(\\.\\d{3})*");
            Matcher matcher = pattern.matcher(precioTexto);

            if (matcher.find()) {
                String precioEncontrado = matcher.group();
                String soloNumeros = precioEncontrado.replaceAll("[^\\d]", "");
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

    @And("debería ver el precio normal del producto en SalcoBrand")
    public void deberia_ver_el_precio_normal() {
        try {
            String precioTexto = salcoBrandPage.getTextPrecioNormalProductoSalcoBrandPage();

            if (precioTexto == null || precioTexto.trim().isEmpty()) {
                precioNormal = 0;
                System.out.println("Precio Normal: 0 (no disponible)");
                return;
            }
            Pattern pattern = Pattern.compile("\\$\\d{1,3}(\\.\\d{3})*");
            Matcher matcher = pattern.matcher(precioTexto);

            if (matcher.find()) {
                String precioEncontrado = matcher.group();
                String soloNumeros = precioEncontrado.replaceAll("[^\\d]", "");
                precioNormal = Long.parseLong(soloNumeros);
                System.out.println("Precio Normal: " + precioNormal);
            } else {
                precioNormal = 0;
                System.out.println("Precio Normal: 0 (formato no encontrado)");
            }

        } catch (Exception e) {
            System.err.println("Error procesando precio normal: " + e.getMessage());
            precioNormal = 0;
        }
    }

    @And("guardo la URL actual de la página en Salcobrand")
    public void guardo_la_url_actual_de_la_pagina() {
        try {
            urlActual = driver.getCurrentUrl();
            System.out.println("URL actual: " + urlActual);

        } catch (Exception e) {
            System.err.println("Error obteniendo la URL actual: " + e.getMessage());
        }
    }

    @And("se guarda la información del medicamento de SalcoBrand en la base de datos")
    public void guardarInformacionSalcobrandEnBaseDeDatos() {
        WebDriver driver = Hooks.getDriver();
        try {
            int idFarmacia = 3;
            String nombreFarmacia = "SalcoBrand";
            String urlWeb = "https://salcobrand.cl";

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

//            // 📩 Notificar si bajó
//            if (precioPromocional < precioAnterior) {
//                NotificacionClient.notificarBajadaPrecio(idMedicamento, precioPromocional);
//            }

            System.out.println("💾 Datos de SalcoBrand guardados correctamente.");

        } catch (Exception e) {
            System.err.println("❌ Error al guardar los datos de SalcoBrand: " + e.getMessage());
            e.printStackTrace();
        }
    }


    @And("guardo la URL de la imagen del medicamento en SalcoBrand")
    public void guardoLaURLDeLaImagenDelMedicamentoEnSalcoBrand() {
        imagenDelMedicamento = salcoBrandPage.getUrlImagenMedicamento();
        System.out.println("URL IMAGEN DEL MEDICAMENTO" + imagenDelMedicamento);
    }
}