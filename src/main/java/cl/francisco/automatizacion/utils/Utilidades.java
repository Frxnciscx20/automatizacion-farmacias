package cl.francisco.automatizacion.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

public class Utilidades {

    private final WebDriver driver;
    private final WebDriverWait waitDefault;

    public Utilidades(WebDriver driver) {
        this.driver = driver;
        // Espera global por defecto (60s)
        this.waitDefault = new WebDriverWait(driver, Duration.ofSeconds(160));
    }

    // 🔹 Espera hasta que un WebElement sea visible (lanza excepción si no aparece)
    public void waitUntilElementIsVisible(WebElement element) {
        try {
            waitDefault.until(ExpectedConditions.visibilityOf(element));
        } catch (TimeoutException e) {
            System.err.println("⏰ Elemento no visible dentro de 60s: " + safeElementName(element));
            throw e;
        } catch (StaleElementReferenceException e) {
            System.err.println("♻️ Elemento recargado en el DOM, intentando nuevamente...");
            retryVisibility(element, 3);
        }
    }

    // 🔹 Espera hasta que un elemento (By) sea visible (lanza excepción)
    public void waitUntilElementIsVisible(By selector) {
        try {
            waitDefault.until(ExpectedConditions.visibilityOfElementLocated(selector));
        } catch (TimeoutException e) {
            System.err.println("⏰ Elemento no visible (By) dentro de 60s: " + selector);
            throw e;
        }
    }

    // 🔹 Igual que anterior pero no lanza excepción — WebElement
    public void waitUntilElementIsVisibleNonThrow(WebElement element, int seconds) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(seconds))
                    .until(ExpectedConditions.visibilityOf(element));
        } catch (TimeoutException e) {
            System.out.println("⚠️ Elemento no visible (WebElement) en " + seconds + "s: " + safeElementName(element));
        } catch (StaleElementReferenceException e) {
            System.out.println("♻️ Elemento recargado, reintentando...");
            retryVisibility(element, 2);
        }
    }

    // 🔹 Igual pero con By
    public void waitUntilElementIsVisibleNonThrow(By selector, int seconds) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(seconds))
                    .until(ExpectedConditions.visibilityOfElementLocated(selector));
        } catch (TimeoutException e) {
            System.out.println("⚠️ Elemento no visible (By) en " + seconds + "s: " + selector);
        }
    }

    // 🔹 Verifica visibilidad rápida (sin esperar)
    public boolean isVisible(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    // 🔹 Espera fija (segura y con logging)
    public void esperarAntesDeContinuar() {
        esperarSegundos(45);
    }

    // 🔹 Espera personalizada
    public void esperarSegundos(int segundos) {
        try {
            System.out.println("⏳ Esperando " + segundos + "s antes de continuar...");
            Thread.sleep(segundos * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // 🔁 Reintenta visibilidad para elementos que se recargan en el DOM
    private void retryVisibility(WebElement element, int reintentos) {
        for (int i = 0; i < reintentos; i++) {
            try {
                waitDefault.until(ExpectedConditions.visibilityOf(element));
                System.out.println("✅ Elemento visible tras reintento " + (i + 1));
                return;
            } catch (Exception ignored) {
                esperarSegundos(2);
            }
        }
        System.err.println("❌ No se logró visualizar el elemento tras " + reintentos + " intentos: " + safeElementName(element));
    }

    // 🧩 Nombre seguro para logs
    private String safeElementName(WebElement element) {
        try {
            String desc = element.toString();
            return desc.length() > 120 ? desc.substring(0, 120) + "..." : desc;
        } catch (Exception e) {
            return "[Elemento desconocido]";
        }
    }
}
