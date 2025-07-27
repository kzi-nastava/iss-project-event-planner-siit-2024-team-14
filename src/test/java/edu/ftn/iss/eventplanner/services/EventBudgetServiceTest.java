package edu.ftn.iss.eventplanner.services;

import edu.ftn.iss.eventplanner.entities.Budget;
import edu.ftn.iss.eventplanner.entities.SolutionCategory;
import edu.ftn.iss.eventplanner.exceptions.BadRequestException;
import edu.ftn.iss.eventplanner.exceptions.InternalServerError;
import edu.ftn.iss.eventplanner.exceptions.NotFoundException;
import edu.ftn.iss.eventplanner.repositories.EventRepository;
import edu.ftn.iss.eventplanner.entities.Event;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@Tag("unit")
@Tag("budget")
class EventBudgetServiceTest {

    static final int
            EVENT_WITH_BUDGET_ID = 1,
            EVENT_WITHOUT_BUDGET_ID = 2,
            NON_EXISTENT_EVENT_ID = 14_02_2003,
            CATEGORY1_ID = 1,
            CATEGORY2_ID = 2,
            CATEGORY_WITHOUT_BUDGET_PLAN_ID = 3,
            NON_EXISTENT_CATEGORY_ID = 14_02_2003;

    static final double
            CATEGORY1_BUDGET_AMOUNT = 100,
            CATEGORY2_BUDGET_AMOUNT = 200;

    Event eventWithBudget, eventWithoutBudget;
    SolutionCategory category1, category2, categoryWithoutBudgetPlan;

    @Mock EventRepository eventRepository;

    @Mock CategoryService categoryService;

    /// # SUT
    @InjectMocks EventBudgetService budgetService;



    //region Scenario: Checking Event Budget

    @Test
    void getBudgetByEventId_EventWithBudget_ReturnsBudget() {
        var budget = budgetService.getBudgetByEventId(EVENT_WITH_BUDGET_ID);

        assertNotNull(budget);
        assertEquals(2, budget.getItems().size());
        assertEquals(CATEGORY1_BUDGET_AMOUNT + CATEGORY2_BUDGET_AMOUNT, budget.getAmount());

        verify(eventRepository, times(1)).findById(EVENT_WITH_BUDGET_ID);
    }

    @Test
    void getBudgetByEventId_EventWithoutBudget_CreatesNewBudget() {
        var budget = budgetService.getBudgetByEventId(EVENT_WITHOUT_BUDGET_ID);
        assertNotNull(budget);
        assertTrue(budget.getItems().isEmpty());
    }

    @Test
    void getBudgetByEventId_NonExistentEvent_ThrowsNotFound() {
        assertThrows(NotFoundException.class,
                () -> budgetService.getBudgetByEventId(NON_EXISTENT_EVENT_ID)
        );
    }


    @Test
    void getBudgetByEventId_UnexpectedError_ThrowsInternalServerError() {
        var err = new RuntimeException("some unexpected db error");
        when(eventRepository.findById(anyInt()))
                .thenThrow(err);

        var ex = assertThrows(RuntimeException.class,
                () -> budgetService.getBudgetByEventId(1)
        );

        assertNotSame(ex, err, "Method did not handle an unexpected error");
        assertInstanceOf(InternalServerError.class, ex);
    }

    //endregion

    //region Scenario: Checking Event Budget for Category

    @Test
    void getBudgetItemByEventIdAndCategoryId_HasBudgetForCategory_ReturnsBudgetItem() {
        var item = budgetService.getBudgetItemByEventIdAndCategoryId(EVENT_WITH_BUDGET_ID, CATEGORY1_ID);
        assertNotNull(item);
        assertEquals(item.getCategory(), category1);

        verify(eventRepository, times(1)).findById(EVENT_WITH_BUDGET_ID);
        verify(categoryService, times(1)).getCategoryById(CATEGORY1_ID);
    }

    @Test
    void getBudgetItemByEventIdAndCategoryId_NoBudgetForCategory_ThrowsNotFound() {
        assertThrows(NotFoundException.class,
                () -> budgetService.getBudgetItemByEventIdAndCategoryId(EVENT_WITH_BUDGET_ID, CATEGORY_WITHOUT_BUDGET_PLAN_ID)
        );
    }

