package edu.ftn.iss.eventplanner.controllers;

import edu.ftn.iss.eventplanner.dtos.EventDTO;
import edu.ftn.iss.eventplanner.entities.Event;
import edu.ftn.iss.eventplanner.services.EventService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    // Endpoint za Top 5 događaja
    @GetMapping("/api/events/top5")
    public ResponseEntity<List<EventDTO>> getTop5Events(
            @RequestParam String city) {
        return ResponseEntity.ok(eventService.getTop5Events(city));
    }

    @GetMapping("api/events/all")
    public ResponseEntity<List<EventDTO>> getAllEvents( @RequestParam String city) {
        return ResponseEntity.ok(eventService.getEvents(city));
    }

    // Endpoint za pretragu i filtriranje događaja
    @GetMapping("/api/events/filter")
    public ResponseEntity<Page<EventDTO>> getFilteredEvents(
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<EventDTO> eventDTOPage = eventService.getFilteredEvents(startDate, endDate, category, pageable);
        return ResponseEntity.ok(eventDTOPage);
    }


}
