package edu.ftn.iss.eventplanner.controllers;

import edu.ftn.iss.eventplanner.dtos.CreateServiceDTO;
import edu.ftn.iss.eventplanner.dtos.ServiceDTO;
import edu.ftn.iss.eventplanner.mappers.ServiceDTOMapper;
import edu.ftn.iss.eventplanner.services.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Set;


@RestController
@RequestMapping(path = {"api/providers/{providerId:\\d+}/services"})
public class ProviderServicesController {

    private final ServiceService serviceService;
    private final ServiceDTOMapper modelMapper;

    @Autowired
    public ProviderServicesController(ServiceService serviceService, ServiceDTOMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.serviceService = serviceService;
    }


    // GET */api/providers/1/services (Result differs across roles)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Page<ServiceDTO>> getProviderServices(
            @PathVariable(name = "providerId") int providerId,
            @RequestParam MultiValueMap<String, String> params,
            Pageable pageable
    ) {
        params.keySet().removeAll(Set.of("page", "size", "sort"));
        return ResponseEntity.ok(Page.empty());
    }

    // POST provider[Is identified by id 1]|admin@*/api/providers/1/services
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ServiceDTO> createService(
            @PathVariable(name = "providerId") int providerId, // or just get it from token ??
            @RequestBody @Validated CreateServiceDTO service
    ) {
        ServiceDTO createdService = /* TODO: implement */ new ServiceDTO();

        URI location = URI.create("api/services/" + createdService.getId());

        return ResponseEntity
                .created(location)
                .body(createdService);
    }
}
