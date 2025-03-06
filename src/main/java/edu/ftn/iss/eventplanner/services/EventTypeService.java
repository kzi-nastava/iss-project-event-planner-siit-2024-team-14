package edu.ftn.iss.eventplanner.services;

import edu.ftn.iss.eventplanner.entities.EventType;
import edu.ftn.iss.eventplanner.exceptions.NotFoundException;
import edu.ftn.iss.eventplanner.repositories.EventTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventTypeService {

    private final EventTypeRepository eventTypes;


    @Autowired
    public EventTypeService(EventTypeRepository eventTypes) {
        this.eventTypes = eventTypes;
    }


    public EventType getEventTypeById(int id) {
        return eventTypes.findById(id)
                .orElseThrow(() -> new NotFoundException("Event type not found"));
    }
}
