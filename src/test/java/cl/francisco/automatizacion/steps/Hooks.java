package cl.francisco.automatizacion.steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.openqa.selenium.WebDriver;
import cl.francisco.automatizacion.driver.DriverFactory;

public class Hooks {

    public static WebDriver driver;

    @Before
    public void setUp() {
        try {
            driver = DriverFactory.createDriver();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("❌ Error al inicializar el WebDriver: " + e.getMessage());
        }
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            System.out.println("🧹 Navegador cerrado correctamente");
        }
    }

    public static WebDriver getDriver() {
        return driver;
    }
}
