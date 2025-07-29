package edu.ftn.iss.eventplanner.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ftn.iss.eventplanner.dtos.budget.CreateBudgetItemDTO;
import edu.ftn.iss.eventplanner.dtos.budget.EventBudgetDTO;
import edu.ftn.iss.eventplanner.dtos.budget.EventBudgetItemDTO;
import edu.ftn.iss.eventplanner.dtos.login.LoginDTO;
import edu.ftn.iss.eventplanner.dtos.login.LoginResponseDTO;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.bind.annotation.ResponseStatus;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Tag("budget")
@Tag("integration")
class EventBudgetControllerIntegrationTest {

    @Autowired TestRestTemplate restTemplate;

    String adminToken, organizerToken;


    @BeforeAll
    void setupAll() {
        adminToken = login("admin@gmail.com", "admin").getToken();
        organizerToken = login("milicabosancic03@gmail.com", "milica123").getToken();
    }

    @BeforeEach
    void setup() {}


    //region Scenario: Checking Event Budget

    @ParameterizedTest
    @ValueSource(ints = {1, 4})
    void getBudgetByEventId_EventWithOrWithoutBudget_ReturnsBudget(int id) {
        var response = get(id);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
    }

    @Test
    void getBudgetByEventId_NonExistentEvent_Returns404() {
        var response = get(14_02_2003);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    private ResponseEntity<EventBudgetDTO> get(int eventId) {
        return restTemplate.getForEntity(
                "/api/events/{id}/budget",
                EventBudgetDTO.class,
                eventId
        );
    }

    //endregion

    //region Scenario: Checking Event Budget for Category

    @Test
    void getBudgetItemByEventIdAndCategoryId_HasBudgetForCategory_ReturnsBudgetItem() {
        var response = get(2, 2);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getCategory().getId());
    }

    @Test
    void getBudgetItemByEventIdAndCategoryId_NoBudgetForCategory_Returns404() {
        var response = get(2, 3);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getBudgetItemByEventIdAndCategoryId_NonExistentCategory_Returns404(@Autowired ObjectMapper objectMapper) {
        var response = restTemplate.getForEntity(
                "/api/events/{id}/budget/{categoryId}",
                String.class,
                1, 14_02_003
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody()); // maybe check that message is "Category not found"
    }


    private ResponseEntity<EventBudgetItemDTO> get(int eventId, int categoryId) {
        return restTemplate.getForEntity(
                "/api/events/{id}/budget/{categoryId}",
                EventBudgetItemDTO.class,
                eventId, categoryId
        );
    }

    //endregion

    //region Scenario: Adding a Budget Item for Category

    @Test
    void addEventBudgetItem_ValidAmount_CreatesItem() {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(adminToken);

        var body = new CreateBudgetItemDTO();
        body.setAmount(770_000);

        var request = new HttpEntity<>(body, headers);

        var location = restTemplate.postForLocation(
                "/api/events/{id}/budget/{categoryId}",
                request,
                1, 4
        );

        assertNotNull(location);

        var response = restTemplate.getForEntity(location, EventBudgetItemDTO.class);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());

        var item = response.getBody();
        assertEquals(4, item.getCategory().getId());
        assertEquals(770_000, item.getAmount(), 0.1);
    }

    @Test
    void addEventBudgetItem_EventWithoutBudget_CreatesNewBudgetWithItem() {
        var response = add(4, 1, 555);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());

        var budgetResponse = get(4);
        assertEquals(HttpStatus.OK, budgetResponse.getStatusCode());
        assertNotNull(budgetResponse.getBody());

        var itemCategoryIds = budgetResponse.getBody().getItems().stream().map(i -> i.getCategory().getId());
        assertThat(itemCategoryIds)
                .contains(response.getBody().getCategory().getId());
    }

    @Test
    void addEventBudgetItem_HasBudgetForCategory_Returns400() {
        var response = add(1, 1, 1234);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void addEventBudgetItem_InvalidAmount_Returns400() {
        var response = add(2, 2, -6);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void addEventBudgetItem_WhenAdded_updatesTotalBudget() {
        var oldBudgetResponse = get(2);
        assertTrue(oldBudgetResponse.getStatusCode().is2xxSuccessful());
        assertNotNull(oldBudgetResponse.getBody());

        double oldBudgetTotal = oldBudgetResponse.getBody().getAmount();

        var response = add(2, 3, 1234);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());

        var newBudgetResponse = get(2);
        assertTrue(newBudgetResponse.getStatusCode().is2xxSuccessful());
        assertNotNull(newBudgetResponse.getBody());

        double newBudgetTotal = newBudgetResponse.getBody().getAmount();
        assertEquals(oldBudgetTotal + response.getBody().getAmount(), newBudgetTotal, 0.1);
    }


    private ResponseEntity<EventBudgetItemDTO> add(int eventId, int categoryId, double amount) {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(adminToken);

        var body = new CreateBudgetItemDTO();
        body.setAmount(amount);

        var request = new HttpEntity<>(body, headers);

        return restTemplate.postForEntity(
                "/api/events/{id}/budget/{categoryId}",
                request,
                EventBudgetItemDTO.class,
                eventId, categoryId
        );
    }

    //endregion

    //region Scenario: Removing a Budget Item for Category

    @Test
    @DirtiesContext
    void deleteEventBudgetItem_HasBudgetForCategory_RemovesItem() {
        var response = delete(1, 1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    @DirtiesContext
    void deleteEventBudgetItem_HasHeldSolutionsForCategory_Returns400() {
        var response = delete(2, 2);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DirtiesContext
    void deleteEventBudgetItem_WhenDeleted_UpdatesBudget() {
        var oldBudgetResponse = get(1);
        assertTrue(oldBudgetResponse.getStatusCode().is2xxSuccessful());
        assertNotNull(oldBudgetResponse.getBody());

        double oldBudgetTotal = oldBudgetResponse.getBody().getAmount();

        var response = delete(1, 1);
        assertTrue(response.getStatusCode().is2xxSuccessful());

        var newBudgetResponse = get(1);
        assertTrue(newBudgetResponse.getStatusCode().is2xxSuccessful());
        assertNotNull(newBudgetResponse.getBody());

        double newBudgetTotal = newBudgetResponse.getBody().getAmount();
        assertTrue(oldBudgetTotal >= newBudgetTotal);
    }

    @Test
    @DirtiesContext
    void deleteEventBudgetItem_NoBudgetForCategory_Returns404() {
        var response = delete(2, 3);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DirtiesContext
    void deleteEventBudgetItem_NotAuthorized_Returns401() {
        var response = restTemplate.exchange(
                "/api/events/{id}/budget/{categoryId}",
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Void.class,
                2, 1
        );

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }


    private ResponseEntity<Void> delete(int eventId, int categoryId) {
        var headers = new HttpHeaders();
        headers.setBearerAuth(adminToken);

        var request = new HttpEntity<>(headers);

        return restTemplate.exchange(
                "/api/events/{id}/budget/{categoryId}",
                HttpMethod.DELETE,
                request,
                Void.class,
                eventId, categoryId
        );
    }

    //endregion

    private @NotNull LoginResponseDTO login(String username, String password) {
        var login = new LoginDTO();
        login.setEmail(username);
        login.setPassword(password);

        var headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        var request = new HttpEntity<>(login, headers);

        var response = restTemplate.postForEntity(
                "/api/users/login",
                request,
                LoginResponseDTO.class
        );

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        return response.getBody();
    }

}