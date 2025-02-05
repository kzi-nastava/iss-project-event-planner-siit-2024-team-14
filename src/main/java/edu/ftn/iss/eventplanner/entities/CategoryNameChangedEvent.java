package edu.ftn.iss.eventplanner.entities;

import lombok.Getter;

@Getter
public class CategoryNameChangedEvent {
    private final int categoryId;
    private final String oldName, newName;

    public CategoryNameChangedEvent(int categoryId, String oldName, String newName) {
        this.categoryId = categoryId;
        this.oldName = oldName;
        this.newName = newName;
    }
}
