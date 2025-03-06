package edu.ftn.iss.eventplanner.services;

import edu.ftn.iss.eventplanner.dtos.homepage.EventDTO;
import edu.ftn.iss.eventplanner.entities.Event;
import edu.ftn.iss.eventplanner.exceptions.NotFoundException;
import edu.ftn.iss.eventplanner.enums.PrivacyType;
import edu.ftn.iss.eventplanner.repositories.EventRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {
    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<EventDTO> getTop5Events(String city) {
        List<Event> events = eventRepository.findFirst5ByLocationOrderByStartDateDesc(city);
        List<Event> publicEvents = events.stream()
                .filter(event -> event.getPrivacyType() == PrivacyType.PUBLIC)
                .collect(Collectors.toList());

        return mapToDTO(publicEvents);
    }

    public List<EventDTO> getEvents() {
        List<Event> events = eventRepository.findAll();
        List<Event> publicEvents = events.stream()
                .filter(event -> event.getPrivacyType() == PrivacyType.PUBLIC)
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
}
