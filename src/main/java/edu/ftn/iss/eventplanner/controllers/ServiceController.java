package edu.ftn.iss.eventplanner.controllers;

import edu.ftn.iss.eventplanner.dtos.serviceDetails.ServiceDTO;
import edu.ftn.iss.eventplanner.dtos.UpdateServiceDTO;
import edu.ftn.iss.eventplanner.entities.Service;
import edu.ftn.iss.eventplanner.mappers.ServiceDTOMapper;
import edu.ftn.iss.eventplanner.services.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Set;


@RestController
@RequestMapping(path = {"api/services"}, produces = MediaType.APPLICATION_JSON_VALUE)
public class ServiceController {

    private final ServiceService serviceService;
    private final ServiceDTOMapper modelMapper;

    @Autowired
    public ServiceController(ServiceService serviceService, ServiceDTOMapper modelMapper) {
        this.serviceService = serviceService;
        this.modelMapper = modelMapper;
    }


    // GET */api/services (Result differs across user roles)
    @GetMapping
    ResponseEntity<Page<ServiceDTO>> getAllServices(
            @RequestParam MultiValueMap<String, String> params,
            Pageable pageable,
            Principal principal
    ) {
        params.keySet().removeAll(Set.of("page", "size", "sort"));
        Page<Service> services = serviceService.getAllServices(pageable, principal == null ? null : principal.getName());

        return ResponseEntity.ok(
                services.map(this.modelMapper::toServiceDTO)
        );
    }

    // GET @*/api/services/1
    @GetMapping(path = "/{id:\\d+}")
    ResponseEntity<ServiceDTO> getServiceById(
            @PathVariable(name = "id") int id
    ) {
        Service service = serviceService.getServiceById(id); // maybe hide services that are not publicly visible
        return ResponseEntity.ok(modelMapper.toServiceDTO(service));
    }


    // PUT provider[Provides the service]|admin@*/api/services/1
    @PutMapping(path = "/{id:\\d+}")
    ResponseEntity<ServiceDTO> putUpdateService(
            @PathVariable int id,
            @RequestBody @Validated UpdateServiceDTO updateServiceDTO
    ) {
        updateServiceDTO.setId(id);
        Service service = serviceService.updateService(modelMapper.toUpdateServiceRequest(updateServiceDTO));
        return ResponseEntity.ok(modelMapper.toServiceDTO(service));
    }

    // DELETE provider[Provides the service]|admin@*/api/services/1
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/{id:\\d+}")
    void deleteService(
            @PathVariable(name = "id") int id
    ) {
        serviceService.deleteService(id);
    }


    // DELETE admin@*/api/services
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping
    void deleteAllServices() {
        serviceService.deleteAllServices();
    }

}