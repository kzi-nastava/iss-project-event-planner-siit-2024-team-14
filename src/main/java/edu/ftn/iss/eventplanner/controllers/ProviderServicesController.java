package edu.ftn.iss.eventplanner.controllers;

import edu.ftn.iss.eventplanner.dtos.CreateServiceDTO;
import edu.ftn.iss.eventplanner.dtos.CreateServiceForProviderDTO;
import edu.ftn.iss.eventplanner.dtos.ServiceDTO;
import edu.ftn.iss.eventplanner.mappers.ServiceDTOMapper;
import edu.ftn.iss.eventplanner.services.ServiceService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collection;
import java.util.List;


@RestController
@RequestMapping(path = {"api/providers/{providerId:\\d+}/services", "api/services"})
public class ProviderServicesController {

    private final ServiceService serviceService;
    private final ServiceDTOMapper modelMapper;

    @Autowired
    public ProviderServicesController(ServiceService serviceService, ServiceDTOMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.serviceService = serviceService;
    }



    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Collection<ServiceDTO>> getProviderServices( // TODO: if an admin or provider requesting his/hers services, return all, otherwise return only publicly visible services
            @PathVariable(required = false, name = "providerId") Integer providerId
    ) {
        return ResponseEntity.ok(List.of());
    }


    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ServiceDTO> createService(
            @PathVariable(required = true, name = "providerId") int providerId, // or just get it from token ??
            @RequestBody @Validated CreateServiceDTO service
    ) {
        ServiceDTO createdService = /* TODO: implement */ new ServiceDTO();

        URI location = URI.create("api/services/" + createdService.getId());

        return ResponseEntity
                .created(location)
                .body(createdService);
    }
}
