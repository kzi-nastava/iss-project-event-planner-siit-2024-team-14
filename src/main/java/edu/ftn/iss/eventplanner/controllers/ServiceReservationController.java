package edu.ftn.iss.eventplanner.controllers;

import edu.ftn.iss.eventplanner.dtos.ServiceReservationRequest;
import edu.ftn.iss.eventplanner.dtos.ServiceReservationResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;

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
    public ResponseEntity<ServiceReservationResponse> bookService(@Valid @RequestBody ServiceReservationRequest request) {
        // Dummy response to simulate booking
        ServiceReservationResponse response = new ServiceReservationResponse(
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
