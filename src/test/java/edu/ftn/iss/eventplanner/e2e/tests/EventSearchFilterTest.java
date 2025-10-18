package edu.ftn.iss.eventplanner.e2e.tests;

import edu.ftn.iss.eventplanner.e2e.pages.EventsPage;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EventSearchFilterTest {

    private static WebDriver driver;
    private static EventsPage eventsPage;

    @BeforeAll
    public static void setupClass() {
        System.setProperty("webdriver.chrome.driver",
                "C:\\Users\\Milica\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(7));
        driver.manage().window().maximize();
        eventsPage = new EventsPage(driver);
    }

    @AfterAll
    public static void tearDownClass() {
        if (driver != null) driver.quit();
    }

    @BeforeEach
    public void openPage() {
        eventsPage.open();
        eventsPage.waitForAtLeastCards(1, 7);
    }

    // ============================
    // 1. SEARCH TESTS
    // ============================

    @Test
    @Order(1)
    @DisplayName("Pretraga: substring 'horse' - bar jedan rezultat")
    public void testSearchHorse() {
        int before = eventsPage.getCardsCount();
        eventsPage.search("horse");
        eventsPage.waitForCardsCountChange(before, 5);

        Assertions.assertTrue(eventsPage.getCardsCount() > 0, "Očekuju se rezultati za 'horse'");
    }

    @Test
    @Order(2)
    @DisplayName("Negativna pretraga: 'xyz12345' daje 0 rezultata")
    public void testSearchNoResults() {
        eventsPage.search("xyz12345");
        Assertions.assertEquals(0, eventsPage.getCardsCount(), "Za 'xyz12345' ne bi trebalo biti rezultata");
    }

    @Test
    @Order(3)
    @DisplayName("Pretraga case-insensitive: 'HORSE' = 'horse'")
    public void testSearchCaseInsensitive() {
        eventsPage.search("HORSE");
        int upperCount = eventsPage.getCardsCount();

        eventsPage.search("horse");
        int lowerCount = eventsPage.getCardsCount();

        Assertions.assertEquals(upperCount, lowerCount,
                "Rezultati moraju biti isti bez obzira na velika/mala slova");
    }

    @Test
    @Order(4)
    @DisplayName("Prazna pretraga vraća bar jedan događaj")
    public void testEmptySearch() {
        eventsPage.search("");
        Assertions.assertTrue(eventsPage.getCardsCount() > 0,
                "Treba postojati rezultati kada nema filtera");
    }

    // ============================
    // 2. FILTER TESTS
    // ============================

    @Test
    @Order(5)
    @DisplayName("Filtriranje po kategoriji (Party)")
    public void testFilterByCategory() {
        eventsPage.openFilters();
        eventsPage.selectByVisibleText("category", "Party");
        int before = eventsPage.getCardsCount();
        eventsPage.applyFilters();
        eventsPage.waitForCardsCountChange(before, 5);

        Assertions.assertTrue(eventsPage.getCardsCount() > 0);
    }

    @Test
    @Order(6)
    @DisplayName("Filtriranje po gradu (Novi Sad)")
    public void testFilterByCity() {
        eventsPage.openFilters();
        eventsPage.selectByVisibleText("location", "Novi Sad");
        int before = eventsPage.getCardsCount();
        eventsPage.applyFilters();
        eventsPage.waitForCardsCountChange(before, 5);

        List<WebElement> results = eventsPage.eventCards();
        Assertions.assertTrue(results.size() > 0);
        Assertions.assertTrue(results.stream().allMatch(c -> c.getText().contains("Novi Sad")),
                "Svi rezultati moraju biti iz 'Novi Sad'");
    }

    @Test
    @Order(7)
    @DisplayName("Filtriranje: Party + Belgrade -> očekuje se 'Birthday Party'")
    public void testFilterPartyBelgrade() {
        eventsPage.openFilters();
        eventsPage.selectByVisibleText("category", "Party");
        eventsPage.selectByVisibleText("location", "Belgrade");
        int before = eventsPage.getCardsCount();
        eventsPage.applyFilters();
        eventsPage.waitForCardsCountChange(before, 5);

        Assertions.assertTrue(eventsPage.eventCards().stream()
                        .anyMatch(c -> c.getText().toLowerCase().contains("birthday party")),
                "Među rezultatima mora biti 'Birthday Party'");
    }

    // ============================
    // 3. DATE FILTERS
    // ============================

    @Test
    @Order(8)
    @DisplayName("Filtriranje po validnom opsegu datuma 2025")
    public void testFilterByValidDateRange() {
        eventsPage.openFilters();
        eventsPage.setDate("startDate", "2025-01-01");
        eventsPage.setDate("endDate", "2025-12-31");
        eventsPage.applyFilters();

        Assertions.assertTrue(eventsPage.getCardsCount() > 0);
    }

    @Test
    @Order(9)
    @DisplayName("Filtriranje: 2025-10-07 do 2025-10-10 -> EXIT Festival")
    public void testFilterByDateRangeExitFestival() {
        eventsPage.openFilters();
        eventsPage.setDate("startDate", "2025-10-07");
        eventsPage.setDate("endDate", "2025-10-10");
        eventsPage.applyFilters();

        List<WebElement> results = eventsPage.eventCards();
        Assertions.assertEquals(1, results.size());
        Assertions.assertTrue(results.get(0).getText().toLowerCase().contains("exit festival"));
    }

    @Test
    @Order(10)
    @DisplayName("Nevalidan opseg datuma (start > end) -> bez filtriranja")
    public void testInvalidDateRange() {
        int initialCount = eventsPage.getCardsCount();
        eventsPage.openFilters();
        eventsPage.setDate("startDate", "2027-02-01");
        eventsPage.setDate("endDate", "2027-01-01");
        eventsPage.applyFilters();

        Assertions.assertEquals(initialCount, eventsPage.getCardsCount());
    }

    // ============================
    // 4. COMBINED FILTERS
    // ============================

    @Test
    @Order(11)
    @DisplayName("Kombinacija: Belgrade + Party + 2025-09-02 -> Birthday Party")
    public void testCombineAllFilters() {
        eventsPage.openFilters();
        eventsPage.selectByVisibleText("location", "Belgrade");
        eventsPage.selectByVisibleText("category", "Party");
        eventsPage.setDate("startDate", "2025-09-02");
        eventsPage.setDate("endDate", "2025-09-02");
        eventsPage.applyFilters();

        List<WebElement> results = eventsPage.eventCards();
        Assertions.assertEquals(1, results.size());
        Assertions.assertTrue(results.get(0).getText().toLowerCase().contains("birthday party"));
    }

    // ============================
    // 5. PAGINATION
    // ============================

    @Test
    @Order(12)
    @DisplayName("Paginacija - Next radi ako postoji druga strana")
    public void testPaginationNext() {
        String infoBefore = eventsPage.pageInfo().getText();
        if (eventsPage.nextButton().isEnabled()) {
            eventsPage.nextButton().click();
            Assertions.assertNotEquals(infoBefore, eventsPage.pageInfo().getText());
        } else {
            Assertions.assertFalse(eventsPage.nextButton().isEnabled());
        }
    }

    @Test
    @Order(13)
    @DisplayName("Paginacija - 'Previous' disabled na prvoj strani")
    public void testPaginationPreviousDisabled() {
        Assertions.assertFalse(eventsPage.prevButton().isEnabled(),
                "'Previous' mora biti onemogućen na prvoj strani");
    }

    // ============================
    // 6. UI
    // ============================

    @Test
    @Order(14)
    @DisplayName("UI: toggle filter panela radi")
    public void testToggleFilters() {
        Assertions.assertFalse(eventsPage.filterPanel().getAttribute("class").contains("show-filters"));
        eventsPage.openFilters();
        Assertions.assertTrue(eventsPage.filterPanel().getAttribute("class").contains("show-filters"));
    }

    @Test
    @Order(15)
    @DisplayName("Navigacija: klik na 'View More' za 'Horse Riding'")
    public void testViewMoreNavigatesToEventDetails() {
        eventsPage.eventCards().stream()
                .filter(c -> c.getText().toLowerCase().contains("horse riding"))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Nema 'Horse Riding' događaja"))
                .findElement(org.openqa.selenium.By.xpath(".//button[contains(.,'View More')]"))
                .click();

        Assertions.assertTrue(driver.getCurrentUrl().matches(".*/events/\\d+$"),
                "URL mora biti /events/{id}");
    }
}
