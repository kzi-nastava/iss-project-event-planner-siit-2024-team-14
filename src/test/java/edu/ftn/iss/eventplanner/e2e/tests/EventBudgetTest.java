package edu.ftn.iss.eventplanner.e2e.tests;

import edu.ftn.iss.eventplanner.e2e.pages.BudgetRow;
import edu.ftn.iss.eventplanner.e2e.pages.EventBudgetPage;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.testng.Assert.*;



public class EventBudgetTest extends TestBase.AsAdmin {

    private static final int EVENT_ID = 2;
    private EventBudgetPage budgetPage;



    @BeforeMethod
    public void setUp() {
        budgetPage = new EventBudgetPage(driver, EVENT_ID);
    }



    @Test
    public void deleteBudgetItem_WhenDeleted_IsRemovedFromTheTable() {
        var rows = assertHasBudgetItems();

        var item = rows.get(0);
        int nRows = budgetPage.getRowCount(); // rows.size()
        String category = item.getCategory();
        item.delete();

        assertEquals(budgetPage.getRowCount(), nRows - 1, "Incorrect number of rows in the table");
        assertTrue(
                budgetPage.getBudgetRow(category).isEmpty(),
                "Deleted category '%s' should not be displayed in the table".formatted(category)
        );
    }


    @Test
    public void addBudgetItem_WhenAdded_IsAddedToTheTable() {
        var popup = budgetPage.addBudgetItem();
        var availableCategories = popup.viewAvailableCategories();

        if (availableCategories.isEmpty()) {
            return;
        }

        String category = availableCategories.iterator().next();
        popup.selectCategory(category);
        popup.enterAmount(77_000);
        popup.submit();

        var row = budgetPage.getBudgetRow(category)
                .orElseThrow(() -> new AssertionError("New category '%s' should have been added to the table".formatted(category)));

        assertEquals(row.getPlannedAmount(), 77_000, "Wrong planned amount");
    }


    @Test
    public void whenAddingBudgetItem_PlannedCategories_ShouldNotShowAsAvailable() {
        var plannedCategories = budgetPage.getBudgetRows().stream()
                .map(br -> br.getCategory().trim().toLowerCase())
                .collect(Collectors.toSet());

        var popup = budgetPage.addBudgetItem();
        var availableCategories = popup.viewAvailableCategories().stream()
                .map(String::trim).map(String::toLowerCase)
                .collect(Collectors.toSet());

        assertTrue(Collections.disjoint(plannedCategories, availableCategories), "Should not show planned categories as available");
    }


    @Test
    public void updateBudgetItem_WhenUpdated_IsUpdated() {
        var rows = assertHasBudgetItems();

        var item = rows.get(0);
        String category = item.getCategory();
        item.edit(100_000);

        var updatedItem = budgetPage.getBudgetRow(category)
                .orElseThrow(() -> new AssertionError("Updated category is missing from the table"));

        assertEquals(updatedItem.getPlannedAmount(), 100_000, "Wrong planned amount");
    }


    @Test
    public void updateBudgetItem_WhenUpdated_TotalBudgetIsUpdated() {
        var rows = assertHasBudgetItems();

        var item = rows.get(0);
        Double amount = item.getPlannedAmount();
        Double totalAmount = budgetPage.getPlannedAmount();

        item.edit(amount + 101);

        assertEquals(budgetPage.getPlannedAmount() - totalAmount, 101);
    }


    @Test
    public void deleteBudgetItem_WhenDeleted_TotalBudgetIsUpdated() {
        assertHasBudgetItems();

        var item = budgetPage.getBudgetRows().get(0);
        Double amount = item.getPlannedAmount();
        Double totalAmount = budgetPage.getPlannedAmount();

        item.delete();

        assertEquals(budgetPage.getPlannedAmount(), totalAmount - amount);
    }


    /// It just logs to console that there was an error, maybe we could check that the table is unchanged
    @Test(enabled = false)
    public void deleteBudgetItem_HasHeldSolutions_DoesNotDelete() {}


    @Test
    public void updateBudgetItem_NegativeAmount_DoesNotUpdate() {
        var rows = assertHasBudgetItems();

        var item = rows.get(0);
        String category = item.getCategory();
        Double oldAmount = item.getPlannedAmount();
        item.edit(-1);

        var hopefullyNotUpdatedItem = budgetPage.getBudgetRow(category).orElseThrow();

        assertEquals(
                hopefullyNotUpdatedItem.getPlannedAmount(),
                oldAmount,
                "Budget item amount was updated with invalid value"
        );
    }


    private List<BudgetRow> assertHasBudgetItems() {
        var rows = budgetPage.getBudgetRows();

        if (rows.isEmpty()) {
            fail("There are no budget items for event with id " + EVENT_ID);
        }

        return rows;
    }

}
