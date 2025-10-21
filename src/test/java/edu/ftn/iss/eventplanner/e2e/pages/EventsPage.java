package edu.ftn.iss.eventplanner.e2e.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class EventsPage {
    private WebDriver driver;
    private WebDriverWait wait;
    private final String baseUrl = "http://localhost:4200/events";

    public EventsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    // ===== NAVIGATION =====
    /** Stari potpis (koristi interni baseUrl) */
    public void open() {
        driver.get(baseUrl);
        waitForInitialLoad();
    }

    /** Novi overload da test može da prosledi baseUrl (npr. "http://localhost:4200") */
    public void open(String appBaseUrl) {
        String url = appBaseUrl.endsWith("/events") ? appBaseUrl : appBaseUrl + "/events";
        driver.get(url);
        waitForInitialLoad();
    }

    // ===== ELEMENTS =====
    public WebElement searchInput() {
        return driver.findElement(By.cssSelector(".search-input"));
    }

    public WebElement filterIcon() {
        return driver.findElement(By.cssSelector(".filter-icon"));
    }

    public WebElement filterPanel() {
        return driver.findElement(By.cssSelector(".filter-options"));
    }

    public List<WebElement> eventCards() {
        return driver.findElements(By.cssSelector(".event-card"));
    }

    public WebElement applyFiltersButton() {
        return driver.findElement(By.xpath("//button[contains(text(),'Apply Filters')]"));
    }

    public WebElement nextButton() {
        return driver.findElement(By.xpath("//button[text()='Next']"));
    }

    public WebElement prevButton() {
        return driver.findElement(By.xpath("//button[text()='Previous']"));
    }

    public WebElement pageInfo() {
        return driver.findElement(By.cssSelector(".pagination span"));
    }

    // ===== ACTIONS =====
    public void search(String text) {
        WebElement input = searchInput();
        int old = getCardsCount();
        input.clear();
        input.sendKeys(text);
        input.sendKeys(Keys.ENTER);
        waitForCardsCountChange(old, 5);
    }

    public void setDate(String id, String value) {
        WebElement el = driver.findElement(By.id(id));
        el.click();
        el.clear();
        String[] parts = value.split("-");
        String formatted = parts[1] + "/" + parts[2] + "/" + parts[0]; // mm/dd/yyyy
        el.sendKeys(formatted);
        el.sendKeys(Keys.TAB);
    }

    public void selectByVisibleText(String selectId, String text) {
        new Select(driver.findElement(By.id(selectId))).selectByVisibleText(text);
    }

    public void openFilters() {
        filterIcon().click();
        wait.until(d -> filterPanel().getAttribute("class").contains("show-filters"));
    }

    public void applyFilters() {
        int old = getCardsCount();
        applyFiltersButton().click();
        waitForCardsCountChange(old, 5);
    }

    public int getCardsCount() {
        return eventCards().size();
    }

    public void waitForCardsCountChange(int oldCount, int timeoutSeconds) {
        new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
                .until(d -> eventCards().size() != oldCount);
    }

    public void waitForAtLeastCards(int min, int timeoutSeconds) {
        new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
                .until(d -> eventCards().size() >= min);
    }

    // ===== KOMPATIBILNE METODE ZA TVOJ TEST =====
    /** Dugme "Add Event" / "Create" */
    public void clickAddEvent() {
        By by = By.cssSelector("[data-testid='add-event']");
        if (!exists(by)) {
            by = By.xpath("//button[contains(.,'Add Event') or contains(.,'Create')]");
        }
        wait.until(ExpectedConditions.elementToBeClickable(by)).click();
    }

    /** Provera da li lista kartica sadrži naziv događaja */
    public boolean isEventInList(String name) {
        return eventCards().stream().anyMatch(c -> c.getText() != null && c.getText().contains(name));
    }

    // ===== POMOĆNE =====
    private void waitForInitialLoad() {
        // sačekaj da se pojave kartice ili prazan state
        wait.until(d -> !eventCards().isEmpty() || exists(By.cssSelector(".empty-state")));
    }

    private boolean exists(By by) {
        return !driver.findElements(by).isEmpty();
    }
}
