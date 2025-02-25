package edu.ftn.iss.eventplanner.services;

import edu.ftn.iss.eventplanner.dtos.serviceBooking.BookingServiceRequestDTO;
import edu.ftn.iss.eventplanner.entities.BookingService;
import edu.ftn.iss.eventplanner.entities.Event;
import edu.ftn.iss.eventplanner.repositories.BookingServiceRepository;
import edu.ftn.iss.eventplanner.repositories.EventRepository;
import edu.ftn.iss.eventplanner.repositories.ServiceRepository;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingServiceService {
    private final BookingServiceRepository bookingServiceRepository;
    private final EventRepository eventRepository;
    private final ServiceRepository serviceRepository;

    public BookingServiceService(BookingServiceRepository bookingServiceRepository, EventRepository eventRepository, ServiceRepository serviceRepository) {
        this.bookingServiceRepository = bookingServiceRepository;
        this.eventRepository = eventRepository;
        this.serviceRepository = serviceRepository;
    }

    public List<String> getAvailableStartTimes(Integer serviceId, LocalDate date, Integer optionalDuration) {
        // Dohvatamo zauzete slotove za dati servis i datum
        List<BookingService> bookedSlots = bookingServiceRepository.findConfirmedBookings(serviceId, date);

        // Radno vreme servisa
        LocalTime startOfDay = LocalTime.of(8, 0);
        LocalTime endOfDay = LocalTime.of(20, 0);

        // Lista zauzetih intervala (početak i kraj) - tj. kada su zauzeti termini
        List<LocalTime[]> occupiedIntervals = bookedSlots.stream()
                .map(b -> new LocalTime[] {
                        b.getStartTime().toLocalTime(),
                        b.getStartTime().toLocalTime().plusMinutes(b.getDuration().toMinutes())
                })
                .collect(Collectors.toList());

        // Generišemo sve moguće start time-ove na svakih 30 minuta
        List<LocalTime> allSlots = new ArrayList<>();
        for (LocalTime time = startOfDay; time.isBefore(endOfDay); time = time.plusMinutes(30)) {
            allSlots.add(time);
        }

        // Filtriramo slobodne termine koji nisu zauzeti i uklapaju se u željeni duration
        List<String> availableTimes = allSlots.stream()
                .filter(time -> {
                    // Proveravamo da li je željeni duration unutar slobodnog vremena
                    boolean isAvailable = true;
                    if (optionalDuration != null) {
                        // Ako je zadat duration, proveravamo da li je ceo termin unutar slobodnog vremena
                        LocalTime endRequested = time.plusMinutes(optionalDuration);
                        isAvailable = occupiedIntervals.stream().noneMatch(interval ->
                                // Provera da li se traženi interval preklapa sa zauzetim
                                !(endRequested.isBefore(interval[0]) || time.isAfter(interval[1]))
                        );
                    } else {
                        // Ako nema duration, samo proveravamo da li je trenutni slot zauzet
                        isAvailable = occupiedIntervals.stream().noneMatch(interval ->
                                // Provera da li je trenutni slot zauzet
                                time.isBefore(interval[1]) && time.isAfter(interval[0])
                        );
                    }
                    return isAvailable;
                })
                .map(LocalTime::toString)
                .collect(Collectors.toList());

        return availableTimes;
    }


    public BookingService createBooking(Integer serviceId, Integer eventId, LocalDate bookingDate, Time startTime, Duration duration) {
        // Dohvatamo servis i događaj iz baze
        System.out.println("usao service");
        edu.ftn.iss.eventplanner.entities.Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new IllegalArgumentException("Service not found"));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));
        System.out.println("nadjeno sve");

        // Kreiramo i čuvamo novu rezervaciju
        BookingService booking = new BookingService();
        booking.setService(service);
        booking.setEvent(event);
        booking.setBookingDate(bookingDate);
        booking.setStartTime(startTime);  // Čuvamo kao Time
        booking.setDuration(duration);  // Čuvamo kao Duration
        booking.setConfirmed(true);  // Može kasnije da se potvrdi

        return bookingServiceRepository.save(booking);
    }
}
