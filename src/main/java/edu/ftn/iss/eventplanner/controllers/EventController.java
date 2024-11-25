package edu.ftn.iss.eventplanner.controllers;
import edu.ftn.iss.eventplanner.dtos.EventDTO;
import edu.ftn.iss.eventplanner.model.Event;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @GetMapping("/top/{city}")
    public ResponseEntity<List<EventDTO>> getTopEvents(@PathVariable String city) {
        if (city == null || city.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        // Simulacija dobavljanja događaja iz baze
        List<Event> eventsFromDatabase = getMockEvents(city);
        List<EventDTO> topEvents = eventsFromDatabase.stream()
                .map(event -> new EventDTO(
                        event.getName(),
                        event.getDescription(),
                        event.getLocation(),
                        event.getStartDate().toString(),
                        event.getEndDate().toString()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(topEvents);
    }

    private List<Event> getMockEvents(String city) {
        // Simulirani podaci
        return List.of(
                new Event(1L, "Event 1", "Description 1", 100, "PUBLIC", city, null, null, 0.0, null),
                new Event(2L, "Event 2", "Description 2", 200, "PUBLIC", city, null, null, 0.0, null),
                new Event(3L, "Event 3", "Description 3", 150, "PRIVATE", city, null, null, 0.0, null),
                new Event(4L, "Event 4", "Description 4", 50, "PUBLIC", city, null, null, 0.0, null),
                new Event(5L, "Event 5", "Description 5", 300, "PUBLIC", city, null, null, 0.0, null)
        );
    }
}
