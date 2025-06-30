package edu.ftn.iss.eventplanner.services;

import edu.ftn.iss.eventplanner.dtos.events.CreateEventDTO;
import edu.ftn.iss.eventplanner.dtos.homepage.EventDTO;
import edu.ftn.iss.eventplanner.entities.*;
import edu.ftn.iss.eventplanner.exceptions.NotFoundException;
import edu.ftn.iss.eventplanner.enums.PrivacyType;
import edu.ftn.iss.eventplanner.repositories.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final EventTypeRepository eventTypeRepository;
    private final EventOrganizerRepository eventOrganizerRepository;
    private final SolutionCategoryRepository solutionCategoryRepository;
    private final UserRepository userRepository;

    public EventService(EventRepository eventRepository,
                        EventTypeRepository eventTypeRepository,
                        EventOrganizerRepository eventOrganizerRepository,
                        SolutionCategoryRepository solutionCategoryRepository,
                        UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.eventTypeRepository = eventTypeRepository;
        this.eventOrganizerRepository = eventOrganizerRepository;
        this.solutionCategoryRepository = solutionCategoryRepository;
        this.userRepository = userRepository;
    }

    /**
     * Returns the top 5 latest public events for a given city.
     */
    public List<EventDTO> getTop5Events(String city) {
        List<Event> events = eventRepository.findFirst5ByLocationOrderByStartDateDesc(city);
        List<Event> publicEvents = events.stream()
                .filter(event -> event.getPrivacyType() == PrivacyType.OPEN)
                .collect(Collectors.toList());

        return mapToDTO(publicEvents);
    }

    /**
     * Returns all public events.
     */
    public List<EventDTO> getEvents() {
        List<Event> events = eventRepository.findAll();
        List<Event> publicEvents = events.stream()
                .filter(event -> event.getPrivacyType() == PrivacyType.OPEN)
                .collect(Collectors.toList());

        return mapToDTO(publicEvents);
    }

    /**
     * Retrieves all unique event locations from the database.
     */
    public List<String> getAllLocations() {
        return eventRepository.findAllLocations();
    }

    /**
     * Retrieves all unique categories from the database.
     */
    public List<String> getAllCategories() {
        return eventRepository.findAllCategories();
    }

    /**
     * Finds all events created by a specific organizer.
     */
    public List<EventDTO> findEventsByOrganizer(Integer organizerId) {
        List<Event> events =  eventRepository.findAllByOrganizerId(organizerId);
        return mapToDTO(events);
    }

    /**
     * Returns filtered events by optional start date, end date, category, and location, with pagination support.
     */
    public Page<EventDTO> getFilteredEvents(String startDate, String endDate, String category, String location, Pageable pageable) {
        LocalDate start = (startDate != null && !startDate.isEmpty()) ? LocalDate.parse(startDate) : null;
        LocalDate end = (endDate != null && !endDate.isEmpty()) ? LocalDate.parse(endDate) : null;

        Page<Event> eventPage = eventRepository.findFilteredEvents(category, start, end, location, pageable);

        return eventPage.map(event -> {
            EventDTO dto = new EventDTO();
            dto.setId(event.getId());
            dto.setName(event.getName());
            dto.setDescription(event.getDescription());
            dto.setLocation(event.getLocation());
            dto.setStartDate(event.getStartDate());
            dto.setEndDate(event.getEndDate());
            dto.setImageUrl(event.getImageUrl());

            if (event.getOrganizer() != null) {
                dto.setOrganizerFirstName(event.getOrganizer().getName());
                dto.setOrganizerLastName(event.getOrganizer().getSurname());
                dto.setOrganizerProfilePicture(event.getOrganizer().getProfilePhoto());
            }
            return dto;
        });
    }

    /**
     * Returns a detailed DTO for a specific event by ID.
     */
    public EventDTO getEventById(Integer id) {
        return eventRepository.findById(id)
                .map(event -> new EventDTO(
                        event.getId(),
                        event.getName(),
                        event.getDescription(),
                        event.getLocation(),
                        event.getPrivacyType().toString(),
                        event.getStartDate(),
                        event.getEndDate(),
                        event.getImageUrl(),
                        event.getOrganizer() != null ? event.getOrganizer().getName() : null,
                        event.getOrganizer() != null ? event.getOrganizer().getSurname() : null,
                        event.getOrganizer() != null ? event.getOrganizer().getId() : null,
                        event.getOrganizer() != null ? event.getOrganizer().getProfilePhoto() : null,
                        event.getMaxParticipants()
                ))
                .orElse(null); // Return null if event does not exist
    }

    /**
     * Maps a list of Event entities to a list of EventDTOs.
     */
    private List<EventDTO> mapToDTO(List<Event> events) {
        return events.stream().map(event -> {
            EventDTO dto = new EventDTO();
            dto.setId(event.getId());
            dto.setName(event.getName());
            dto.setDescription(event.getDescription());
            dto.setLocation(event.getLocation());
            dto.setStartDate(event.getStartDate());
            dto.setEndDate(event.getEndDate());
            dto.setImageUrl(event.getImageUrl());
            dto.setMaxParticipants(event.getMaxParticipants());

            if (event.getOrganizer() != null) {
                dto.setOrganizerFirstName(event.getOrganizer().getName());
                dto.setOrganizerLastName(event.getOrganizer().getSurname());
                dto.setOrganizerProfilePicture(event.getOrganizer().getProfilePhoto());
                dto.setOrganizerId(event.getOrganizer().getId());
            }

            return dto;
        }).collect(Collectors.toList());
    }

    /**
     * Returns the Event entity by ID or throws NotFoundException if not found.
     */
    public Event getEventById(int id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event not found"));
    }

    /**
     * Creates and saves a new event, including optional image file upload and category/type assignments.
     */
    public Event createEvent(CreateEventDTO dto, MultipartFile photo) throws IOException {
        System.out.println("EVENT FOR SAVING: " + dto);

        Event event = new Event();
        event.setName(dto.getName());
        event.setDescription(dto.getDescription());
        event.setLocation(dto.getLocation());
        event.setStartDate(dto.getStartDate());
        event.setEndDate(dto.getEndDate());
        event.setMaxParticipants(dto.getGuestNumber());

        // Set event type
        EventType eventType = eventTypeRepository.findByName(dto.getEventType())
                .orElseThrow(() -> new NotFoundException("Event Type not found"));
        event.setEventType(eventType);

        // Set privacy type (OPEN, PRIVATE, etc.)
        event.setPrivacyType(PrivacyType.valueOf(dto.getType()));

        // Set organizer
        EventOrganizer organizer = eventOrganizerRepository.findById(dto.getOrganizer());
        event.setOrganizer(organizer);

        // Set selected categories
        List<SolutionCategory> selectedCategories = solutionCategoryRepository.findByNameIn(dto.getCategories());
        event.setSelectedCategories(selectedCategories);

        // Handle image file if present
        if (photo != null && !photo.isEmpty()) {
            String photoFilename = dto.getName() + "_" + dto.getOrganizer() + ".png";
            String uploadDir = "src/main/resources/static/event-photo/";
            Path filePath = Paths.get(uploadDir + photoFilename);

            Files.createDirectories(filePath.getParent());
            Files.write(filePath, photo.getBytes());

            dto.setPhoto(photoFilename);
            event.setImageUrl(photoFilename);
        }

        return eventRepository.save(event);
    }

    public List<EventDTO> getJoinedEventsForUser(Integer userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found with ID: " + userId);
        }

        User user = userOptional.get();
        List<Event> joinedEvents = user.getJoinedEvents();

        return joinedEvents.stream()
                .map(event -> {
                    EventDTO dto = new EventDTO();
                    dto.setId(event.getId());
                    dto.setName(event.getName());
                    dto.setDescription(event.getDescription());
                    dto.setLocation(event.getLocation());
                    dto.setPrivacyType(event.getPrivacyType().toString());
                    dto.setStartDate(event.getStartDate());
                    dto.setEndDate(event.getEndDate());
                    dto.setImageUrl(event.getImageUrl());
                    dto.setMaxParticipants(event.getMaxParticipants());

                    if (event.getOrganizer() != null) {
                        dto.setOrganizerFirstName(event.getOrganizer().getName());
                        dto.setOrganizerLastName(event.getOrganizer().getSurname());
                        dto.setOrganizerId(event.getOrganizer().getId());
                        dto.setOrganizerProfilePicture(event.getOrganizer().getProfilePhoto());
                    }

                    return dto;
                })
                .toList();
    }


}
