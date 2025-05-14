package cl.francisco.automatizacion.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

public class Utilidades {
    private WebDriver driver;

    public Utilidades(WebDriver driver) {
        this.driver = driver;
    }

    // Versión 1: Recibe WebElement
    public void waitUntilElementIsVisible(WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    // Versión 2: Recibe By
    public void waitUntilElementIsVisible(By selector) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(selector));
    }


    // Versión 1: WebElement + tiempo personalizado
    public void waitUntilElementIsVisibleNonThrow(WebElement element, int seconds) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
            wait.until(ExpectedConditions.visibilityOf(element));
        } catch (Exception e) {
            System.out.println("Elemento no visible (WebElement) dentro del tiempo esperado: " + e.getMessage());
        }
    }

    // Versión 2: By selector + tiempo personalizado
    public void waitUntilElementIsVisibleNonThrow(By selector, int seconds) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
            wait.until(ExpectedConditions.visibilityOfElementLocated(selector));
        } catch (Exception e) {
            System.out.println("Elemento no visible (By) dentro del tiempo esperado: " + e.getMessage());
        }
    }


    public boolean isVisible(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
