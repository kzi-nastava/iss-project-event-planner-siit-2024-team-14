package edu.ftn.iss.eventplanner.e2e.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import static edu.ftn.iss.eventplanner.e2e.util.Util.parseCurrency;

public class EventBudgetPage {
    public static final String URL = "http://localhost:4200/events/%d/#Budget";

    private final WebDriver driver;
    private final WebDriverWait wait;

    //@FindBy(xpath = "//article[.//h3[normalize-space()='Budget']]//table")
    //private WebElement table;

    @FindBy(xpath = "//button[contains(@title, 'Add new budget')]")
    private WebElement buttonAddBudgetItem;



    public EventBudgetPage(WebDriver driver, int eventId) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        driver.get(String.format(URL, eventId));
        PageFactory.initElements(driver, this);
    }


    public AddBudgetItemPopup addBudgetItem() {
        wait.until(ExpectedConditions.elementToBeClickable(buttonAddBudgetItem)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("app-add-event-budget-item")));
        return new AddBudgetItemPopup(driver);
    }


    public List<BudgetRow> getBudgetRows() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("table")))
                .findElements(By.cssSelector("tbody tr:not(:last-child)"))
                .stream().map(we -> new BudgetRow(we, driver))
                .toList();
    }


    public Optional<BudgetRow> getBudgetRow(String category) {
        return getBudgetRows().stream()
                .filter(row -> row.getCategory().equalsIgnoreCase(category))
                .findFirst();
    }


    public int getRowCount() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("table")))
                .findElements(By.cssSelector("tbody tr")).size();
    }


    public Double getSpentAmount() {
        var data = getSummaryRow().findElement(By.cssSelector("td:nth-child(3)"));
        return parseCurrency(data.getText());
    }


    public Double getPlannedAmount() {
        var data = getSummaryRow().findElement(By.cssSelector("td:nth-child(2)"));
        return parseCurrency(data.getText());
    }


    private WebElement getSummaryRow() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("table")))
                .findElement(By.cssSelector("tbody tr:last-child"));
    }

}
