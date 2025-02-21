package edu.ftn.iss.eventplanner.mappers;

import edu.ftn.iss.eventplanner.dtos.EventTypeDTO;
import edu.ftn.iss.eventplanner.dtos.ServiceDTO;
import edu.ftn.iss.eventplanner.entities.Service;
import edu.ftn.iss.eventplanner.enums.OfferingVisibility;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



@Component
public class ServiceDTOMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public ServiceDTOMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }


    public Service fromDTO(Object dto) {
        return modelMapper.map(dto, Service.class);
    }

    public ServiceDTO toServiceDTO(Service service) {
        // TODO: Maybe add custom mapping to model mapper config
        ServiceDTO serviceDTO = modelMapper.map(service, ServiceDTO.class);
        serviceDTO.setSpecificities(""); // TODO: Add specificities to service
        serviceDTO.setAvailable(service.isAvailable());
        serviceDTO.setVisibility( service.isVisible() ? OfferingVisibility.PUBLIC : OfferingVisibility.PRIVATE);
        serviceDTO.setImages(new String[] { service.getImageUrl() });
        serviceDTO.setApplicableEventTypes(new EventTypeDTO[] {});
        serviceDTO.setSessionDuration(service.getDuration());

        if (service.getProvider() != null) {
            serviceDTO.getProvider().setPhotos(service.getProvider().getPictures());
        }

        return serviceDTO;
    }
}
