package edu.ftn.iss.eventplanner.controllers;

import edu.ftn.iss.eventplanner.dtos.CreateServiceDTO;
import edu.ftn.iss.eventplanner.dtos.ServiceDTO;
import edu.ftn.iss.eventplanner.dtos.UpdateServiceDTO;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping(path = {"api/services"})
public class ServiceController {

    public ServiceController() {}



    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Collection<ServiceDTO>> getAllServices() {
        return ResponseEntity.ok(List.of());
    }


    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ServiceDTO> getServiceById(
            @PathVariable(name = "id") Long id
    ) {
        return ResponseEntity.notFound().build();
    }


    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ServiceDTO> createService(
            @RequestBody CreateServiceDTO service
    ) {
        // NOTE: Data about the provider of the service has to be passed. Here through dto, but should
        // add create service on a path where you can deduce its owner. (there shall be get provider services...)
        // TODO: figure this out
        ServiceDTO createdService = new ServiceDTO();
        return ResponseEntity
                .created(URI.create("services/" + createdService.getId()))
                .body(createdService);
    }


    @PutMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ServiceDTO> putUpdateService(
            @RequestBody UpdateServiceDTO service
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


