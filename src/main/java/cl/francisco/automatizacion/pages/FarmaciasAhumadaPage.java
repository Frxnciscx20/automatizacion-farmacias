package cl.francisco.automatizacion.pages;

import cl.francisco.automatizacion.utils.Utilidades;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class FarmaciasAhumadaPage {

    private WebDriver driver;
    private Utilidades utilidades;

    public FarmaciasAhumadaPage(WebDriver driver) {
        this.driver = driver;
        this.utilidades = new Utilidades(driver);
        PageFactory.initElements(driver, this);
    }

    // 🔍 ELEMENTOS
    @FindBy(xpath = "(//form[@role='search'])[1]/input[1]")
    private WebElement inputBuscarMedicamento;

    @FindBy(xpath = "//li[@id='product-0']/a")
    private WebElement btnPrimeraOpcionMedicamento;

    @FindBy(xpath = "//h1[@class='product-name']")
    private WebElement textNombreProducto;

    @FindBy(xpath = "(//div[@class='price'])[1]")
    private WebElement textPrecioPromocionProducto;

    @FindBy(xpath = "//span[@class='strike-through list text-decoration-none']")
    private WebElement textPrecioNormalProducto;

    @FindBy(xpath = "//div[normalize-space(text())='Uso de cookies']")
    private WebElement textUsoDeCookies;

    @FindBy(xpath = "//button[normalize-space(text())='Sí' or normalize-space(text())='Sì' or normalize-space(text())='Aceptar']")
    private WebElement btnSiUsoDeCookies;

    @FindBy(xpath = "(//button[@class='close p-0 m-0'])[2]")
    private WebElement btnCerrarAlertaOferta;

    @FindBy(xpath = "//span[@class='swiper-slide swiper-slide-active']/img")
    private WebElement urlImagenMedicamento;

    // 🕒 MÉTODOS MEJORADOS CON ESPERAS ROBUSTAS

    public boolean isVisibleInputBuscarMedicamento() {
        utilidades.waitUntilElementIsVisibleNonThrow(inputBuscarMedicamento, 45);
        return utilidades.isVisible(inputBuscarMedicamento);
    }

    public void sendKeysInputBuscarMedicamento(String medicamento) {
        utilidades.waitUntilElementIsVisible(inputBuscarMedicamento);
        utilidades.esperarSegundos(2); // ⏳ pequeña pausa para evitar StaleElement
        inputBuscarMedicamento.clear();
        inputBuscarMedicamento.sendKeys(medicamento);
    }

    public void clickBtnPrimeraOpcionMedicamento() {
        // Espera más larga porque el buscador de Ahumada carga lento
        utilidades.waitUntilElementIsVisibleNonThrow(btnPrimeraOpcionMedicamento, 50);
        utilidades.esperarSegundos(1);
        btnPrimeraOpcionMedicamento.click();
    }

    public String getTextNombreProducto() {
        utilidades.waitUntilElementIsVisibleNonThrow(textNombreProducto, 40);
        return textNombreProducto.getText().trim();
    }

    public String getTextPrecioPromocionProducto() {
        utilidades.waitUntilElementIsVisibleNonThrow(textPrecioPromocionProducto, 40);
        return textPrecioPromocionProducto.getText().trim();
    }

    public String getTextPrecioNormalProducto() {
        utilidades.waitUntilElementIsVisibleNonThrow(textPrecioNormalProducto, 40);
        return textPrecioNormalProducto.getText().trim();
    }

    public boolean isVisibleTextoUsoDeCookies() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(30))
                    .until(ExpectedConditions.visibilityOf(textUsoDeCookies));
            return utilidades.isVisible(textUsoDeCookies);
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void clickBtnSiUsoDeCookies() {
        utilidades.waitUntilElementIsVisibleNonThrow(btnSiUsoDeCookies, 10);
        btnSiUsoDeCookies.click();
        utilidades.esperarSegundos(2);
    }

    public boolean isVisibleBtnCerrarAlertaOferta() {
        utilidades.waitUntilElementIsVisibleNonThrow(btnCerrarAlertaOferta, 20);
        return utilidades.isVisible(btnCerrarAlertaOferta);
    }

    public void clickBtnCerrarAlertaOferta() {
        utilidades.waitUntilElementIsVisibleNonThrow(btnCerrarAlertaOferta, 15);
        btnCerrarAlertaOferta.click();
        utilidades.esperarSegundos(1);
    }

    public String getUrlImagenMedicamento() {
        utilidades.waitUntilElementIsVisibleNonThrow(urlImagenMedicamento, 30);
        return urlImagenMedicamento.getAttribute("src");
    }

    public WebElement getTextoUsoDeCookies() {
        return textUsoDeCookies;
    }

}