    @Test
    void getBudgetItemByEventIdAndCategoryId_NonExistentCategory_ThrowsNotFound() {
        var notFoundEx = assertThrows(NotFoundException.class,
                () -> budgetService.getBudgetItemByEventIdAndCategoryId(EVENT_WITH_BUDGET_ID, NON_EXISTENT_CATEGORY_ID)
            );

        assertEquals("Category not found", notFoundEx.getMessage());
    }

    //endregion

    //region Scenario: Adding a Budget Item for Category

    @Test
    void addEventBudgetItem_ValidAmount_addsBudgetItem() {
        var item = budgetService.addEventBudgetItem(EVENT_WITH_BUDGET_ID, CATEGORY_WITHOUT_BUDGET_PLAN_ID, 555);

        assertNotNull(item);
        assertEquals(555, item.getAmount());
    }

    @Test
    void addEventBudgetItem_EventWithoutBudget_createsNewBudgetWithItem() {
        var item = budgetService.addEventBudgetItem(EVENT_WITHOUT_BUDGET_ID, CATEGORY1_ID, CATEGORY1_BUDGET_AMOUNT);

        assertNotNull(item);
        assertSame(item.getBudget(), eventWithoutBudget.getBudget());
        assertNotNull(eventWithoutBudget.getBudget());
    }

    @Test
    void addEventBudgetItem_WhenAdded_updatesTotalBudget() {
        double oldBudgetTotal = eventWithBudget.getBudget().getAmount();
        var item = budgetService.addEventBudgetItem(EVENT_WITH_BUDGET_ID, CATEGORY_WITHOUT_BUDGET_PLAN_ID, 555);

        assertNotNull(item);
        assertEquals(oldBudgetTotal + 555, eventWithBudget.getBudget().getAmount());
    }

    @Test
    void addEventBudgetItem_InvalidAmount_ThrowsBadRequestException() {
        assertThrows(BadRequestException.class,
                () -> budgetService.addEventBudgetItem(EVENT_WITH_BUDGET_ID, CATEGORY1_ID, -1)
        );
    }

    @Test
    void addEventBudgetItem_HasBudgetForCategory_ThrowsBadRequest() {
        assertThrows(BadRequestException.class,
                () -> budgetService.addEventBudgetItem(EVENT_WITH_BUDGET_ID, CATEGORY1_ID, 555)
        );
    }

    //endregion

    //region Scenario: Updating a Budget Item for Category

    @Test
    void updateEventBudgetItem_ValidAmount_updatesBudgetItem() {
        var item = budgetService.updateEventBudgetItem(EVENT_WITH_BUDGET_ID, CATEGORY2_ID, 555);

        assertNotNull(item);
        assertEquals(eventWithBudget.getBudget(), item.getBudget());
        assertEquals(555, item.getAmount());
    }

    @Test
    void updateEventBudgetItem_WhenUpdated_updatesTotalBudget() {
        double oldBudgetTotal = eventWithBudget.getBudget().getAmount();
        var item = budgetService.updateEventBudgetItem(EVENT_WITH_BUDGET_ID, CATEGORY2_ID, CATEGORY2_BUDGET_AMOUNT + 100);

        assertEquals(oldBudgetTotal + 100, eventWithBudget.getBudget().getAmount());
    }

    @Test
    void updateEventBudgetItem_InvalidAmount_throwsBadRequestException() {
        assertThrows(BadRequestException.class,
                () -> budgetService.updateEventBudgetItem(EVENT_WITH_BUDGET_ID, CATEGORY2_ID, -1)
        );
    }

    //endregion

    //region Scenario: Removing a Budget Item for Category

    @Test
    void deleteEventBudgetItem_HasBudgetForCategory_RemovesItem() {
        budgetService.deleteEventBudgetItem(EVENT_WITH_BUDGET_ID, CATEGORY1_ID);

        assertThat(eventWithBudget.getBudget().getItems())
                .noneMatch(item -> item.getCategory().equals(category1));
    }

    @Test
    void deleteEventBudgetItem_WhenDeleted_UpdatesTotalBudget() {
        double oldBudgetTotal = eventWithBudget.getBudget().getAmount();
        budgetService.deleteEventBudgetItem(EVENT_WITH_BUDGET_ID, CATEGORY1_ID);

        assertEquals(oldBudgetTotal - CATEGORY1_BUDGET_AMOUNT, eventWithBudget.getBudget().getAmount());
    }

    @Test
    @Disabled("Test not implemented yet")
    void deleteEventBudgetItem_HasHeldSolutionsForCategory_ThrowsBadRequest() {

    }

