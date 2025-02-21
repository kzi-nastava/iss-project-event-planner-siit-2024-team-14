package edu.ftn.iss.eventplanner.controllers;

import edu.ftn.iss.eventplanner.dtos.ServiceReservationRequestDTO;
import edu.ftn.iss.eventplanner.services.ServiceReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/services")
public class ServiceReservationController {
    private final ServiceReservationService serviceReservationService;

    public ServiceReservationController(ServiceReservationService serviceReservationService) {
        this.serviceReservationService = serviceReservationService;
    }

    @PostMapping("/reserve")
    public ResponseEntity<String> reserveService(@RequestBody ServiceReservationRequestDTO requestDTO) {
        String response = serviceReservationService.reserveService(requestDTO);
        return ResponseEntity.ok(response);
    }
}
