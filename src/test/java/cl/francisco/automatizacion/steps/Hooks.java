package cl.francisco.automatizacion.steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

public class Hooks {

    public static WebDriver driver;

    @Before
    public void setUp() {
        // 👉 Ruta exacta de tu msedgedriver
        System.setProperty("webdriver.edge.driver", "C:\\Users\\Francisco\\Documents\\Drivers\\msedgedriver.exe");

        EdgeOptions options = new EdgeOptions();
        options.addArguments("--remote-allow-origins=*");

        driver = new EdgeDriver(options);
        driver.manage().window().maximize();
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    public static WebDriver getDriver() {
        return driver;
    }
}