    @Test
    void deleteEventBudgetItem_NoBudgetForCategory_ThrowsNotFound() {
        assertThrows(NotFoundException.class,
                () -> budgetService.deleteEventBudgetItem(EVENT_WITH_BUDGET_ID, CATEGORY_WITHOUT_BUDGET_PLAN_ID)
        );
    }

    //endregion

    //region Scenario: Planning Event's Budget

    @ParameterizedTest
    @MethodSource("provideBudgetPlans")
    void planEventBudget_CategoriesExistValidAmounts_CreatesBudget(@NotNull Map<Integer, Double> plan) {
        var budget = budgetService.planEventBudget(EVENT_WITHOUT_BUDGET_ID, plan);
        assertNotNull(budget);

        double expectedTotal = plan.values().stream().mapToDouble(Double::doubleValue).sum();
        assertEquals(expectedTotal, budget.getAmount());

        assertEquals(
                plan.keySet(),
                budget.getItems().stream()
                        .map(item -> item.getCategory().getId())
                        .collect(Collectors.toSet()),
                "Budget does not contain all items, or contains some not in, the provided plan"
        );
    }

    @Test
    void planEventBudget_EventHasBudget_ThrowsBadRequest() {
        assertThrows(BadRequestException.class,
                () -> budgetService.planEventBudget(EVENT_WITH_BUDGET_ID, Map.of())
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidBudgetPlans")
    void planEventBudget_NonExistentCategory_ThrowsNotFoundWithoutCreatingBudget(Map<Integer, Double> plan) {
        assertThrows(NotFoundException.class,
                () -> budgetService.planEventBudget(EVENT_WITHOUT_BUDGET_ID, plan)
                );
    }

    // should probably add for invalid amounts

    static Stream<Map<Integer, Double>> provideBudgetPlans() {
        return Stream.of(
                Map.of(),
                Map.of(
                        CATEGORY1_ID, CATEGORY1_BUDGET_AMOUNT,
                        CATEGORY2_ID, CATEGORY2_BUDGET_AMOUNT
                )
        );
    }

    static Stream<Map<Integer, Double>> provideInvalidBudgetPlans() {
        return Stream.of(
                Map.of(NON_EXISTENT_CATEGORY_ID, 555d),
                Map.of(
                        CATEGORY1_ID, CATEGORY1_BUDGET_AMOUNT,
                        NON_EXISTENT_CATEGORY_ID, 555d
                )
        );
    }

    //endregion

    @BeforeEach
    void setUp() {
        eventWithBudget = new Event();
        eventWithBudget.setId(EVENT_WITH_BUDGET_ID);

        eventWithoutBudget = new Event();
        eventWithBudget.setId(EVENT_WITHOUT_BUDGET_ID);

        category1 = new SolutionCategory();
        category1.setId(CATEGORY1_ID);

        category2 = new SolutionCategory();
        category2.setId(CATEGORY2_ID);

        categoryWithoutBudgetPlan = new SolutionCategory();
        categoryWithoutBudgetPlan.setId(CATEGORY_WITHOUT_BUDGET_PLAN_ID);

        var budget = new Budget(eventWithBudget);
        budget.addItem(category1, CATEGORY1_BUDGET_AMOUNT);
        budget.addItem(category2, CATEGORY2_BUDGET_AMOUNT);
        eventWithBudget.setBudget(budget);

        lenient().when(eventRepository.findById(EVENT_WITH_BUDGET_ID))
                .thenReturn(Optional.of(eventWithBudget));
        lenient().when(eventRepository.findById(EVENT_WITHOUT_BUDGET_ID))
                .thenReturn(Optional.of(eventWithoutBudget));
        lenient().when(eventRepository.findById(NON_EXISTENT_EVENT_ID))
                .thenReturn(Optional.empty());

        lenient().when(categoryService.getCategoryById(CATEGORY1_ID))
                .thenReturn(category1);
        lenient().when(categoryService.getCategoryById(CATEGORY2_ID))
                .thenReturn(category2);
        lenient().when(categoryService.getCategoryById(CATEGORY_WITHOUT_BUDGET_PLAN_ID))
                .thenReturn(categoryWithoutBudgetPlan);
        lenient().when(categoryService.getCategoryById(NON_EXISTENT_CATEGORY_ID))
                .thenThrow(new NotFoundException("Category not found"));
    }

}