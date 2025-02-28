package edu.ftn.iss.eventplanner.services;
import edu.ftn.iss.eventplanner.dtos.serviceBooking.ApproveRequestDTO;
import edu.ftn.iss.eventplanner.dtos.serviceBooking.BookingServiceRequestsForProviderDTO;
import edu.ftn.iss.eventplanner.entities.*;
import edu.ftn.iss.eventplanner.enums.ReservationType;
import edu.ftn.iss.eventplanner.enums.Status;
import edu.ftn.iss.eventplanner.repositories.BookingServiceRepository;
import edu.ftn.iss.eventplanner.repositories.EventRepository;
import edu.ftn.iss.eventplanner.repositories.NotificationRepository;
import edu.ftn.iss.eventplanner.repositories.ServiceRepository;
import jakarta.mail.MessagingException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingServiceService {
    private final BookingServiceRepository bookingServiceRepository;
    private final EventRepository eventRepository;
    private final ServiceRepository serviceRepository;
    private final EmailService emailService;
    private final NotificationRepository notificationRepository;

    public BookingServiceService(BookingServiceRepository bookingServiceRepository, EventRepository eventRepository, ServiceRepository serviceRepository, EmailService emailService, NotificationRepository notificationRepository) {
        this.bookingServiceRepository = bookingServiceRepository;
        this.eventRepository = eventRepository;
        this.serviceRepository = serviceRepository;
        this.emailService = emailService;
        this.notificationRepository = notificationRepository;
    }

    public List<String> getAvailableStartTimes(Integer serviceId, LocalDate date, Integer optionalDuration) {
        // Dohvatamo zauzete slotove za dati servis i datum
        List<BookingService> bookedSlots = bookingServiceRepository.findConfirmedBookings(serviceId, date);

        // Radno vreme servisa
        LocalTime startOfDay = LocalTime.of(8, 0);
        LocalTime endOfDay = LocalTime.of(23, 45);

        // Lista zauzetih intervala (početak i kraj) - tj. kada su zauzeti termini
        List<LocalTime[]> occupiedIntervals = bookedSlots.stream()
                .map(b -> new LocalTime[] {
                        b.getStartTime().toLocalTime(),
                        b.getStartTime().toLocalTime().plusMinutes(b.getDuration().toMinutes())
                })
                .collect(Collectors.toList());

        // Generišemo sve moguće start time-ove na svakih 30 minuta
        List<LocalTime> allSlots = new ArrayList<>();
        for (LocalTime time = startOfDay; time.isBefore(endOfDay); time = time.plusMinutes(15)) {
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
        edu.ftn.iss.eventplanner.entities.Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new IllegalArgumentException("Service not found"));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        BookingService booking = new BookingService();
        booking.setService(service);
        booking.setEvent(event);
        booking.setBookingDate(bookingDate);
        booking.setStartTime(startTime);
        booking.setDuration(duration);
        if (service.getReservationType() == ReservationType.MANUAL) {
            booking.setConfirmed(Status.PENDING);

            Notification notification = new Notification();
            notification.setMessage("Check booking service request you have one new pending request");
            notification.setUser(service.getProvider());
            notification.setDate(LocalDate.now());
            notification.setRead(false);
            notificationRepository.save(notification);
        }else{
            booking.setConfirmed(Status.APPROVED);
        }

        bookingServiceRepository.save(booking);

        try {
            emailService.sendBookingNotification(event.getOrganizer().getEmail(), event.getName(), service.getName(), bookingDate, startTime, duration);
            emailService.sendBookingNotification(service.getProvider().getEmail(), event.getName(), service.getName(), bookingDate, startTime, duration);
        } catch (MessagingException e) {
            System.err.println("Failed to send booking notification email: " + e.getMessage());
        }

        return booking;
    }

    // Zakazani task koji se izvršava na svakih 5 minuta i kreira notifikacije za rezervacije koje su 1h pre početka
    @Scheduled(cron = "0 0,30 * * * *")
    public void createNotificationsForUpcomingBookings() {
        LocalTime now = LocalTime.now(); // Trenutno vreme
        LocalTime oneHourLater = now.plusHours(1); // Vreme za 1h kasnije
        LocalDate today = LocalDate.now(); // Trenutni datum

        // Konvertujemo LocalTime u Time (SQL format)
        Time sqlOneHourLater = Time.valueOf(oneHourLater);

        // Pronalazimo sve rezervacije koje počinju tačno za 1 sat
        List<BookingService> upcomingBookings = bookingServiceRepository.findBookingsStartingAt(sqlOneHourLater);
        for (BookingService booking : upcomingBookings) {
            Event event = booking.getEvent();
            User organizer = event.getOrganizer();
            System.out.println("Pronadjen event"+ event.getName());

            // Proveravamo da li notifikacija već postoji (da ne bismo duplirali)
            boolean alreadyExists = notificationRepository.existsByUserAndEventAndMessageContaining(organizer, event, "Reminder");
            if (!alreadyExists) {
                System.out.println("Pravimo event"+ event.getName());
                Notification notification = new Notification();
                notification.setUser(organizer);
                notification.setEvent(event);
                notification.setMessage("Reminder: Your event '" + event.getName() + "' has a booking starting in 1 hour.");
                notification.setDate(today);
                notification.setRead(false);
                System.out.println("cuvanje"+ event.getName());

                notificationRepository.save(notification);
                System.out.println("Notifikacija kreirana za: " + organizer.getEmail());
            }
        }

    }

    public BookingServiceRequestsForProviderDTO deleteRequest(ApproveRequestDTO dto) {

        BookingService booking = bookingServiceRepository.findById(dto.getRequestId())
                .orElseThrow(() -> new IllegalArgumentException("Booking request not found with ID: " + dto.getRequestId()));
        booking.setConfirmed(Status.REJECTED);
        bookingServiceRepository.save(booking);

        Notification notification = new Notification();
        notification.setDate(LocalDate.now());
        notification.setRead(false);
        notification.setUser(booking.getEvent().getOrganizer());
        notification.setEvent(booking.getEvent());
        notification.setMessage("Your reservation for service: " + booking.getService().getName()+ " , for your event: " + booking.getEvent().getName() + "has been rejected");
        notificationRepository.save(notification);

        return mapToDTO(booking);
    }

    public BookingServiceRequestsForProviderDTO approveRequest(ApproveRequestDTO dto) {
        BookingService booking = bookingServiceRepository.findById(dto.getRequestId())
                .orElseThrow(() -> new IllegalArgumentException("Booking request not found with ID: " + dto.getRequestId()));

        booking.setConfirmed(Status.APPROVED);
        bookingServiceRepository.save(booking);

        Notification notification = new Notification();
        notification.setDate(LocalDate.now());
        notification.setRead(false);
        notification.setUser(booking.getEvent().getOrganizer());
        notification.setEvent(booking.getEvent());
        notification.setMessage("Your reservation for service: " + booking.getService().getName() +
                ", for your event: " + booking.getEvent().getName() + " has been accepted");
        notificationRepository.save(notification);

        return mapToDTO(booking);
    }

    public List<BookingServiceRequestsForProviderDTO> getRequests() {
        return bookingServiceRepository.findByConfirmed(Status.PENDING).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<BookingServiceRequestsForProviderDTO> getAllBookings() {
        return bookingServiceRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private BookingServiceRequestsForProviderDTO mapToDTO(BookingService booking) {
        BookingServiceRequestsForProviderDTO dto = new BookingServiceRequestsForProviderDTO();
        dto.setId(booking.getId());
        dto.setService(booking.getService().getName());
        dto.setEvent(booking.getEvent().getName());
        dto.setBookingDate(booking.getBookingDate());
        dto.setStartTime(booking.getStartTime().toString());
        dto.setDuration(booking.getDuration().toMinutes());
        dto.setConfirmed(booking.getConfirmed());

        return dto;
    }
}
