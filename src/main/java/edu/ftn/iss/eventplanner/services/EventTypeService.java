package edu.ftn.iss.eventplanner.services;

import edu.ftn.iss.eventplanner.dtos.eventType.EventTypeDTO;
import edu.ftn.iss.eventplanner.dtos.eventType.UpdatedEventTypeDTO;
import edu.ftn.iss.eventplanner.dtos.events.EventTypeNamesDTO;
import edu.ftn.iss.eventplanner.dtos.registration.RegisterResponseDTO;
import edu.ftn.iss.eventplanner.entities.EventType;
import edu.ftn.iss.eventplanner.entities.SolutionCategory;
import edu.ftn.iss.eventplanner.repositories.EventTypeRepository;
import edu.ftn.iss.eventplanner.repositories.SolutionCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventTypeService {

    private final EventTypeRepository eventTypeRepository;
    private final SolutionCategoryRepository solutionCategoryRepository;

    @Autowired
    public EventTypeService(EventTypeRepository eventTypes, SolutionCategoryRepository solutionCategoryRepository) {
        this.eventTypeRepository = eventTypes;
        this.solutionCategoryRepository = solutionCategoryRepository;
    }


    public EventType getEventTypeById(int id) {
        return eventTypeRepository.findById(id);
        // throwing an error
                //.orElseThrow(() -> new NotFoundException("Event type not found"));
    }

    public List<EventTypeDTO> getAllEventTypesWithCategories() {
        List<EventType> eventTypes = eventTypeRepository.findAllWithCategories();

        return eventTypes.stream().map(eventType -> {
            // Map the EventType to EventTypeDTO and include categories
            return new EventTypeDTO(
                    eventType.getId(),
                    eventType.getName(),
                    eventType.getDescription(),
                    eventType.isActive(),
                    eventType.getSolutionCategories()
            );
        }).collect(Collectors.toList());
    }

    public RegisterResponseDTO create(EventTypeDTO dto) {
        // Check if an event type with the same name already exists
        if (eventTypeRepository.findByName(dto.getName()).isPresent()) {
            return new RegisterResponseDTO("Event Type with this name already exists", false);
        }

        EventType eventType = new EventType();
        eventType.setName(dto.getName());
        eventType.setDescription(dto.getDescription());
        eventType.setActive(dto.isActive());

        // Get the categories and ensure they are either found or created
        List<SolutionCategory> categories = dto.getCategories().stream()
                .map(categoryDto -> solutionCategoryRepository.findByName(categoryDto.getName())
                        .orElseGet(() -> solutionCategoryRepository.save(new SolutionCategory(
                                categoryDto.getName(),
                                categoryDto.getDescription(),
                                categoryDto.getStatus()))))
                .collect(Collectors.toList());

        eventType.setSolutionCategories(categories);

        eventTypeRepository.save(eventType);

        return new RegisterResponseDTO("Event Type created successfully", true);
    }

    public UpdatedEventTypeDTO update(Integer eventTypeId, EventTypeDTO dto) {
        Optional<EventType> eventTypeOptional = eventTypeRepository.findById(eventTypeId);

        if (eventTypeOptional.isPresent()) {
            EventType eventType = eventTypeOptional.get();

            eventType.setId(eventTypeId);
            eventType.setName(dto.getName());
            eventType.setDescription(dto.getDescription());
            eventType.setActive(dto.isActive());

            // Instead of clearing all categories, update them properly
            List<SolutionCategory> updatedCategories = dto.getCategories().stream()
                    .map(categoryDto -> solutionCategoryRepository.findByName(categoryDto.getName())
                            .orElseGet(() -> solutionCategoryRepository.save(new SolutionCategory(categoryDto.getName(), categoryDto.getDescription(), categoryDto.getStatus()))))
                    .collect(Collectors.toList());

            eventType.setSolutionCategories(updatedCategories);

            EventType updatedEventType = eventTypeRepository.save(eventType);

            UpdatedEventTypeDTO updatedDTO = new UpdatedEventTypeDTO();
            updatedDTO.setName(updatedEventType.getName());
            updatedDTO.setDescription(updatedEventType.getDescription());
            updatedDTO.setCategories(updatedEventType.getSolutionCategories());
            updatedDTO.setActive(updatedEventType.isActive());

            return updatedDTO;
        } else {
            throw new RuntimeException("Event Type with id " + eventTypeId + " not found");
        }
    }

    public EventType activateOrDeactivate(Integer id) {
        System.out.println("ENTERED SERVICE");

        Optional<EventType> existingEvent = eventTypeRepository.findById(id);

        if (existingEvent.isPresent()) {
            EventType eventToUpdate = existingEvent.get();
            // Toggle the active status (if true, make it false, and if false, make it true)
            boolean currentStatus = eventToUpdate.isActive();
            eventToUpdate.setActive(!currentStatus); // Toggle the current status
            return eventTypeRepository.save(eventToUpdate); // Save and return the updated event
        } else {
            return null;
        }
    }

    public List<EventTypeNamesDTO> getAllForEvent() {
        List<EventType> eventTypes = eventTypeRepository.findAll();
        return eventTypes.stream()
                .map(eventType -> new EventTypeNamesDTO(
                        eventType.getName()
                ))
                .collect(Collectors.toList());
    }
}
