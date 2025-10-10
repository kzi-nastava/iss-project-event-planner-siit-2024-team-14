package edu.ftn.iss.eventplanner.controllers;

import edu.ftn.iss.eventplanner.dtos.events.CreateEventDTO;
import edu.ftn.iss.eventplanner.dtos.homepage.EventDTO;
import edu.ftn.iss.eventplanner.entities.Event;
import edu.ftn.iss.eventplanner.exceptions.BadRequestException;
import edu.ftn.iss.eventplanner.services.BlockedUserService;
import edu.ftn.iss.eventplanner.services.EventService;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

@RestController
public class EventController {

    private final EventService eventService;
    private final BlockedUserService blockedUserService;

    public EventController(EventService eventService, BlockedUserService blockedUserService) {
        this.eventService = eventService;
        this.blockedUserService = blockedUserService;
    }

    @GetMapping("/api/events/top5")
    public ResponseEntity<List<EventDTO>> getTop5Events(
            @RequestParam String city,
            @RequestParam(required = false) Integer userId) {
        List<Integer> blockedUserIds = List.of();
        if (userId != null) {
            blockedUserIds = blockedUserService.getBlockedUsers(userId);
        }

        return ResponseEntity.ok(eventService.getTop5Events(city, blockedUserIds));
    }

    @GetMapping("api/events/all")
    public ResponseEntity<List<EventDTO>> getAllEvents(@RequestParam(required = false) Integer userId) {
        List<Integer> blockedUserIds = List.of();
        if (userId != null) {
            blockedUserIds = blockedUserService.getBlockedUsers(userId);
        }
        return ResponseEntity.ok(eventService.getEvents(blockedUserIds));
    }

    @GetMapping("api/events/locations")
    public ResponseEntity<List<String>> getAllLocations() {
        return ResponseEntity.ok(eventService.getAllLocations());
    }

    @GetMapping("api/events/categories")
    public ResponseEntity<List<String>> getAllCategories() {
        return ResponseEntity.ok(eventService.getAllCategories());
    }

    @GetMapping("api/events/by-organizer/{organizerId}")
    public ResponseEntity<List<EventDTO>> getEventsByOrganizer(@PathVariable Integer organizerId) {
        List<EventDTO> events = eventService.findEventsByOrganizer(organizerId);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/api/events/filter")
    public ResponseEntity<Page<EventDTO>> getFilteredEvents(
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "location", required = false) String location,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(required = false) Integer userId) {

        List<Integer> blockedUserIds = List.of();
        if (userId != null) {
            blockedUserIds = blockedUserService.getBlockedUsers(userId);
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<EventDTO> eventDTOPage = eventService.getFilteredEvents(startDate, endDate, category, location, blockedUserIds, pageable);
        return ResponseEntity.ok(eventDTOPage);
    }

    @GetMapping("/api/events/{id}")
    public ResponseEntity<EventDTO> getEventById(@PathVariable Integer id) {
        EventDTO event = eventService.getEventById(id);
        if (event != null) {
            return ResponseEntity.ok(event);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/api/events/create-event", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public EventDTO createEvent(@RequestPart("dto") CreateEventDTO dto,
                                @RequestPart(value = "photo", required = false) MultipartFile photo) throws IOException {
        System.out.println("Event type in controller: " + dto.getEventType());
        if (dto.getName() == null || dto.getDescription() == null || dto.getStartDate() == null || dto.getEndDate() == null || dto.getEventType() == null) {
            throw new BadRequestException("Missing required event data");
        }

        // Call the service to create the event
        Event createdEvent = eventService.createEvent(dto, photo);

        // Convert the entity to DTO before returning
        EventDTO eventDTO = new EventDTO();
        eventDTO.setId(createdEvent.getId());
        eventDTO.setName(createdEvent.getName());
        eventDTO.setDescription(createdEvent.getDescription());
        eventDTO.setLocation(createdEvent.getLocation());
        eventDTO.setStartDate(createdEvent.getStartDate());
        eventDTO.setEndDate(createdEvent.getEndDate());

        eventDTO.setImageUrl("event-photo/" + createdEvent.getImageUrl());
        eventDTO.setPrivacyType(createdEvent.getPrivacyType().toString());

        return eventDTO;
    }

    @GetMapping("/api/events/joined/{userId}")
    public ResponseEntity<List<EventDTO>> getJoinedEvents(@PathVariable Integer userId) {
        List<EventDTO> events = eventService.getJoinedEventsForUser(userId);
        return ResponseEntity.ok(events);
    }

    @PostMapping("/api/events/{eventId}/toggle-favorite/{userId}")
    public ResponseEntity<String> toggleFavorite(
            @PathVariable Integer eventId,
            @PathVariable Integer userId) {
        eventService.toggleFavoriteEvent(userId, eventId);
        return ResponseEntity.ok("Favorite toggled successfully");
    }

    @GetMapping("/api/events/get-photo/{id}")
    public ResponseEntity<Resource> getPhoto(@PathVariable("id") int id) throws MalformedURLException {
        System.out.println("ENTERED getPhoto CONTROLLER for Event");
        return eventService.getEventPhoto(id);
    }
}
