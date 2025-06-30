package edu.ftn.iss.eventplanner.controllers;

import edu.ftn.iss.eventplanner.dtos.CreateServiceDTO;
import edu.ftn.iss.eventplanner.dtos.serviceDetails.ServiceDTO;
import edu.ftn.iss.eventplanner.entities.CreateServiceRequest;
import edu.ftn.iss.eventplanner.entities.Service;
import edu.ftn.iss.eventplanner.mappers.ServiceDTOMapper;
import edu.ftn.iss.eventplanner.services.ServiceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
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
    public ResponseEntity<Page<ServiceDTO>> getProviderServices(
            @PathVariable(name = "providerId") int providerId,
            @RequestParam MultiValueMap<String, String> params,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        params.remove("provider");
        params.add("provider", String.valueOf(providerId));

        URI redirectUri = uriComponentsBuilder.path("/api/services")
                .queryParams(params)
                .build()
                .toUri();

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(redirectUri)
                .build();
    }

    // POST provider[Is identified by id 1]|admin@*/api/providers/1/services
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN') || hasRole('PROVIDER') && #providerId == authentication.principal.id")
    public ResponseEntity<ServiceDTO> createService(
            @PathVariable(name = "providerId") int providerId,
            @RequestBody @Valid CreateServiceDTO serviceRequestDTO,
            UriComponentsBuilder uriBuilder
    ) {
        CreateServiceRequest request = modelMapper.toCreateServiceRequest(serviceRequestDTO, providerId);
        Service createdService = serviceService.createService(request);

        return ResponseEntity
                .created(uriBuilder.path("/api/services/{id}").buildAndExpand(createdService.getId()).toUri())
                .body(modelMapper.toServiceDTO(createdService));
    }
}