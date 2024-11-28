package edu.ftn.iss.eventplanner.controllers;

import edu.ftn.iss.eventplanner.dtos.EventDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
public class EventController {

    // Endpoint za Top 5 događaja
    @GetMapping("/api/events/top5")
    public ResponseEntity<List<EventDTO>> getTop5Events(
            @RequestParam String city) {
        List<EventDTO> topEvents = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            EventDTO event = new EventDTO();
            event.setId((long) i);
            event.setName("Event " + i);
            event.setDescription("Description for Event " + i);
            event.setLocation(city);
            event.setStartDate(LocalDate.now().plusDays(i));
            event.setEndDate(LocalDate.now().plusDays(i + 1));
            topEvents.add(event);
        }
        return ResponseEntity.ok(topEvents);
    }

    // Endpoint za pretragu i filtriranje događaja
    @GetMapping("/api/events/search")
    public ResponseEntity<List<EventDTO>> searchEvents(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String privacyType,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        // Simulacija događaja za pretragu (generiše se 30 događaja)
        List<EventDTO> allEvents = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            EventDTO event = new EventDTO();
            event.setId((long) i);
            event.setName("Event " + i);
            event.setDescription("Description for Event " + i);
            event.setLocation(i % 2 == 0 ? "Belgrade" : "Novi Sad");
            event.setPrivacyType(i % 2 == 0 ? "open" : "closed");
            event.setStartDate(LocalDate.now().plusDays(i));
            event.setEndDate(LocalDate.now().plusDays(i + 1));
            event.setBudget(1000 + i * 10);
            allEvents.add(event);
        }

        // Filtriranje po parametrima
        List<EventDTO> filteredEvents = new ArrayList<>();
        for (EventDTO event : allEvents) {
            if ((name == null || event.getName().toLowerCase().contains(name.toLowerCase())) &&
                    (location == null || event.getLocation().equalsIgnoreCase(location)) &&
                    (privacyType == null || event.getPrivacyType().equalsIgnoreCase(privacyType)) &&
                    (startDate == null || !event.getStartDate().isBefore(startDate)) &&
                    (endDate == null || !event.getEndDate().isAfter(endDate))) {
                filteredEvents.add(event);
            }
        }

        // Paginacija
        int start = page * size;
        int end = Math.min(start + size, filteredEvents.size());
        if (start > filteredEvents.size()) {
            return ResponseEntity.ok(new ArrayList<>());
        }

        List<EventDTO> paginatedEvents = filteredEvents.subList(start, end);

        return ResponseEntity.ok(paginatedEvents);
    }
}
