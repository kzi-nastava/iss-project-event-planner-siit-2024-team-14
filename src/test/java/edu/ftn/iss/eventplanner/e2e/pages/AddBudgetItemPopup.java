package edu.ftn.iss.eventplanner.e2e.pages;

import java.time.Duration;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;



public class AddBudgetItemPopup {

    @FindBy(name = "category")
    private WebElement selectCategory;

    @FindBy(xpath = "//select[@name='category']/option | //*[@role='option']") // ugly bc of mat-select
    private List<WebElement> selectCategoryOptions;

    @FindBy(name = "amount")
    private WebElement inputAmount;

    @FindBy(css = ".popup-container button[type='submit']")
    private WebElement buttonSubmit;

    @FindBy(css = ".popup-container button[type='reset']")
    private WebElement buttonCancel;

    private final WebDriver driver;



    public AddBudgetItemPopup(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }



    public Set<String> viewAvailableCategories() {
        toggleCategorySelect();

        return selectCategoryOptions.stream()
                .map(we -> we.getText().trim())
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());
    }


    public void toggleCategorySelect() {
        selectCategory.click();
    }


    public void selectCategory(String category) {
        var match = selectCategoryOptions.stream()
                .filter(we -> we.getText().trim().equalsIgnoreCase(category))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Category option not found: " + category));

        match.click();
    }


    public void enterAmount(double amount) {
        inputAmount.clear();
        inputAmount.sendKeys(String.valueOf(amount));
    }


    public void submit() {
        buttonSubmit.click();
        waitUntilPopupCloses();
    }


    public void cancel() {
        buttonCancel.click();
        waitUntilPopupCloses();
    }


    private void waitUntilPopupCloses() {
        new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".popup-overlay")));
    }

}
