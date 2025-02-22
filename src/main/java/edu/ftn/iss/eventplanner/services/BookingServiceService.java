package edu.ftn.iss.eventplanner.services;
import edu.ftn.iss.eventplanner.entities.BookingService;
import edu.ftn.iss.eventplanner.repositories.BookingServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceService {
    private final BookingServiceRepository bookingServicesRepository;

    public List<BookingService> getConfirmedBookingsForServiceAndDate(Integer serviceId, LocalDate date) {
        return bookingServicesRepository.findConfirmedBookings(serviceId, date);
    }
}
