package edu.ftn.iss.eventplanner.services;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
class ProductServiceTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    //region Scenario: Purchasing a Product

    @Test
    @Tag("budget")
    @Disabled("Not implemented yet")
    void purchaseProduct_WhenPurchasedInExistentCategory_UpdatesBudget() {}

    @Test
    @Tag("budget")
    @Disabled("Not implemented yet")
    void purchaseProduct_WhenPurchasedInNonExistentCategory_AddsBudgetItemForCategory() {}

    @Test
    @Tag("budget")
    @Disabled("Not implemented yet")
    void purchaseProduct_ExceedsBudgetPlan_throwsBadRequestException() {}

    //endregion

}