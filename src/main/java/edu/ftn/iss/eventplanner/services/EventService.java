package edu.ftn.iss.eventplanner.services;

import edu.ftn.iss.eventplanner.dtos.EventDTO;
import edu.ftn.iss.eventplanner.entities.Event;
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
        return mapToDTO(events);
    }

    public List<EventDTO> getEvents(String city) {
        List<Event> events = eventRepository.findByLocation(city);
        return mapToDTO(events);
    }

    public Page<EventDTO> getFilteredEvents(String startDate, String endDate, String category, Pageable pageable) {
        LocalDate start = (startDate != null && !startDate.isEmpty()) ? LocalDate.parse(startDate) : null;
        LocalDate end = (endDate != null && !endDate.isEmpty()) ? LocalDate.parse(endDate) : null;

        Page<Event> eventPage;

        if (category != null && !category.isEmpty()) {
            eventPage = eventRepository.findByEventTypeAndDateRange(category, start, end, pageable);
        } else if (start != null && end != null) {
            eventPage = eventRepository.findByDateRange(start, end, pageable);
        } else if (start != null) {
            eventPage = eventRepository.findByStartDateGreaterThanEqual(start, pageable);
        } else if (end != null) {
            eventPage = eventRepository.findByEndDateLessThanEqual(end, pageable);
        } else {
            eventPage = eventRepository.findAll(pageable); // Ako nema filtera, vrati sve događaje
        }

        // Mapiranje Event objekata na EventDTO
        Page<EventDTO> eventDTOPage = eventPage.map(event -> {
            EventDTO dto = new EventDTO();
            dto.setId(event.getId());
            dto.setName(event.getName());
            dto.setDescription(event.getDescription());
            dto.setLocation(event.getLocation());
            dto.setStartDate(event.getStartDate());
            dto.setEndDate(event.getEndDate());
            dto.setImageUrl(event.getImageUrl());


            if (event.getOrganizer() != null) {
                dto.setOrganizerFirstName(event.getOrganizer().getFirstName());
                dto.setOrganizerLastName(event.getOrganizer().getLastName());
                dto.setOrganizerProfilePicture(event.getOrganizer().getProfilePicture());
            }
            return dto;
        });
        return eventDTOPage;
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
                dto.setOrganizerFirstName(event.getOrganizer().getFirstName());
                dto.setOrganizerLastName(event.getOrganizer().getLastName());
                dto.setOrganizerProfilePicture(event.getOrganizer().getProfilePicture());
            }

            return dto;
        }).collect(Collectors.toList());
    }
}
