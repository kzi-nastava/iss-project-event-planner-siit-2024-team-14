package edu.ftn.iss.eventplanner.controllers;

import edu.ftn.iss.eventplanner.dtos.CreateServiceForProviderDTO;
import edu.ftn.iss.eventplanner.dtos.serviceDetails.ServiceDTO;
import edu.ftn.iss.eventplanner.dtos.UpdateServiceDTO;
import edu.ftn.iss.eventplanner.services.ServiceService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping(path = {"api/services"})
public class ServiceController {

    private final ServiceService serviceService;

    public ServiceController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Collection<ServiceDTO>> getAllServices() {
        return ResponseEntity.ok(List.of());
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ServiceDTO> getServiceById(
            @PathVariable(name = "id") Integer id) {
        ServiceDTO serviceDTO = serviceService.getServiceById(id);
        return ResponseEntity.ok(serviceDTO);
    }


    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ServiceDTO> createService(
            @RequestBody @Validated CreateServiceForProviderDTO service
    ) {
        ServiceDTO createdService = new ServiceDTO();
        return ResponseEntity
                .created(URI.create("services/" + createdService.getId()))
                .body(createdService);
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ServiceDTO> putUpdateService(
            @RequestBody @Validated UpdateServiceDTO service
    ) {
        return ResponseEntity.accepted().build();
    }

    @DeleteMapping(path = "/{id}")
    ResponseEntity<Void> deleteService(
            @PathVariable(name = "id") long id
    ) {
        return ResponseEntity.noContent().build();
    }

}


