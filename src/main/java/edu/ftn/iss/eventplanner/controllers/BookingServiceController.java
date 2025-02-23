package edu.ftn.iss.eventplanner.controllers;

import edu.ftn.iss.eventplanner.dtos.serviceBooking.BookingServiceRequestDTO;
import edu.ftn.iss.eventplanner.entities.BookingService;
import edu.ftn.iss.eventplanner.services.BookingServiceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

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
        System.out.println("usao kontroler");
        // Konvertovanje startTime u Time
        Time startTime = Time.valueOf(request.getStartTime() + ":00");  // Pretpostavljam da dolazi kao HH:mm
        Duration duration = Duration.ofMinutes(request.getDuration());  // Pretvaramo broj minuta u Duration

        try {
            BookingService bookingService = bookingServiceService.createBooking(request.getServiceId(), request.getEventId(), request.getBookingDate(), startTime, duration);
            return ResponseEntity.ok(bookingService);  // 200 OK
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();  // 400 Bad Request ako dođe do greške
        }
    }




}
