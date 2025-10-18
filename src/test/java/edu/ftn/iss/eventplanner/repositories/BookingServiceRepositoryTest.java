package edu.ftn.iss.eventplanner.repositories;

import edu.ftn.iss.eventplanner.entities.BookingService;
import edu.ftn.iss.eventplanner.entities.Event;
import edu.ftn.iss.eventplanner.entities.Service;
import edu.ftn.iss.eventplanner.entities.SolutionCategory;
import edu.ftn.iss.eventplanner.enums.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookingServiceRepositoryTest {

    private BookingServiceRepository bookingServiceRepository;

    private BookingService booking1;
    private BookingService booking2;
    private Service service;
    private Event event;
    private SolutionCategory category;

    @BeforeEach
    public void setup() {
        // Kreiramo mock repozitorijum
        bookingServiceRepository = mock(BookingServiceRepository.class);

        // Kreiramo entitete
        category = new SolutionCategory();
        category.setName("Test Category");

        service = new Service();
        service.setName("Test Service");
        service.setCategory(category);

        event = new Event();
        event.setName("Test Event");

        // Booking 1: potvrđen
        booking1 = new BookingService();
        booking1.setId(1);
        booking1.setService(service);
        booking1.setEvent(event);
        booking1.setBookingDate(LocalDate.now());
        booking1.setConfirmed(Status.APPROVED);
        booking1.setStartTime(Time.valueOf("10:00:00"));
        booking1.setDuration(Duration.ofHours(2));

        // Booking 2: nepotvrđen
        booking2 = new BookingService();
        booking2.setId(2);
        booking2.setService(service);
        booking2.setEvent(event);
        booking2.setBookingDate(LocalDate.now());
        booking2.setConfirmed(Status.PENDING);
        booking2.setStartTime(Time.valueOf("12:00:00"));
        booking2.setDuration(Duration.ofHours(1));

        // Mock ponašanja metoda
        when(bookingServiceRepository.findById(1)).thenReturn(Optional.of(booking1));

        when(bookingServiceRepository.findConfirmedBookings(service.getId(), LocalDate.now()))
                .thenReturn(Arrays.asList(booking1));

        when(bookingServiceRepository.findBookingsStartingAt(Time.valueOf("10:00:00")))
                .thenReturn(Arrays.asList(booking1));

        when(bookingServiceRepository.findByConfirmed(Status.APPROVED))
                .thenReturn(Arrays.asList(booking1));
        when(bookingServiceRepository.findByConfirmed(Status.PENDING))
                .thenReturn(Arrays.asList(booking2));
    }

    @Test
    public void testFindById() {
        Optional<BookingService> bsOpt = bookingServiceRepository.findById(1);
        assertTrue(bsOpt.isPresent());
        BookingService bs = bsOpt.get();
        assertNotNull(bs.getService());
        assertNotNull(bs.getEvent());

        verify(bookingServiceRepository, times(1)).findById(1);
    }

    @Test
    public void testFindConfirmedBookings() {
        List<BookingService> confirmedBookings = bookingServiceRepository
                .findConfirmedBookings(service.getId(), LocalDate.now());

        assertEquals(1, confirmedBookings.size());
        assertEquals(Status.APPROVED, confirmedBookings.get(0).getConfirmed());

        verify(bookingServiceRepository, times(1))
                .findConfirmedBookings(service.getId(), LocalDate.now());
    }

    @Test
    public void testFindBookingsStartingAt() {
        List<BookingService> bookingsAt10 = bookingServiceRepository
                .findBookingsStartingAt(Time.valueOf("10:00:00"));

        assertEquals(1, bookingsAt10.size());
        assertEquals(booking1.getId(), bookingsAt10.get(0).getId());

        verify(bookingServiceRepository, times(1))
                .findBookingsStartingAt(Time.valueOf("10:00:00"));
    }

    @Test
    public void testFindByConfirmed() {
        List<BookingService> approvedBookings = bookingServiceRepository.findByConfirmed(Status.APPROVED);
        List<BookingService> pendingBookings = bookingServiceRepository.findByConfirmed(Status.PENDING);

        assertTrue(approvedBookings.stream().anyMatch(b -> b.getId().equals(booking1.getId())));
        assertTrue(pendingBookings.stream().anyMatch(b -> b.getId().equals(booking2.getId())));

        verify(bookingServiceRepository, times(1)).findByConfirmed(Status.APPROVED);
        verify(bookingServiceRepository, times(1)).findByConfirmed(Status.PENDING);
    }
}
