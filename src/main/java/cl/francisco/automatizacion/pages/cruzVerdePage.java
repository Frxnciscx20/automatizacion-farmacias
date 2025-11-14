package cl.francisco.automatizacion.pages;

import cl.francisco.automatizacion.utils.Utilidades;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class cruzVerdePage {

    private WebDriver driver;
    private Utilidades utilidades;

    public cruzVerdePage(WebDriver driver) {
        this.driver = driver;
        this.utilidades = new Utilidades(driver);
        PageFactory.initElements(driver, this);
    }

    // 🧩 ELEMENTOS
    @FindBy(xpath = "//input[@placeholder='Buscar...']")
    private WebElement inputBuscarCruzVerde;

    @FindBy(xpath = "//h1[@class='text-28 leading-35 font-bold w-3/4']")
    private WebElement textNombreProductoCruzVerde;

    @FindBy(xpath = "//p[contains(@class, 'text-green-turquoise') and contains(normalize-space(.), '$')]")
    private WebElement textPrecioOfertaCruzVerde;

    @FindBy(xpath = "//p[contains(@class,'line-through') and contains(@class,'text-gray-dark')]")
    private WebElement textPrecioNormalCruzVerde;

    @FindBy(xpath = "(//img[contains(@class, 'ngxImageZoomFull')])[1]")
    private WebElement urlImagenMedicamento;

    @FindBy(xpath = "//span[normalize-space(text())='Aceptar' and @class='ng-star-inserted']")
    private WebElement btnAceptarAlertaLocacion;


    // 🕒 MÉTODOS MEJORADOS CON ESPERAS ROBUSTAS

    public boolean isVisibleInputBuscarMedicamentos() {
        utilidades.waitUntilElementIsVisibleNonThrow(inputBuscarCruzVerde, 45);
        return utilidades.isVisible(inputBuscarCruzVerde);
    }

    public boolean isVisibleInputBuscarMedicamentoCruzVerde() {
        utilidades.waitUntilElementIsVisibleNonThrow(inputBuscarCruzVerde, 45);
        return utilidades.isVisible(inputBuscarCruzVerde);
    }

    public void sendKeysInputBuscarMedicamentoCruzVerde(String medicamento) {
        utilidades.waitUntilElementIsVisible(inputBuscarCruzVerde);
        utilidades.esperarSegundos(2); // pequeña pausa antes de escribir
        inputBuscarCruzVerde.clear();
        inputBuscarCruzVerde.sendKeys(medicamento);
        utilidades.esperarSegundos(2); // da tiempo para que cargue el listado dinámico
    }

    public void clickBtnPrimeraOpcionMedicamentoSalcoCruzVerde(String medicamento) {
        try {
            By selector = By.xpath("(//span[contains(text(),'" + medicamento + "')])[1]");
            utilidades.waitUntilElementIsVisibleNonThrow(selector, 50);
            WebElement element = driver.findElement(selector);
            utilidades.esperarSegundos(1);
            element.click();
            System.out.println("✅ Clic en primera opción de medicamento: " + medicamento);
        } catch (Exception e) {
            System.err.println("⚠️ No se pudo hacer clic en el primer resultado: " + e.getMessage());
        }
    }

    public String getTextNombreProductoCruzVerde() {
        utilidades.waitUntilElementIsVisibleNonThrow(textNombreProductoCruzVerde, 50);
        return textNombreProductoCruzVerde.getText().trim();
    }

    public String getTextPrecioPromocionProductoCruzVerde() {
        try {
            utilidades.waitUntilElementIsVisibleNonThrow(textPrecioOfertaCruzVerde, 50);
            return textPrecioOfertaCruzVerde.getText().trim();
        } catch (Exception e) {
            System.err.println("⚠️ No se encontró precio promocional en Cruz Verde: " + e.getMessage());
            return "";
        }
    }

    public String getTextPrecioNormalProductoCruzVerde() {
        utilidades.waitUntilElementIsVisibleNonThrow(textPrecioNormalCruzVerde, 50);
        return textPrecioNormalCruzVerde.getText().trim();
    }

    public String getUrlImagenMedicamento() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(50));
            wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("(//img[contains(@class, 'ngxImageZoomFull')])[1]")
            ));
            wait.until(ExpectedConditions.attributeContains(urlImagenMedicamento, "src", "http"));
            String src = urlImagenMedicamento.getAttribute("src");
            System.out.println("🖼️ Imagen del medicamento capturada: " + src);
            return src != null ? src.trim() : "";
        } catch (Exception e) {
            System.err.println("❌ Error obteniendo imagen en Cruz Verde: " + e.getMessage());
            return "";
        }
    }

    public boolean isVisibleAlertaLocacion() {
        utilidades.waitUntilElementIsVisibleNonThrow(btnAceptarAlertaLocacion, 15);
        return utilidades.isVisible(btnAceptarAlertaLocacion);
    }

    public void clickBtnAlertaLocacion() {
        utilidades.waitUntilElementIsVisibleNonThrow(btnAceptarAlertaLocacion, 10);
        try {
            btnAceptarAlertaLocacion.click();
            System.out.println("📍 Alerta de ubicación cerrada correctamente.");
            utilidades.esperarSegundos(2);
        } catch (Exception e) {
            System.err.println("⚠️ No se pudo cerrar la alerta de ubicación: " + e.getMessage());
        }
    }
}
