package edu.ftn.iss.eventplanner.services;

import edu.ftn.iss.eventplanner.entities.*;
import edu.ftn.iss.eventplanner.exceptions.BadRequestException;
import edu.ftn.iss.eventplanner.exceptions.InternalServerError;
import edu.ftn.iss.eventplanner.exceptions.NotFoundException;
import edu.ftn.iss.eventplanner.repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventBudgetService {

    private final EventRepository events;
    private final CategoryService categoryService;



    public Budget getBudgetByEventId(int id) {
        return Optional.of(getEventById(id)) // not null
                .map(Event::getBudget)
                .orElseThrow(() -> new NotFoundException("Event does not have a budget"));
    }


    public BudgetItem getBudgetItemByEventIdAndCategoryId(int eventId, int categoryId) {
        return getBudgetByEventId(eventId)
                .getItem(categoryService.getCategoryById(categoryId))
                .orElseThrow(() -> new NotFoundException("Budget item for category not found"));
    }



    @Transactional
    public Budget planEventBudget(int eventId, Map<Integer, Double> categoryIdToBudgetAmount) {
        Event event = getEventById(eventId);

        Optional.ofNullable(event.getBudget())
                .ifPresent(b -> { throw new BadRequestException("Event already has a budget"); });

        Budget budget = new Budget();
        categoryIdToBudgetAmount.forEach((id, amount) ->
            budget.addItem(categoryService.getCategoryById(id), amount)
        );

        event.setBudget(budget);
        return budget;
    }


    @Transactional
    public BudgetItem addEventBudgetItem(int eventId, int categoryId, double amount) {
        var event = getEventById(eventId);

        var budget = event.getBudget();
        if (budget == null)
            event.setBudget(budget = new Budget());

        var category = categoryService.getCategoryById(categoryId);
        if (budget.getItem(category).isPresent())
            throw new BadRequestException("Budget plan for '" + category.getName() + "' already exists");

        budget.addItem(category, amount);
        return budget.getItem(category)
                .orElseThrow(InternalServerError::new);
    }


    @Transactional
    public BudgetItem updateEventBudgetItem(int eventId, int categoryId, double amount) {
        BudgetItem item = getBudgetItemByEventIdAndCategoryId(eventId, categoryId);
        item.setAmount(amount);
        return item;
    }


    @Transactional
    public void deleteEventBudgetItem(int eventId, int categoryId) {
        BudgetItem item = getBudgetItemByEventIdAndCategoryId(eventId, categoryId);

        if (item.getSpent() != 0) {
            throw new BadRequestException("Cannot delete budget item because there are held solutions for its category");
        }

        Optional.of(item.getBudget())
                .map(Budget::getItems)
                .ifPresent(items -> items.remove(item));
    }



    private Event getEventById(int id) {
        return events.findById(id)
                .orElseThrow(() -> new NotFoundException("Event not found"));
    }



    void productPurchasedHandler() {
    }

}
