package cl.francisco.automatizacion.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.time.Duration;

public class DriverFactory {

    public static WebDriver createDriver() throws Exception {
        WebDriver driver;
        String USERNAME = System.getenv("LT_USERNAME");
        String ACCESS_KEY = System.getenv("LT_ACCESS_KEY");

        if (USERNAME != null && ACCESS_KEY != null) {
            System.out.println("🌐 Conectando a LambdaTest...");

            DesiredCapabilities caps = new DesiredCapabilities();
            caps.setCapability("browserName", "Chrome");
            caps.setCapability("browserVersion", "latest");
            caps.setCapability("platformName", "Windows 11");
            caps.setCapability("name", "Test Automático - SoloFarmacias");
            caps.setCapability("build", "Build 01 - LambdaTest");

            URL hubUrl = new URL("https://" + USERNAME + ":" + ACCESS_KEY + "@hub.lambdatest.com/wd/hub");
            driver = new RemoteWebDriver(hubUrl, caps);

            System.out.println("✅ Conectado exitosamente a LambdaTest");

        } else {
            System.out.println("⚙️ No se encontraron variables de entorno. Usando Chrome local...");
            WebDriverManager.chromedriver().setup();

            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage");
            options.addArguments("--remote-allow-origins=*");

            driver = new ChromeDriver(options);
            driver.manage().window().maximize();

            System.out.println("✅ Chrome local inicializado correctamente (modo headless)");
        }

        // Espera implícita de 30 segundos
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));

        return driver;
    }
}
