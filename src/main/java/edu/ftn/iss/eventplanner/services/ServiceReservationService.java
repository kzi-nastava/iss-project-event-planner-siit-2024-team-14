package edu.ftn.iss.eventplanner.services;

import edu.ftn.iss.eventplanner.dtos.ServiceReservationRequestDTO;
import edu.ftn.iss.eventplanner.repositories.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class ServiceReservationService {

    private final ServiceRepository serviceRepository;

    @Transactional
    public String reserveService(ServiceReservationRequestDTO requestDTO) {
        edu.ftn.iss.eventplanner.entities.Service service = serviceRepository.findById(requestDTO.getServiceId())
                .orElseThrow(() -> new IllegalArgumentException("Service not found"));

        // Validacija datuma (ne može biti u prošlosti)
        if (requestDTO.getDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Cannot book a service in the past");
        }

        // Validacija vremena
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime startTime = LocalTime.parse(requestDTO.getStartTime(), timeFormatter);
        LocalTime endTime = LocalTime.parse(requestDTO.getEndTime(), timeFormatter);

        if (endTime.isBefore(startTime)) {
            throw new IllegalArgumentException("End time must be after start time");
        }

        // Provera dostupnosti i ostala pravila mogu se dodati ovde

        return "Reservation successful for service: " + service.getName();
    }
}
