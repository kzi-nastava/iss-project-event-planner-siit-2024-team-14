package edu.ftn.iss.eventplanner.e2e.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static edu.ftn.iss.eventplanner.e2e.util.Util.parseCurrency;

public class BudgetRow {

    private final WebDriverWait wait;
    private final WebElement row;
    private final String _category;



    public BudgetRow(WebElement row, WebDriver driver) {
        this.row = row;
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        _category = row.findElement(By.cssSelector("td:nth-child(1)")).getText().trim();
    }



    public String getCategory() {
        return _category;
    }


    public Double getSpentAmount() {
        var data = row.findElement(By.cssSelector("td:nth-child(3)"));
        return parseCurrency(data.getText());
    }


    public Double getPlannedAmount() {
        var data = row.findElement(By.cssSelector("td:nth-child(2)"));
        return parseCurrency(data.getText());
    }


    public void delete() {
        row.findElement(By.cssSelector("button[title*='Remove']")).click();
        wait.until(ExpectedConditions.stalenessOf(row));
    }


    public void edit(double newAmount) {
        row.findElement(By.cssSelector("button[title*='Edit']")).click();

        var input = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("td:nth-child(2) input[type='number']"))
        );

        // when `WebElement::clear` is used it loses focus and leaves edit mode
        input.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        input.sendKeys(String.valueOf(newAmount));
        input.sendKeys(Keys.ENTER);

        wait.until(ExpectedConditions.invisibilityOf(input));
    }

}
