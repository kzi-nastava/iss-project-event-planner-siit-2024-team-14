package edu.ftn.iss.eventplanner.e2e.tests;

import edu.ftn.iss.eventplanner.e2e.pages.LoginPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;

import java.time.Duration;


public class TestBase {
    protected static WebDriver driver;

    @BeforeSuite
    public void initializeWebDriver() {
        var browser = System.getProperty("browser", "firefox");

        switch (browser) {
            case "chrome":
            {
                System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
                driver = new ChromeDriver();
                break;
            }
            case "firefox":
            {
                System.setProperty("webdriver.gecko.driver", "geckodriver.exe");
                driver = new FirefoxDriver();
                break;
            }
            default:
            {
                throw new IllegalArgumentException("Unsupported browser: " + browser);
            }
        }

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
    }

    @AfterSuite
    public void quitDriver() {
        driver.quit();
    }



    public static class AsAdmin extends TestBase {

        @BeforeClass
        public void loginAdmin() {
            new LoginPage(driver)
                    .logIn("admin@gmail.com", "admin");
        }

    }

    public static class AsOrganizer extends TestBase {

        @BeforeClass
        public void loginOrganizer() {
            new LoginPage(driver)
                    .logIn("milicabosancic03@gmail.com", "ana123");
        }

    }

    public static class AsProvider extends TestBase {

        @BeforeClass
        public void loginProvider() {
            new LoginPage(driver)
                    .logIn("milicabosancic1612@gmail.com", "securepassword");
        }

    }

}
