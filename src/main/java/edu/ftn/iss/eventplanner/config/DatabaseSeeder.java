package edu.ftn.iss.eventplanner.config;

import edu.ftn.iss.eventplanner.entities.Event;
import edu.ftn.iss.eventplanner.entities.EventOrganizer;
import edu.ftn.iss.eventplanner.entities.EventType;
import edu.ftn.iss.eventplanner.repositories.EventRepository;
import edu.ftn.iss.eventplanner.repositories.EventTypeRepository;
import edu.ftn.iss.eventplanner.repositories.EventOrganizerRepository;
import edu.ftn.iss.eventplanner.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.List;

@Configuration
public class DatabaseSeeder {

    @Bean
    CommandLineRunner initDatabase(EventRepository eventRepository,
                                   EventTypeRepository eventTypeRepository,
                                   UserRepository userRepository) { // Koristi UserRepository
        return args -> {
            System.out.println("🔍 Provera podataka u bazi...");

            // Proveri da li već postoji tip događaja
            EventType partyType = eventTypeRepository.findByName("Party")
                    .orElseGet(() -> eventTypeRepository.save(new EventType(null, "Party", "Social gathering", true, null)));
            EventType theatreType = eventTypeRepository.findByName("Theatre")
                    .orElseGet(() -> eventTypeRepository.save(new EventType(null, "Theatre", "Performing arts", true, null)));

            // Proveri da li već postoji organizator
            EventOrganizer organizer = (EventOrganizer) userRepository.findByEmail("organizer@example.com")
                    .orElseGet(() -> {
                        EventOrganizer newOrganizer = new EventOrganizer();
                        newOrganizer.setEmail("organizer@example.com");
                        newOrganizer.setPassword("securepassword");
                        newOrganizer.setFirstName("John");
                        newOrganizer.setLastName("Doe");
                        newOrganizer.setProfilePicture("default.png");
                        newOrganizer.setVerified(true);
                        newOrganizer.setSuspended(false);
                        return userRepository.save(newOrganizer);
                    });

            // Proveri da li postoji događaj pre nego što ga dodaš
            if (eventRepository.findByName("Birthday Party").isEmpty()) {
                eventRepository.save(new Event(null, organizer, "Birthday Party", "Entry with present", 50, "open", "Novi Sad",
                        LocalDate.of(2025, 6, 2), LocalDate.of(2025, 6, 2), partyType));
            }

            if (eventRepository.findByName("Horse Riding").isEmpty()) {
                eventRepository.save(new Event(null, organizer, "Horse Riding", "For horse lovers, free entry", 30, "open", "Novi Sad",
                        LocalDate.of(2025, 7, 25), LocalDate.of(2025, 7, 25), partyType));
            }

            System.out.println("✅ Provera završena, dodati novi podaci ako su nedostajali.");
        };
    }

}
