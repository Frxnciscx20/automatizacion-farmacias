package cl.francisco.automatizacion.steps;

import cl.francisco.automatizacion.db.MedicamentoDAO;
import cl.francisco.automatizacion.pages.FarmaciasAhumadaPage;
import io.cucumber.java.en.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FarmaciasAhumadaStepDefinitions {

    private WebDriver driver;
    private FarmaciasAhumadaPage ahumadaPage;

    private String nombreProducto;
    private double precioPromocional;
    private double precioNormal;
    private String urlActual;

    @Given("que el usuario navega al sitio de Farmacias Ahumada")
    public void que_el_usuario_navega_al_sitio_de_Farmacias_Ahumada() {
        // Ruta fija al driver Edge
        System.setProperty("webdriver.edge.driver", "C:\\Users\\Francisco\\Documents\\Drivers\\msedgedriver.exe");

        EdgeOptions options = new EdgeOptions();
        options.addArguments("--remote-allow-origins=*");
        driver = new EdgeDriver(options);
        driver.manage().window().maximize();

        driver.get("https://www.farmaciasahumada.cl");
        ahumadaPage = new FarmaciasAhumadaPage(driver);

        if (ahumadaPage.isVisibleTextoUsoDeCookies()) {
            ahumadaPage.clickBtnSiUsoDeCookies();
        }
    }


    @When("^el usuario en Farmacias Ahumada busca el medicamento \"([^\"]*)\"$")
    public void el_usuario_busca_el_medicamento(String medicamento) {
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

            // Verificar si el texto del precio existe
            if (precioTexto == null || precioTexto.trim().isEmpty()) {
                precioPromocional = 0;
                System.out.println("Precio Promoción: 0 (no disponible)");
                return;
            }

            // Usar expresión regular para encontrar el precio en formato $xx.xxx
            Pattern pattern = Pattern.compile("\\$\\d{1,3}(\\.\\d{3})*");
            Matcher matcher = pattern.matcher(precioTexto);

            if (matcher.find()) {
                String precioEncontrado = matcher.group(); // Ej: "$44.199"

                // Eliminar el símbolo $ y puntos
                String soloNumeros = precioEncontrado.replaceAll("[^\\d]", ""); // Resultado: "44199"

                // Convertir a long y guardar
                precioPromocional = Long.parseLong(soloNumeros);
                System.out.println("Precio Promoción: " + precioPromocional);
            } else {
                // No se encontró un precio con formato esperado
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
                idMedicamento = MedicamentoDAO.insertarMedicamento(nombreProducto, descripcion, laboratorio, presentacion, urlActual);
            }

            boolean stock = true;
            MedicamentoDAO.guardarPrecio(idMedicamento, idFarmacia, precioPromocional, precioNormal);

            System.out.println("Datos guardados correctamente.");

        } catch (Exception e) {
            System.err.println("Error al guardar los datos: " + e.getMessage());
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }

}
