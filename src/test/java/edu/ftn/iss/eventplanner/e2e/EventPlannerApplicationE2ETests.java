package edu.ftn.iss.eventplanner.e2e;

import edu.ftn.iss.eventplanner.e2e.tests.TestBase;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.time.Duration;

import static org.testng.AssertJUnit.assertNotNull;


public class EventPlannerApplicationE2ETests extends TestBase {

    @Test
    void setsUpDriver() {
        assertNotNull(driver);
    }

    @Test
    void ftnRadi() {
        assertNotNull(driver);
        driver.navigate().to("https://www.ftn.uns.ac.rs?utm_source=EventPlanner14");

        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.urlContains("ftn.uns.ac.rs"));
    }

}
