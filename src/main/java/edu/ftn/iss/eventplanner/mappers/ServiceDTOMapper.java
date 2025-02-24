package edu.ftn.iss.eventplanner.mappers;

import edu.ftn.iss.eventplanner.dtos.CreateServiceDTO;
import edu.ftn.iss.eventplanner.dtos.EventTypeDTO;
import edu.ftn.iss.eventplanner.dtos.ServiceDTO;
import edu.ftn.iss.eventplanner.entities.CreateServiceRequest;
import edu.ftn.iss.eventplanner.entities.Service;
import edu.ftn.iss.eventplanner.enums.OfferingVisibility;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;


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

    public CreateServiceRequest toCreateServiceRequest(CreateServiceDTO serviceRequestDTO, int providerId) {
        CreateServiceRequest createServiceRequest = modelMapper.map(serviceRequestDTO, CreateServiceRequest.class);
        createServiceRequest.setProviderId(providerId);

        Optional.ofNullable(serviceRequestDTO.getCategory())
                .ifPresentOrElse(
                        cr -> {
                            if (cr.getId() != null) {
                                createServiceRequest.setCategoryId(cr.getId());
                            } else {
                                createServiceRequest.setCategoryName(cr.getName());
                                createServiceRequest.setCategoryDescription(cr.getDescription());
                            }
                        },
                        () -> {/* maybe throw exception */}
                );

        Optional.ofNullable(serviceRequestDTO.getSessionDurationMinutes())
                        .ifPresent(minutes -> createServiceRequest.setSessionDuration(Duration.ofMinutes(minutes)));

        Optional.ofNullable(serviceRequestDTO.getMinDurationMinutes())
                        .ifPresent(minutes -> createServiceRequest.setMinDuration(Duration.ofMinutes(minutes)));

        Optional.ofNullable(serviceRequestDTO.getMaxDurationMinutes())
                        .ifPresent(minutes -> createServiceRequest.setMaxDuration(Duration.ofMinutes(minutes)));

        Optional.ofNullable(serviceRequestDTO.getReservationPeriodDays())
                .ifPresent(days -> createServiceRequest.setReservationPeriod(Duration.ofDays(days)));

        Optional.ofNullable(serviceRequestDTO.getCancellationPeriodDays())
                .ifPresent(days -> createServiceRequest.setCancellationPeriod(Duration.ofDays(days)));

        return createServiceRequest;
    }
}
