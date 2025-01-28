package edu.ftn.iss.eventplanner.services;

import edu.ftn.iss.eventplanner.dtos.EventDTO;
import edu.ftn.iss.eventplanner.entities.Event;
import edu.ftn.iss.eventplanner.repositories.EventRepository;
import org.springframework.stereotype.Service;

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

        return events.stream().map(event -> {
            EventDTO dto = new EventDTO();
            dto.setId(event.getId());
            dto.setName(event.getName());
            dto.setDescription(event.getDescription());
            dto.setLocation(event.getLocation());
            dto.setStartDate(event.getStartDate());
            dto.setEndDate(event.getEndDate());
            return dto;
        }).collect(Collectors.toList());
    }
}
