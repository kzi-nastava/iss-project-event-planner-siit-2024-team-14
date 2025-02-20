package edu.ftn.iss.eventplanner.mappers;

import edu.ftn.iss.eventplanner.dtos.ServiceDTO;
import edu.ftn.iss.eventplanner.entities.Service;
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
        return new ServiceDTO();
    }
}
