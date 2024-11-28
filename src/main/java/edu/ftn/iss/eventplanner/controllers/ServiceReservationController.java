package edu.ftn.iss.eventplanner.controllers;

import edu.ftn.iss.eventplanner.dtos.ServiceReservationRequestDTO;
import edu.ftn.iss.eventplanner.dtos.ServiceReservationResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/services/reservations")
public class ServiceReservationController {

    /**
     * Endpoint za kreiranje rezervacije usluge.
     *
     * @param request DTO sa informacijama o rezervaciji.
     * @return Response sa statusom i informacijama o rezervaciji.
     */
    @PostMapping
    public ResponseEntity<ServiceReservationResponseDTO> bookService(@Valid @RequestBody ServiceReservationRequestDTO request) {
        // Dummy response to simulate booking
        ServiceReservationResponseDTO response = new ServiceReservationResponseDTO(
                1L,
                request.getServiceId(),
                request.getEventId(),
                request.getReservationDate(),
                request.getStartTime(),
                request.getEndTime(),
                "Confirmed"
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Endpoint za dobijanje svih rezervacija.
     *
     * @return Lista svih rezervacija (dummy response).
     */
    @GetMapping
    public ResponseEntity<String> getAllReservations() {
        return ResponseEntity.ok("List of all reservations (dummy response)");
    }
}
