package cl.francisco.automatizacion.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import java.net.URL;

public class LambdaTestDriver {

    public static WebDriver create() throws Exception {
        // 🔐 Credenciales desde variables de entorno
        String USERNAME = System.getenv("LT_USERNAME");
        String ACCESS_KEY = System.getenv("LT_ACCESS_KEY");

        if (USERNAME == null || ACCESS_KEY == null) {
            throw new IllegalArgumentException("❌ Faltan las variables de entorno LT_USERNAME o LT_ACCESS_KEY");
        }

        String hubURL = "https://" + USERNAME + ":" + ACCESS_KEY + "@hub.lambdatest.com/wd/hub";

        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("browserName", "Chrome");
        caps.setCapability("browserVersion", "latest");
        caps.setCapability("platformName", "Windows 11");

        caps.setCapability("build", "Build 01 - Integración LambdaTest");
        caps.setCapability("name", "Test Farmacias Chile");

        // 🔧 Opcional: resolución de pantalla
        caps.setCapability("resolution", "1920x1080");

        return new RemoteWebDriver(new URL(hubURL), caps);
    }
}
