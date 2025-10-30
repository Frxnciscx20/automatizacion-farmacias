package cl.francisco.automatizacion.pages;

import cl.francisco.automatizacion.utils.Utilidades;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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

    @FindBy(xpath = "//input[@placeholder='Buscar...']")
    private WebElement inputBuscarCruzVerde;

    @FindBy(xpath = "//h1[@class='text-28 leading-35 font-bold w-3/4']")
    private WebElement textNombreProductoCruzVerde;

    @FindBy(xpath = "//p[contains(@class, 'text-green-turquoise') and contains(normalize-space(.), '$')]")
    private WebElement textPrecioOfertaCruzVerde;

    @FindBy(xpath = "//p[@class='font-semibold leading-16 leading-22 line-through mb-2 mr-8 text-14 text-16 text-gray-dark ng-star-inserted']")
    private WebElement textPrecioNormalCruzVerde;

    @FindBy(xpath = "(//img[contains(@class, 'ngxImageZoomFull')])[1]")
    private WebElement urlImagenMedicamento;

    @FindBy(xpath = "//span[normalize-space(text())='Aceptar' and @class='ng-star-inserted']")
    private WebElement btnAceptarAlertaLocacion;

    public boolean isVisibleInputBuscarMedicamentos(){
        utilidades.waitUntilElementIsVisibleNonThrow(inputBuscarCruzVerde,30);
        return utilidades.isVisible(inputBuscarCruzVerde);
    }

    public void sendKeysInputBuscarMedicamentoCruzVerde(String medicamento){
        utilidades.esperarAntesDeContinuar();
        utilidades.waitUntilElementIsVisible(inputBuscarCruzVerde);
        inputBuscarCruzVerde.sendKeys(medicamento);
    }

    public void clickBtnPrimeraOpcionMedicamentoSalcoCruzVerde(String medicamento) {
        By selector = By.xpath("(//span[contains(text(),'"+medicamento+"')])[1]");
        utilidades.waitUntilElementIsVisible(selector);
        WebElement element = driver.findElement(selector);
        element.click();
    }

    public String getTextNombreProductoCruzVerde() {
        return textNombreProductoCruzVerde.getText();
    }

    public String getTextPrecioPromocionProductoCruzVerde() {
        try {
            return textPrecioOfertaCruzVerde.getText().trim();
        } catch (Exception e) {
            System.err.println("No se encontró el precio promocional en Cruz Verde: " + e.getMessage());
            return "";
        }
    }

    public String getTextPrecioNormalProductoCruzVerde() {
        return textPrecioNormalCruzVerde.getText();
    }

    public String getUrlImagenMedicamento() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("(//img[contains(@class, 'ngxImageZoomFull')])[1]")
            ));
            wait.until(ExpectedConditions.attributeContains(urlImagenMedicamento, "src", "http"));
            String src = urlImagenMedicamento.getAttribute("src");
            System.out.println("✅ Imagen capturada: " + src);
            return src != null ? src.trim() : "";
        } catch (Exception e) {
            System.err.println("❌ Error obteniendo imagen: " + e.getMessage());
            return "";
        }
    }

    public boolean isVisibleAlertaLocacion(){
        utilidades.waitUntilElementIsVisibleNonThrow(btnAceptarAlertaLocacion,10);
        return utilidades.isVisible(btnAceptarAlertaLocacion);
    }

    public void clickBtnAlertaLocacion(){
        utilidades.waitUntilElementIsVisible(btnAceptarAlertaLocacion);
        btnAceptarAlertaLocacion.click();
    }
}
