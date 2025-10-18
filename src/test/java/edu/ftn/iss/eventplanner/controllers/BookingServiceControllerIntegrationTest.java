package edu.ftn.iss.eventplanner.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ftn.iss.eventplanner.dtos.serviceBooking.ApproveRequestDTO;
import edu.ftn.iss.eventplanner.dtos.serviceBooking.BookingServiceRequestDTO;
import edu.ftn.iss.eventplanner.entities.*;
import edu.ftn.iss.eventplanner.enums.ReservationType;
import edu.ftn.iss.eventplanner.enums.Status;
import edu.ftn.iss.eventplanner.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class BookingServiceControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookingServiceRepository bookingServiceRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private EventOrganizerRepository eventOrganizerRepository;

    @Autowired
    private ServiceAndProductProviderRepository serviceAndProductProviderRepository;

    private SolutionCategory category;
    private Service service;
    private Event event;
    private EventOrganizer organizer;
    private ServiceAndProductProvider provider;

    @BeforeEach
    void setup() {
        // Čišćenje baze pre svakog testa
        bookingServiceRepository.deleteAll();
        serviceRepository.deleteAll();
        eventRepository.deleteAll();

        // Kreiranje osnovnih entiteta za test
        category = new SolutionCategory();
        category.setName("Test Category");
        categoryRepository.save(category);

        organizer = new EventOrganizer();
        organizer.setEmail("sigma.skolica@gmail.com");

        event = new Event();
        event.setName("Test Event");
        event.setBudget(new Budget());
        event.setOrganizer(organizer);

        provider = new ServiceAndProductProvider();
        provider.setEmail("milicabosancic03@gmail.com");

        service = new Service();
        service.setName("Test Service");
        service.setReservationType(ReservationType.MANUAL);
        service.setDuration(Duration.ofHours(1));
        service.setCategory(category);
        service.setProvider(provider);

        // Snimanje entiteta u bazu
        organizer = eventOrganizerRepository.saveAndFlush(organizer);
        provider = serviceAndProductProviderRepository.saveAndFlush(provider);
        service = serviceRepository.saveAndFlush(service);
        event = eventRepository.saveAndFlush(event);
    }

    // ================= GET Endpoints =================

    @Test
    void testGetAvailableStartTimes_success() throws Exception {
        // Test GET /available-start-times za validan serviceId i datum
        mockMvc.perform(get("/api/bookings/available-start-times")
                        .param("serviceId", service.getId().toString())
                        .param("date", LocalDate.now().toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetBookingRequests_success() throws Exception {
        // Test GET /requests
        mockMvc.perform(get("/api/bookings/requests"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetAllBookings_success() throws Exception {
        // Test GET /all
        mockMvc.perform(get("/api/bookings/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    // ================= POST / PUT Rezervacije =================

    @Test
    void testReserveService_success() throws Exception {
        // Test POST /reserve sa validnim podacima
        BookingServiceRequestDTO dto = new BookingServiceRequestDTO();
        dto.setServiceId(service.getId());
        dto.setEventId(event.getId());
        dto.setBookingDate(LocalDate.now());
        dto.setStartTime("10:00");
        dto.setDuration(60);

        mockMvc.perform(post("/api/bookings/reserve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void testApproveRequest_success() throws Exception {
        // Test PUT /approve za PENDING rezervaciju
        BookingService booking = new BookingService();
        booking.setService(service);
        booking.setEvent(event);
        booking.setBookingDate(LocalDate.now());
        booking.setStartTime(Time.valueOf("10:00:00"));
        booking.setDuration(Duration.ofMinutes(60));
        booking.setConfirmed(Status.PENDING);
        bookingServiceRepository.save(booking);

        ApproveRequestDTO dto = new ApproveRequestDTO();
        dto.setRequestId(booking.getId());
        dto.setApproved("true");

        mockMvc.perform(put("/api/bookings/approve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.confirmed").value("APPROVED"));
    }

    @Test
    void testDeleteRequest_success() throws Exception {
        // Test PUT /delete za PENDING rezervaciju
        BookingService booking = new BookingService();
        booking.setService(service);
        booking.setEvent(event);
        booking.setBookingDate(LocalDate.now());
        booking.setStartTime(Time.valueOf("11:00:00"));
        booking.setDuration(Duration.ofMinutes(60));
        booking.setConfirmed(Status.PENDING);
        bookingServiceRepository.save(booking);

        ApproveRequestDTO dto = new ApproveRequestDTO();
        dto.setRequestId(booking.getId());

        mockMvc.perform(put("/api/bookings/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.confirmed").value("REJECTED"));
    }

    // ================= Validacija / Error Scenariji =================

    @Test
    void testReserveService_invalidServiceId_shouldReturnBadRequest() throws Exception {
        // Test rezervacije sa nepostojećim serviceId
        BookingServiceRequestDTO dto = new BookingServiceRequestDTO();
        dto.setServiceId(9999);
        dto.setEventId(event.getId());
        dto.setBookingDate(LocalDate.now());
        dto.setStartTime("10:00");
        dto.setDuration(60);

        mockMvc.perform(post("/api/bookings/reserve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testReserveService_pastDate_shouldReturnBadRequest() throws Exception {
        // Test rezervacije za prošli datum
        BookingServiceRequestDTO dto = new BookingServiceRequestDTO();
        dto.setServiceId(service.getId());
        dto.setEventId(event.getId());
        dto.setBookingDate(LocalDate.now().minusDays(1));
        dto.setStartTime("10:00");
        dto.setDuration(60);

        mockMvc.perform(post("/api/bookings/reserve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testReserveService_conflictingTime_shouldReturnBadRequest() throws Exception {
        // Test rezervacije kada termin već postoji
        BookingService booking = new BookingService();
        booking.setService(service);
        booking.setEvent(event);
        booking.setBookingDate(LocalDate.now());
        booking.setStartTime(Time.valueOf("10:00:00"));
        booking.setDuration(Duration.ofMinutes(60));
        booking.setConfirmed(Status.APPROVED);
        bookingServiceRepository.save(booking);

        BookingServiceRequestDTO dto = new BookingServiceRequestDTO();
        dto.setServiceId(service.getId());
        dto.setEventId(event.getId());
        dto.setBookingDate(LocalDate.now());
        dto.setStartTime("10:00");
        dto.setDuration(60);

        mockMvc.perform(post("/api/bookings/reserve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testReserveService_invalidParameters_shouldReturnBadRequest() throws Exception {
        // Test rezervacije sa nevalidnim parametrima
        BookingServiceRequestDTO dto = new BookingServiceRequestDTO();
        dto.setServiceId(999);
        dto.setEventId(event.getId());
        dto.setBookingDate(LocalDate.now());
        dto.setStartTime("99:99");
        dto.setDuration(0);

        mockMvc.perform(post("/api/bookings/reserve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testReserveService_multipleConflicts_shouldReturnBadRequest() throws Exception {
        // Test rezervacije koja preklapa više potvrđenih termina
        BookingService b1 = new BookingService();
        b1.setService(service);
        b1.setEvent(event);
        b1.setBookingDate(LocalDate.now());
        b1.setStartTime(Time.valueOf("10:00:00"));
        b1.setDuration(Duration.ofMinutes(60));
        b1.setConfirmed(Status.APPROVED);
        bookingServiceRepository.save(b1);

        BookingService b2 = new BookingService();
        b2.setService(service);
        b2.setEvent(event);
        b2.setBookingDate(LocalDate.now());
        b2.setStartTime(Time.valueOf("11:00:00"));
        b2.setDuration(Duration.ofMinutes(60));
        b2.setConfirmed(Status.APPROVED);
        bookingServiceRepository.save(b2);

        BookingServiceRequestDTO dto = new BookingServiceRequestDTO();
        dto.setServiceId(service.getId());
        dto.setEventId(event.getId());
        dto.setBookingDate(LocalDate.now());
        dto.setStartTime("10:30");
        dto.setDuration(120);

        mockMvc.perform(post("/api/bookings/reserve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }
}
