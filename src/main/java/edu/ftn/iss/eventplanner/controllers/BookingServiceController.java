package edu.ftn.iss.eventplanner.controllers;
import edu.ftn.iss.eventplanner.dtos.serviceBooking.ApproveRequestDTO;
import edu.ftn.iss.eventplanner.dtos.serviceBooking.BookingServiceRequestDTO;
import edu.ftn.iss.eventplanner.dtos.serviceBooking.BookingServiceRequestsForProviderDTO;
import edu.ftn.iss.eventplanner.entities.BookingService;
import edu.ftn.iss.eventplanner.services.BookingServiceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import javax.validation.Valid;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingServiceController {

    private final BookingServiceService bookingServiceService;

    @GetMapping("/available-start-times")
    public List<String> getAvailableStartTimes(
            @RequestParam Integer serviceId,
            @RequestParam String date,
            @RequestParam(required = false) Integer duration) {

        LocalDate bookingDate = LocalDate.parse(date);
        return bookingServiceService.getAvailableStartTimes(serviceId, bookingDate, duration);
    }

    @PostMapping("/reserve")
    public ResponseEntity<BookingService> reserveService(@RequestBody BookingServiceRequestDTO request) {
        Time startTime = Time.valueOf(request.getStartTime() + ":00");
        Duration duration = Duration.ofMinutes(request.getDuration());

        try {
            BookingService bookingService = bookingServiceService.createBooking(request.getServiceId(), request.getEventId(), request.getBookingDate(), startTime, duration);
            return ResponseEntity.ok(bookingService);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/approve")
    public ResponseEntity<BookingServiceRequestsForProviderDTO> approveRequest(@Valid @RequestBody ApproveRequestDTO approveRequestDTO) {
        BookingServiceRequestsForProviderDTO response = bookingServiceService.approveRequest(approveRequestDTO);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/delete")
    public ResponseEntity<BookingServiceRequestsForProviderDTO> deleteRequest(@Valid @RequestBody ApproveRequestDTO approveRequestDTO) {
        BookingServiceRequestsForProviderDTO response = bookingServiceService.deleteRequest(approveRequestDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/requests")
    public ResponseEntity<List<BookingServiceRequestsForProviderDTO>> getBookingRequests() {
        List<BookingServiceRequestsForProviderDTO> requests = bookingServiceService.getRequests();
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/all")
    public ResponseEntity<List<BookingServiceRequestsForProviderDTO>> getAllBookings() {
        List<BookingServiceRequestsForProviderDTO> requests = bookingServiceService.getAllBookings();
        return ResponseEntity.ok(requests);
    }




}
