package edu.ftn.iss.eventplanner.services;

import edu.ftn.iss.eventplanner.dtos.serviceBooking.ApproveRequestDTO;
import edu.ftn.iss.eventplanner.dtos.serviceBooking.BookingServiceRequestsForProviderDTO;
import edu.ftn.iss.eventplanner.entities.*;
import edu.ftn.iss.eventplanner.enums.ReservationType;
import edu.ftn.iss.eventplanner.enums.Status;
import edu.ftn.iss.eventplanner.repositories.*;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class BookingServiceServiceTest {

    // ======== Mock dependencies ========
    @Mock private BookingServiceRepository bookingServiceRepository;
    @Mock private ServiceRepository serviceRepository;
    @Mock private EventRepository eventRepository;
    @Mock private SolutionBookingRepository solutionBookingRepository;
    @Mock private NotificationRepository notificationRepository;
    @Mock private EmailService emailService;

    @InjectMocks
    private BookingServiceService bookingServiceService;

    private Service service;
    private Event event;
    private EventOrganizer organizer;
    private ServiceAndProductProvider provider;

    // ======== Setup before each test ========
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        organizer = new EventOrganizer();
        organizer.setId(1);
        organizer.setEmail("org@example.com");

        event = new Event();
        event.setId(1);
        event.setName("Test Event");
        event.setOrganizer(organizer);

        provider = new ServiceAndProductProvider();
        provider.setId(10);
        provider.setEmail("provider@example.com");

        service = new Service();
        service.setId(1);
        service.setReservationType(ReservationType.MANUAL);
        service.setDuration(Duration.ofHours(1));
        service.setProvider(provider);
    }

    // ============================
    // ===== getAvailableStartTimes =====
    // ============================

    // Test kada nema rezervacija – svi slotovi su dostupni
    @Test
    void testGetAvailableStartTimes_NoBookings() {
        when(bookingServiceRepository.findConfirmedBookings(anyInt(), any())).thenReturn(List.of());
        List<String> slots = bookingServiceService.getAvailableStartTimes(1, LocalDate.now(), null);

        assertFalse(slots.isEmpty());
        assertTrue(slots.stream().allMatch(s -> s.matches("\\d{2}:\\d{2}")));
        verify(bookingServiceRepository).findConfirmedBookings(eq(1), any(LocalDate.class));
    }

    // Test kada postoje rezervacije – proverava se izbacivanje preklapajućih slotova
    @Test
    void testGetAvailableStartTimes_RespectsOptionalDuration_AndExcludesOverlaps() {
        BookingService existing = new BookingService();
        existing.setId(100);
        existing.setService(service);
        existing.setEvent(event);
        existing.setBookingDate(LocalDate.now());
        existing.setStartTime(Time.valueOf("12:00:00"));
        existing.setDuration(Duration.ofMinutes(60));
        existing.setConfirmed(Status.APPROVED);

        when(bookingServiceRepository.findConfirmedBookings(eq(1), any(LocalDate.class)))
                .thenReturn(List.of(existing));

        List<String> slots = bookingServiceService.getAvailableStartTimes(1, LocalDate.now(), 60);

        // slotovi koji se preklapaju sa 12:00–13:00 ne smeju da postoje
        assertFalse(slots.contains("12:00"));
        assertFalse(slots.contains("11:30"));
        assertFalse(slots.contains("12:30"));
        // ali npr. 13:30 sme da postoji
        assertTrue(slots.contains("13:30"));
    }

    // Test za različite trajanja (duration)
    @Test
    void testGetAvailableStartTimes_VariousDurations() {
        BookingService existing = new BookingService();
        existing.setService(service);
        existing.setBookingDate(LocalDate.now());
        existing.setStartTime(Time.valueOf("12:00:00"));
        existing.setDuration(Duration.ofMinutes(60));
        existing.setConfirmed(Status.APPROVED);

        when(bookingServiceRepository.findConfirmedBookings(eq(service.getId()), any(LocalDate.class)))
                .thenReturn(List.of(existing));

        // duration = 30
        List<String> slots30 = bookingServiceService.getAvailableStartTimes(service.getId(), LocalDate.now(), 30);
        assertFalse(slots30.contains("12:00"));
        assertTrue(slots30.contains("13:30"));

        // duration = 60
        List<String> slots60 = bookingServiceService.getAvailableStartTimes(service.getId(), LocalDate.now(), 60);
        assertFalse(slots60.contains("12:30"));
    }

    // ============================
    // ===== createBooking =====
    // ============================

    // Manual reservation → PENDING + notifikacija + email
    @Test
    void testCreateBooking_ManualReservation() throws MessagingException {
        when(serviceRepository.findById(1)).thenReturn(Optional.of(service));
        when(eventRepository.findById(1)).thenReturn(Optional.of(event));
        when(bookingServiceRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(notificationRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        BookingService booking = bookingServiceService.createBooking(
                1, 1, LocalDate.now(), Time.valueOf("10:00:00"), Duration.ofHours(1)
        );

        assertEquals(Status.PENDING, booking.getConfirmed());
        verify(notificationRepository, times(1)).save(any(Notification.class));
        verify(solutionBookingRepository, never()).save(any());
        verify(emailService).sendBookingNotification(
                eq("org@example.com"),
                eq("Test Event"),
                eq(null),
                eq(LocalDate.of(2025,9,11)),
                eq(Time.valueOf("10:00:00")),
                eq(Duration.ofHours(1))
        );
    }

    // Automatic reservation → APPROVED + SolutionBooking kreiran
    @Test
    void testCreateBooking_AutoReservation() {
        service.setReservationType(ReservationType.AUTOMATIC);

        when(serviceRepository.findById(1)).thenReturn(Optional.of(service));
        when(eventRepository.findById(1)).thenReturn(Optional.of(event));
        when(bookingServiceRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(solutionBookingRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        BookingService booking = bookingServiceService.createBooking(
                1, 1, LocalDate.now(), Time.valueOf("11:00:00"), Duration.ofHours(1)
        );

        assertEquals(Status.APPROVED, booking.getConfirmed());
        verify(solutionBookingRepository, times(1)).save(any(SolutionBooking.class));
    }

    // Kreiranje booking-a kada servis ne postoji → IllegalArgumentException
    @Test
    void testCreateBooking_ServiceNotFound() {
        when(serviceRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                bookingServiceService.createBooking(
                        1, 1, LocalDate.now(), Time.valueOf("10:00:00"), Duration.ofHours(1)
                )
        );
    }

    // Kreiranje booking-a kada event ne postoji → IllegalArgumentException
    @Test
    void testCreateBooking_EventNotFound() {
        when(serviceRepository.findById(1)).thenReturn(Optional.of(service));
        when(eventRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                bookingServiceService.createBooking(
                        1, 1, LocalDate.now(), Time.valueOf("10:00:00"), Duration.ofHours(1)
                )
        );
    }

    // Booking u prošlosti → IllegalArgumentException
    @Test
    void testCreateBooking_InPast_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            bookingServiceService.createBooking(service.getId(), event.getId(), LocalDate.now().minusDays(1),Time.valueOf("10:00:00"), Duration.ofHours(1));
        });
    }

    // Preklapanje sa CONFIRMED → baca exception
    @Test
    void testCreateBooking_OverlapWithConfirmed_Throws() {
        BookingService confirmedBooking = new BookingService();
        confirmedBooking.setService(service);
        confirmedBooking.setEvent(event);
        confirmedBooking.setBookingDate(LocalDate.now());
        confirmedBooking.setStartTime(Time.valueOf("10:00:00"));
        confirmedBooking.setDuration(Duration.ofHours(1));
        confirmedBooking.setConfirmed(Status.APPROVED);

        when(bookingServiceRepository.findConfirmedBookings(eq(service.getId()), any(LocalDate.class)))
                .thenReturn(List.of(confirmedBooking));

        assertThrows(IllegalArgumentException.class, () -> {
            bookingServiceService.createBooking(service.getId(), event.getId(),
                    LocalDate.now(), Time.valueOf("10:30:00"), Duration.ofMinutes(30));
        });
    }

    // Preklapanje sa PENDING → dozvoljeno, status ostaje PENDING
    @Test
    void testCreateBooking_OverlapWithPending_Allows() {
        BookingService pendingBooking = new BookingService();
        pendingBooking.setService(service);
        pendingBooking.setEvent(event);
        pendingBooking.setBookingDate(LocalDate.now());
        pendingBooking.setStartTime(Time.valueOf("12:00:00"));
        pendingBooking.setDuration(Duration.ofHours(1));
        pendingBooking.setConfirmed(Status.PENDING);

        when(bookingServiceRepository.findConfirmedBookings(eq(service.getId()), any(LocalDate.class)))
                .thenReturn(List.of());

        when(serviceRepository.findById(service.getId())).thenReturn(Optional.of(service));
        when(eventRepository.findById(event.getId())).thenReturn(Optional.of(event));
        when(bookingServiceRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(notificationRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        BookingService created = bookingServiceService.createBooking(
                service.getId(),
                event.getId(),
                LocalDate.now(),
                Time.valueOf("12:30:00"),
                Duration.ofMinutes(30)
        );

        assertEquals(Status.PENDING, created.getConfirmed());
        assertTrue(created.getDuration().toMinutes() > 0);
    }

    // Email test → proverava slanje email-ova i provideru i organizatoru
    @Test
    void testCreateBooking_EmailSentToProviderAndOrganizer() throws Exception {
        when(serviceRepository.findById(1)).thenReturn(Optional.of(service));
        when(eventRepository.findById(1)).thenReturn(Optional.of(event));
        when(bookingServiceRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(notificationRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        bookingServiceService.createBooking(1, 1, LocalDate.of(2025,11,29), Time.valueOf("10:00:00"), Duration.ofHours(1));

        verify(emailService).sendBookingNotification(eq("org@example.com"), eq("Test Event"), eq(null),
                eq(LocalDate.of(2025,11,29)), eq(Time.valueOf("10:00:00")), eq(Duration.ofHours(1)));
        verify(emailService).sendBookingNotification(eq("provider@example.com"), eq("Test Event"), eq(null),
                eq(LocalDate.of(2025,11,29)), eq(Time.valueOf("10:00:00")), eq(Duration.ofHours(1)));
    }

    // ============================
    // ===== approveRequest =====
    // ============================

    // Uspešno odobrenje, kreira SolutionBooking i notifikaciju
    @Test
    void testApproveRequest_Success_NoOverlap_CreatesSolutionBookingAndNotification() {
        BookingService pending = new BookingService();
        pending.setId(77);
        pending.setService(service);
        pending.setEvent(event);
        pending.setBookingDate(LocalDate.now());
        pending.setStartTime(Time.valueOf("10:00:00"));
        pending.setDuration(Duration.ofMinutes(60));
        pending.setConfirmed(Status.PENDING);

        ApproveRequestDTO dto = new ApproveRequestDTO();
        dto.setRequestId(77);

        when(bookingServiceRepository.findById(77)).thenReturn(Optional.of(pending));

        BookingService existing = new BookingService();
        existing.setId(200);
        existing.setService(service);
        existing.setEvent(event);
        existing.setBookingDate(LocalDate.now());
        existing.setStartTime(Time.valueOf("08:00:00"));
        existing.setDuration(Duration.ofMinutes(60));
        existing.setConfirmed(Status.APPROVED);

        when(bookingServiceRepository.findConfirmedBookings(eq(service.getId()), eq(pending.getBookingDate())))
                .thenReturn(List.of(existing));

        when(bookingServiceRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(solutionBookingRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(notificationRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(notificationRepository.existsByUserAndEventAndMessageContaining(any(), any(), anyString())).thenReturn(false);

        BookingServiceRequestsForProviderDTO result = bookingServiceService.approveRequest(dto);

        assertEquals(Status.APPROVED, result.getConfirmed());
        verify(solutionBookingRepository, times(1)).save(any(SolutionBooking.class));
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    // Preklapanje → exception
    @Test
    void testApproveRequest_Overlap_Throws() {
        BookingService pending = new BookingService();
        pending.setId(88);
        pending.setService(service);
        pending.setEvent(event);
        pending.setBookingDate(LocalDate.now());
        pending.setStartTime(Time.valueOf("10:00:00"));
        pending.setDuration(Duration.ofMinutes(60));
        pending.setConfirmed(Status.PENDING);

        ApproveRequestDTO dto = new ApproveRequestDTO();
        dto.setRequestId(88);

        when(bookingServiceRepository.findById(88)).thenReturn(Optional.of(pending));

        BookingService overlapping = new BookingService();
        overlapping.setId(201);
        overlapping.setService(service);
        overlapping.setEvent(event);
        overlapping.setBookingDate(LocalDate.now());
        overlapping.setStartTime(Time.valueOf("10:30:00"));
        overlapping.setDuration(Duration.ofMinutes(60));
        overlapping.setConfirmed(Status.APPROVED);

        when(bookingServiceRepository.findConfirmedBookings(eq(service.getId()), eq(pending.getBookingDate())))
                .thenReturn(List.of(overlapping));

        assertThrows(IllegalArgumentException.class, () -> bookingServiceService.approveRequest(dto));

        verify(solutionBookingRepository, never()).save(any());
        verify(notificationRepository, never()).save(any());
    }

    // Već odobren booking → exception
    @Test
    void testApproveRequest_AlreadyApproved_Throws() {
        BookingService approved = new BookingService();
        approved.setId(123);
        approved.setService(service);
        approved.setEvent(event);
        approved.setBookingDate(LocalDate.now());
        approved.setStartTime(Time.valueOf("09:00:00"));
        approved.setDuration(Duration.ofMinutes(60));
        approved.setConfirmed(Status.APPROVED);

        ApproveRequestDTO dto = new ApproveRequestDTO();
        dto.setRequestId(123);

        when(bookingServiceRepository.findById(123)).thenReturn(Optional.of(approved));

        assertThrows(IllegalStateException.class,
                () -> bookingServiceService.approveRequest(dto));

        verify(solutionBookingRepository, never()).save(any());
        verify(notificationRepository, never()).save(any());
    }

    // ============================
    // ===== deleteRequest =====
    // ============================

    // Uspešno brisanje → status REJECTED + notifikacija
    @Test
    void testDeleteRequest_SetsRejected_AndNotifies() {
        BookingService pending = new BookingService();
        pending.setId(99);
        pending.setService(service);
        pending.setEvent(event);
        pending.setBookingDate(LocalDate.now());
        pending.setStartTime(Time.valueOf("14:00:00"));
        pending.setDuration(Duration.ofMinutes(60));
        pending.setConfirmed(Status.PENDING);

        ApproveRequestDTO dto = new ApproveRequestDTO();
        dto.setRequestId(99);

        when(bookingServiceRepository.findById(99)).thenReturn(Optional.of(pending));
        when(bookingServiceRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(notificationRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        BookingServiceRequestsForProviderDTO out = bookingServiceService.deleteRequest(dto);

        assertEquals(Status.REJECTED, out.getConfirmed());
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    // Brisanje nepostojećeg booking-a → exception
    @Test
    void testDeleteRequest_NotFound_Throws() {
        ApproveRequestDTO dto = new ApproveRequestDTO();
        dto.setRequestId(999);

        when(bookingServiceRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> bookingServiceService.deleteRequest(dto));
    }

    // ============================
    // ===== getRequests / getAllBookings (DTO mapiranje) =====
    // ============================

    @Test
    void testGetRequests_ReturnsPendingMapped() {
        BookingService b = new BookingService();
        b.setId(321);
        b.setService(service);
        b.setEvent(event);
        b.setBookingDate(LocalDate.now());
        b.setStartTime(Time.valueOf("09:00:00"));
        b.setDuration(Duration.ofMinutes(45));
        b.setConfirmed(Status.PENDING);

        when(bookingServiceRepository.findByConfirmed(Status.PENDING)).thenReturn(List.of(b));

        List<BookingServiceRequestsForProviderDTO> list = bookingServiceService.getRequests();
        assertEquals(1, list.size());
        assertEquals(321, list.get(0).getId());
        assertEquals(Status.PENDING, list.get(0).getConfirmed());
        assertNotNull(list.get(0).getStartTime());
    }

    @Test
    void testGetAllBookings_ReturnsAllMapped() {
        BookingService b1 = new BookingService();
        b1.setId(1);
        b1.setService(service);
        b1.setEvent(event);
        b1.setBookingDate(LocalDate.now());
        b1.setStartTime(Time.valueOf("10:00:00"));
        b1.setDuration(Duration.ofMinutes(60));
        b1.setConfirmed(Status.APPROVED);

        BookingService b2 = new BookingService();
        b2.setId(2);
        b2.setService(service);
        b2.setEvent(event);
        b2.setBookingDate(LocalDate.now());
        b2.setStartTime(Time.valueOf("12:00:00"));
        b2.setDuration(Duration.ofMinutes(30));
        b2.setConfirmed(Status.REJECTED);

        when(bookingServiceRepository.findAll()).thenReturn(List.of(b1, b2));

        List<BookingServiceRequestsForProviderDTO> list = bookingServiceService.getAllBookings();
        assertEquals(2, list.size());
        assertEquals(1, list.get(0).getId());
        assertEquals(2, list.get(1).getId());
    }

    // ============================
    // ===== @Scheduled createNotificationsForUpcomingBookings =====
    // ============================

    // Kreira notifikaciju ako ne postoji
    @Test
    void testCreateNotificationsForUpcomingBookings_CreatesWhenMissing() {
        BookingService upcoming = new BookingService();
        upcoming.setId(555);
        upcoming.setService(service);
        upcoming.setEvent(event);
        upcoming.setBookingDate(LocalDate.now());
        upcoming.setStartTime(Time.valueOf(LocalTime.now().plusHours(1)));
        upcoming.setDuration(Duration.ofMinutes(30));
        upcoming.setConfirmed(Status.APPROVED);

        when(bookingServiceRepository.findBookingsStartingAt(any(Time.class)))
                .thenReturn(List.of(upcoming));
        when(notificationRepository.existsByUserAndEventAndMessageContaining(any(User.class), any(Event.class), contains("Reminder")))
                .thenReturn(false);

        bookingServiceService.createNotificationsForUpcomingBookings();

        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    // Ne duplicira notifikaciju ako već postoji
    @Test
    void testCreateNotificationsForUpcomingBookings_DoesNotDuplicate() {
        BookingService upcoming = new BookingService();
        upcoming.setId(556);
        upcoming.setService(service);
        upcoming.setEvent(event);
        upcoming.setBookingDate(LocalDate.now());
        upcoming.setStartTime(Time.valueOf(LocalTime.now().plusHours(1)));
        upcoming.setDuration(Duration.ofMinutes(30));
        upcoming.setConfirmed(Status.APPROVED);

        when(bookingServiceRepository.findBookingsStartingAt(any(Time.class)))
                .thenReturn(List.of(upcoming));
        when(notificationRepository.existsByUserAndEventAndMessageContaining(any(User.class), any(Event.class), contains("Reminder")))
                .thenReturn(true);

        bookingServiceService.createNotificationsForUpcomingBookings();

        verify(notificationRepository, never()).save(any(Notification.class));
    }

}
