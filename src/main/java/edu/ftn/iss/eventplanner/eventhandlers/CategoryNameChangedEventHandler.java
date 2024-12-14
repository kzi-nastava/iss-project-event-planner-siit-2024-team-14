package edu.ftn.iss.eventplanner.eventhandlers;

import edu.ftn.iss.eventplanner.entities.CategoryNameChangedEvent;
import jakarta.validation.constraints.NotNull;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class CategoryNameChangedEventHandler {

    @EventListener
    public void handleCategoryNameChanged(@NotNull CategoryNameChangedEvent event) {
        // TODO: Notify relevant providers
        System.out.printf("Changed the name of category with id %d, from '%s' to '%s'.%n", event.getCategoryId(), event.getOldName(), event.getNewName());
    }

}
