package edu.ftn.iss.eventplanner.controllers;

import edu.ftn.iss.eventplanner.dtos.eventType.EventTypeDTO;
import edu.ftn.iss.eventplanner.dtos.eventType.UpdatedEventTypeDTO;
import edu.ftn.iss.eventplanner.dtos.registration.RegisterResponseDTO;
import edu.ftn.iss.eventplanner.entities.EventType;
import edu.ftn.iss.eventplanner.repositories.EventTypeRepository;
import edu.ftn.iss.eventplanner.services.EventTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/event-types")
public class EventTypeController {

    @Autowired
    private EventTypeService eventTypeService;

    @Autowired
    private EventTypeRepository eventTypeRepository;

    @GetMapping("/get-all-categories")
    public ResponseEntity<List<EventTypeDTO>> getAllEventTypesWithCategories() {
        List<EventTypeDTO> eventTypes = eventTypeService.getAllEventTypesWithCategories();
        return ResponseEntity.ok(eventTypes);
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> create(@RequestBody EventTypeDTO dto) {
        Optional<EventType> existingEventType = eventTypeRepository.findByName(dto.getName());

        if (existingEventType.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "An event type with this name already exists."));
        }

        eventTypeService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Event type created successfully."));
    }

    @PutMapping("/update")
    public ResponseEntity<UpdatedEventTypeDTO> update(@RequestBody EventTypeDTO updateEventType) {
        Integer id = updateEventType.getId();

        UpdatedEventTypeDTO updatedEventType = eventTypeService.update(id, updateEventType);

        return ResponseEntity.ok(updatedEventType);
    }

    @PutMapping("/de-activate/{id}")
    public ResponseEntity<EventType> updateEventTypeStatus(@PathVariable("id") int id) {
        EventType updatedEvent = eventTypeService.activateOrDeactivate(id);
        if (updatedEvent != null) {
            return ResponseEntity.ok(updatedEvent); // Return updated event with status 200 OK
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Return 404 if event not found
        }
    }
}
