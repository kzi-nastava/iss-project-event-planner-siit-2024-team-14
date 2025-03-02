package edu.ftn.iss.eventplanner.services;

import edu.ftn.iss.eventplanner.dtos.serviceDetails.ServiceDTO;
import edu.ftn.iss.eventplanner.dtos.serviceDetails.ProviderDTO;
import edu.ftn.iss.eventplanner.dtos.serviceDetails.CategoryDTO;
import edu.ftn.iss.eventplanner.dtos.serviceDetails.EventTypeDTO;
import edu.ftn.iss.eventplanner.repositories.ServiceRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceService {

    private final ServiceRepository serviceRepository;

    @Autowired
    private ModelMapper modelMapper;

    public ServiceService(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    public ServiceDTO getServiceById(Integer id) {
        edu.ftn.iss.eventplanner.entities.Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Service not found"));

        ServiceDTO serviceDTO = modelMapper.map(service, ServiceDTO.class);

        // Mapiranje kategorije
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(service.getCategory().getId());
        categoryDTO.setName(service.getCategory().getName());
        categoryDTO.setDescription(service.getCategory().getDescription());
        serviceDTO.setCategory(categoryDTO);

        // Mapiranje event tipova
        List<EventTypeDTO> eventTypes = service.getCategory().getEventTypes().stream()
                .map(eventType -> {
                    EventTypeDTO eventTypeDTO = new EventTypeDTO();
                    eventTypeDTO.setName(eventType.getName());
                    eventTypeDTO.setDescription(eventType.getDescription());
                    return eventTypeDTO;
                })
                .collect(Collectors.toList());

        serviceDTO.setApplicableEventTypes(eventTypes.toArray(new EventTypeDTO[0]));

        ProviderDTO providerDTO = new ProviderDTO();
        providerDTO.setCompanyName(service.getProvider().getCompanyName());
        providerDTO.setDescription(service.getProvider().getDescription());
        serviceDTO.setProvider(providerDTO);
        serviceDTO.setProviderId(service.getProvider().getId());

        return serviceDTO;
    }
}
