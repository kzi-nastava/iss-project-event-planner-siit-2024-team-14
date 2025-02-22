package edu.ftn.iss.eventplanner.controllers;

import edu.ftn.iss.eventplanner.entities.BookingService;
import edu.ftn.iss.eventplanner.services.BookingServiceService;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingServiceController {

    private final BookingServiceService bookingServicesService;

    @GetMapping("/available-times")
    public List<BookingService> getAvailableTimes(
            @RequestParam Integer serviceId,
            @RequestParam String date) {

        LocalDate bookingDate = LocalDate.parse(date);
        return bookingServicesService.getConfirmedBookingsForServiceAndDate(serviceId, bookingDate);
    }
}
