package cl.francisco.automatizacion.pages;

import cl.francisco.automatizacion.utils.Utilidades;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class FarmaciasAhumadaPage {

    private WebDriver driver;
    private Utilidades utilidades;

    public FarmaciasAhumadaPage(WebDriver driver) {
        this.driver = driver;
        this.utilidades = new Utilidades(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy (xpath = "(//form[@role='search'])[1]/input[1]")
    private WebElement inputBuscarMedicamento;

    @FindBy (xpath =  "//li[@id='product-0']/a")
    private WebElement btnPrimeraOpcionMedicamento;

    @FindBy (xpath = "//h1[@class='product-name']")
    private WebElement textNombreProducto;

    @FindBy (xpath = "(//div[@class='price'])[1]")
    private WebElement textPrecioPromocionProducto;

    @FindBy (xpath = "//span[@class='strike-through list text-decoration-none']")
    private WebElement textPrecioNormalProducto;

    @FindBy (xpath = "//div[normalize-space(text())='Uso de cookies']")
    private WebElement textUsoDeCookies;

    @FindBy(xpath = "//button[normalize-space(text())='Sì']")
    private WebElement btnSiUsoDeCookies;

    @FindBy(xpath = "(//button[@class='close p-0 m-0'])[2]")
    private WebElement btnCerrarAlertaOferta;

    @FindBy(xpath = "//span[@class='swiper-slide swiper-slide-active']/img")
    private WebElement urlImagenMedicamento;

    public void sendKeysInputBuscarMedicamento(String medicamento){
        utilidades.waitUntilElementIsVisible(inputBuscarMedicamento);
        inputBuscarMedicamento.sendKeys(medicamento);
    }

    public void clickBtnPrimeraOpcionMedicamento(){
        utilidades.waitUntilElementIsVisible(btnPrimeraOpcionMedicamento);
        btnPrimeraOpcionMedicamento.click();
    }

    public String getTextNombreProducto() {
        return textNombreProducto.getText();
    }

    public String getTextPrecioPromocionProducto() {
        return textPrecioPromocionProducto.getText();
    }

    public String getTextPrecioNormalProducto() {
        return textPrecioNormalProducto.getText();
    }

    public boolean isVisibleTextoUsoDeCookies(){
        utilidades.waitUntilElementIsVisibleNonThrow(textUsoDeCookies,10);
        return utilidades.isVisible(textUsoDeCookies);
    }

    public void clickBtnSiUsoDeCookies(){
        utilidades.waitUntilElementIsVisible(btnSiUsoDeCookies);
        btnSiUsoDeCookies.click();
    }

    public boolean isVisibleBtnCerrarAlertaOferta(){
        utilidades.waitUntilElementIsVisibleNonThrow(btnCerrarAlertaOferta,15);
        return utilidades.isVisible(btnCerrarAlertaOferta);
    }

    public void clickBtnCerrarAlertaOferta(){
        utilidades.waitUntilElementIsVisible(btnCerrarAlertaOferta);
        btnCerrarAlertaOferta.click();
    }

    public String getUrlImagenMedicamento() {
        return urlImagenMedicamento.getAttribute("src");
    }

}
