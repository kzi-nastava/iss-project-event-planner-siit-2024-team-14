package edu.ftn.iss.eventplanner.e2e.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;



public class LoginPage {
    public static final String URL = "http://localhost:4200/login";


    @FindBy(id = "email")
    private WebElement inputEmail;

    @FindBy(id = "password")
    private WebElement inputPassword;

    @FindBy(xpath = "//form//button[type='submit']")
    private WebElement buttonSubmit;



    public LoginPage(WebDriver driver) {
        driver.get(URL);
        PageFactory.initElements(driver, this);
    }



    public void logIn(String email, String password) {
        inputEmail.clear();
        inputPassword.clear();

        inputEmail.sendKeys(email);
        inputPassword.sendKeys(password);
        buttonSubmit.click();
    }

}
