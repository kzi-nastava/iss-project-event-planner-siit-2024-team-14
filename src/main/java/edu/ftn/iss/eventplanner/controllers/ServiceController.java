package edu.ftn.iss.eventplanner.controllers;

import edu.ftn.iss.eventplanner.dtos.CreateServiceForProviderDTO;
import edu.ftn.iss.eventplanner.dtos.ServiceDTO;
import edu.ftn.iss.eventplanner.dtos.UpdateServiceDTO;
import edu.ftn.iss.eventplanner.services.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = {"api/services"})
public class ServiceController {

    //private final ServiceService serviceService;


    public ServiceController() {

    }



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



    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ServiceDTO> putUpdateService(
            @RequestBody @Validated UpdateServiceDTO service
    ) {
        return ResponseEntity.accepted().build();
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/{id}")
    void deleteService(
            @PathVariable(name = "id") long id
    ) {

    }



    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    @DeleteMapping
    void deleteAllServices() { }

}


