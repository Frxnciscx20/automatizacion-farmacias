package cl.francisco.automatizacion.pages;

import cl.francisco.automatizacion.utils.Utilidades;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SalcoBrandPage {

    private WebDriver driver;
    private Utilidades utilidades;

    public SalcoBrandPage(WebDriver driver) {
        this.driver = driver;
        this.utilidades = new Utilidades(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//input[@placeholder='¿Qué estás buscando?']")
    private WebElement inputBuscarSalcoBrand;

    @FindBy(xpath = "//h1[@class='product-name product_name_pdp']")
    private WebElement textNombreProductoSalcoBrand;

    @FindBy(xpath = "//div[@class='normal withoutSbpay']/span")
    private WebElement textPrecioOfertaProductoSalcoBrand;

    @FindBy(xpath = "//div[@class='old-prices']/span")
    private WebElement textPrecioNormalProductoSalcoBrand;

    @FindBy(xpath = "//source[@type='image/webp']/../img")
    private WebElement urlImagenMedicamento;

    public boolean isVisibleInputBuscarMedicamentos(){
        utilidades.waitUntilElementIsVisibleNonThrow(inputBuscarSalcoBrand,30);
        return utilidades.isVisible(inputBuscarSalcoBrand);
    }

    public void sendKeysInputBuscarMedicamentoSalcoBrandPage(String medicamento){
        utilidades.waitUntilElementIsVisible(inputBuscarSalcoBrand);
        inputBuscarSalcoBrand.sendKeys(medicamento);
    }

    public void clickBtnPrimeraOpcionMedicamentoSalcoBrandPage(String medicamento) {
        By selector = By.xpath("(//span[contains(text(),'"+medicamento+"')])[1]");
        utilidades.waitUntilElementIsVisible(selector);
        WebElement element = driver.findElement(selector);
        element.click();
    }

    public String getTextNombreProductoSalcoBrandPage() {
        return textNombreProductoSalcoBrand.getText();
    }

    public String getTextPrecioPromocionProductoSalcoBrandPage() {
        return textPrecioOfertaProductoSalcoBrand.getText();
    }

    public String getTextPrecioNormalProductoSalcoBrandPage() {
        return textPrecioNormalProductoSalcoBrand.getText();
    }

    public String getUrlImagenMedicamento() {
        return urlImagenMedicamento.getAttribute("src");
    }
}
