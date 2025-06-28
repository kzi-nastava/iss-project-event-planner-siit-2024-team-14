package edu.ftn.iss.eventplanner.controllers;

import edu.ftn.iss.eventplanner.dtos.budget.*;
import edu.ftn.iss.eventplanner.entities.BudgetItem;
import edu.ftn.iss.eventplanner.entities.Event;
import edu.ftn.iss.eventplanner.entities.Solution;
import edu.ftn.iss.eventplanner.services.EventBudgetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping(path = {"/api/events/{eventId:\\d+}/budget"}, produces = MediaType.APPLICATION_JSON_VALUE)
public class EventBudgetController {

    private final EventBudgetService budgetService;
    private final ModelMapper modelMapper;

    public EventBudgetController(EventBudgetService budgetService, ModelMapper modelMapper) {
        this.budgetService = budgetService;
        this.modelMapper = modelMapper;
    }

    // GET organizer[Organizes the event with id 1]@*/api/events/1/budget
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public EventBudgetDTO getEventBudget(
            @PathVariable int eventId
    ) {
        Object budget = budgetService.getBudgetByEventId(eventId);
        return modelMapper.map(budget, EventBudgetDTO.class);
    }

    // POST organizer[Organizes the event with id 1]@*/api/events/1/budget
    @PostMapping
    public ResponseEntity<EventBudgetDTO> planEventBudget(
            @PathVariable int eventId,
            @RequestBody List<PlanBudgetItemDTO> items,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        Object budget = budgetService.planEventBudget(eventId, items.stream().collect(Collectors.toMap(PlanBudgetItemDTO::getCategoryId, PlanBudgetItemDTO::getAmount)) );

        URI location = uriComponentsBuilder
                .path("/api/events/{eventId}/budget")
                .buildAndExpand(eventId)
                .toUri();

        return ResponseEntity
                .created(location)
                .body(modelMapper.map(budget, EventBudgetDTO.class));
    }

    // GET organizer[Organizes the event with id 1]@*/api/events/1/budget/1
    @GetMapping(path = "/{categoryId:\\d+}")
    @ResponseStatus(HttpStatus.OK)
    public EventBudgetItemDTO getEventBudgetItem(
            @PathVariable int eventId,
            @PathVariable int categoryId
    ) {
        Object item = budgetService.getBudgetItemByEventIdAndCategoryId(eventId, categoryId);
        return modelMapper.map(item, EventBudgetItemDTO.class);
    }

    // GET ...*/api/events/1/budget?category=1
    @GetMapping(params = "category")
    @ResponseStatus(HttpStatus.OK)
    public EventBudgetItemDTO getEventBudgetItemQP(
            @PathVariable int eventId,
            @RequestParam int category
    ) {
        return getEventBudgetItem(eventId, category);
    }

    // POST organizer[Organizes the event with id 1]@*/api/events/1/budget/1
    @PostMapping(path = "/{categoryId:\\d+}")
    public ResponseEntity<EventBudgetItemDTO> addEventBudgetItem(
            @PathVariable int eventId,
            @PathVariable int categoryId,
            @Valid @RequestBody CreateBudgetItemDTO itemDTO,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        Object item = budgetService.addEventBudgetItem(eventId, categoryId, itemDTO.getAmount());

        URI location = uriComponentsBuilder
                .path("/api/events/{eventId}/{categoryId}")
                .buildAndExpand(eventId, categoryId)
                .toUri();

        return ResponseEntity
                .created(location)
                .body(modelMapper.map(item, EventBudgetItemDTO.class));
    }

    // PUT organizer[Organizes the event with id 1]@*/api/events/1/budget/1
    @PutMapping(path = "/{categoryId:\\d+}")
    @ResponseStatus(HttpStatus.OK)
    public EventBudgetItemDTO putUpdateEventBudgetItem(
            @PathVariable int eventId,
            @PathVariable int categoryId,
            @RequestBody CreateBudgetItemDTO updateDto
    ) {
        Object item = budgetService.updateEventBudgetItem(eventId, categoryId, updateDto.getAmount());
        return modelMapper.map(item, EventBudgetItemDTO.class);
    }

    // DELETE organizer[Organizes the event with id 1]@*/api/events/1/budget/1
    @DeleteMapping(path = "/{categoryId:\\d+}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEventBudgetItem(
            @PathVariable int eventId,
            @PathVariable int categoryId
    ) {
        budgetService.deleteEventBudgetItem(eventId, categoryId);
    }
}
