package edu.ftn.iss.eventplanner.e2e.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;


public class LoginPage {
    public static final String URL = "http://localhost:4200/login";


    @FindBy(id = "email")
    private WebElement inputEmail;

    @FindBy(id = "password")
    private WebElement inputPassword;

    @FindBy(xpath = "//form//button[@type='submit']")
    private WebElement buttonSubmit;

    private final WebDriver driver;


    public LoginPage(WebDriver driver) {
        this.driver = driver;
        driver.get(URL);
        PageFactory.initElements(driver, this);
    }



    public void logIn(String email, String password) {
        inputEmail.clear();
        inputPassword.clear();

        inputEmail.sendKeys(email);
        inputPassword.sendKeys(password);
        buttonSubmit.click();

        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.urlContains("home"));
    }

}
