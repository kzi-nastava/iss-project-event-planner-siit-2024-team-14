package edu.ftn.iss.eventplanner.services;

import edu.ftn.iss.eventplanner.dtos.events.CreateEventDTO;
import edu.ftn.iss.eventplanner.dtos.homepage.EventDTO;
import edu.ftn.iss.eventplanner.entities.Event;
import edu.ftn.iss.eventplanner.entities.EventOrganizer;
import edu.ftn.iss.eventplanner.entities.EventType;
import edu.ftn.iss.eventplanner.entities.SolutionCategory;
import edu.ftn.iss.eventplanner.exceptions.NotFoundException;
import edu.ftn.iss.eventplanner.enums.PrivacyType;
import edu.ftn.iss.eventplanner.repositories.EventOrganizerRepository;
import edu.ftn.iss.eventplanner.repositories.EventRepository;
import edu.ftn.iss.eventplanner.repositories.EventTypeRepository;
import edu.ftn.iss.eventplanner.repositories.SolutionCategoryRepository;
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
import java.util.stream.Collectors;

@Service
public class EventService {
    private final EventRepository eventRepository;
    private final EventTypeRepository eventTypeRepository;
    private final EventOrganizerRepository eventOrganizerRepository;
    private final SolutionCategoryRepository solutionCategoryRepository;

    public EventService(EventRepository eventRepository, EventTypeRepository eventTypeRepository, EventOrganizerRepository eventOrganizerRepository, SolutionCategoryRepository solutionCategoryRepository) {
        this.eventRepository = eventRepository;
        this.eventTypeRepository = eventTypeRepository;
        this.eventOrganizerRepository = eventOrganizerRepository;
        this.solutionCategoryRepository = solutionCategoryRepository;
    }

    public List<EventDTO> getTop5Events(String city) {
        List<Event> events = eventRepository.findFirst5ByLocationOrderByStartDateDesc(city);
        List<Event> publicEvents = events.stream()
                .filter(event -> event.getPrivacyType() == PrivacyType.OPEN)
                .collect(Collectors.toList());

        return mapToDTO(publicEvents);
    }

    public List<EventDTO> getEvents() {
        List<Event> events = eventRepository.findAll();
        List<Event> publicEvents = events.stream()
                .filter(event -> event.getPrivacyType() == PrivacyType.OPEN)
                .collect(Collectors.toList());

        return mapToDTO(publicEvents);
    }

    public List<String> getAllLocations() {
        return eventRepository.findAllLocations();
    }

    public List<String> getAllCategories() {
        return eventRepository.findAllCategories();
    }

    public List<EventDTO> findEventsByOrganizer(Integer organizerId) {
        List<Event> events =  eventRepository.findByOrganizerId(organizerId);
        return mapToDTO(events);
    }

    public Page<EventDTO> getFilteredEvents(String startDate, String endDate, String category, String location, Pageable pageable) {
        LocalDate start = (startDate != null && !startDate.isEmpty()) ? LocalDate.parse(startDate) : null;
        LocalDate end = (endDate != null && !endDate.isEmpty()) ? LocalDate.parse(endDate) : null;

        // Pozivanje novog metoda sa konsolidovanim filtriranjem
        Page<Event> eventPage = eventRepository.findFilteredEvents(category, start, end, location, pageable);

        // Mapiranje Event objekata na EventDTO
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
                        event.getOrganizer() != null ? event.getOrganizer().getProfilePhoto() : null
                ))
                .orElse(null); // Ako event ne postoji, vrati null
    }

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

            if (event.getOrganizer() != null) {
                dto.setOrganizerFirstName(event.getOrganizer().getName());
                dto.setOrganizerLastName(event.getOrganizer().getSurname());
                dto.setOrganizerProfilePicture(event.getOrganizer().getProfilePhoto());
                dto.setOrganizerId(event.getOrganizer().getId());
            }

            return dto;
        }).collect(Collectors.toList());
    }

    public Event getEventById(int id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event not found"));
    }

    public Event createEvent(CreateEventDTO dto, MultipartFile photo) throws IOException {
        System.out.println("EVENT FOR SAVING: " + dto);

        Event event = new Event();
        event.setName(dto.getName());
        event.setDescription(dto.getDescription());
        event.setLocation(dto.getLocation());
        event.setStartDate(dto.getStartDate());
        event.setEndDate(dto.getEndDate());
        event.setMaxParticipants(dto.getGuestNumber());

        EventType eventType = eventTypeRepository.findByName(dto.getEventType())
                .orElseThrow(() -> new NotFoundException("Event Type not found"));
        event.setEventType(eventType);

        event.setPrivacyType(PrivacyType.valueOf(dto.getType()));

        EventOrganizer organizer = eventOrganizerRepository.findById(dto.getOrganizer());
        event.setOrganizer(organizer);

        List<SolutionCategory> selectedCategories =
                solutionCategoryRepository.findByNameIn(dto.getCategories());
        event.setSelectedCategories(selectedCategories);

        if (photo != null && !photo.isEmpty()) {
            String photoFilename = dto.getName() + "_" + dto.getOrganizer() + ".png";
            String uploadDir = "src/main/resources/static/event-photo/";
            Path filePath = Paths.get(uploadDir + photoFilename);

            Files.createDirectories(filePath.getParent());
            Files.write(filePath, photo.getBytes());

            dto.setPhoto(photoFilename);

            event.setImageUrl(photoFilename);
        }


        if (event.getPrivacyType() == PrivacyType.CLOSED) {
            // ADD LOGIC FOR CLOSED EVENT AND INVITATIONS
        }
        return eventRepository.save(event);
    }

}
