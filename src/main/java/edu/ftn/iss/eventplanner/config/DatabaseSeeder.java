package edu.ftn.iss.eventplanner.config;
import edu.ftn.iss.eventplanner.entities.Event;
import edu.ftn.iss.eventplanner.entities.EventType;
import edu.ftn.iss.eventplanner.repositories.EventRepository;
import edu.ftn.iss.eventplanner.repositories.EventTypeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.List;

@Configuration
public class DatabaseSeeder {

    @Bean
    CommandLineRunner initDatabase(EventRepository eventRepository, EventTypeRepository eventTypeRepository) {
        return args -> {
            if (eventRepository.count() == 0) {
                // Dodavanje EventType-a
                EventType partyType = new EventType(null, "Party", "Social gathering", true);
                EventType theatreType = new EventType(null, "Theatre", "Performing arts", true);
                eventTypeRepository.saveAll(List.of(partyType, theatreType));

                // Kreiranje događaja
                List<Event> events = List.of(
                        new Event(null, "Birthday Party", "Entry with present", 50, "open", "Novi Sad", LocalDate.of(2025, 6, 2), LocalDate.of(2025, 6, 2), 200.0, partyType),
                        new Event(null, "Horse Riding", "For horse lovers, free entry", 30, "open", "Novi Sad", LocalDate.of(2025, 7, 25), LocalDate.of(2025, 7, 25), 500.0, partyType),
                        new Event(null, "Rooftop Theatre", "Free entry, bring popcorn and drinks :)", 100, "open", "Novi Sad", LocalDate.of(2025, 2, 16), LocalDate.of(2025, 2, 16), 150.0, theatreType),
                        new Event(null, "Bakery Opening", "Come with an empty stomach!", 80, "open", "Novi Sad", LocalDate.of(2025, 9, 1), LocalDate.of(2025, 9, 1), 300.0, partyType),
                        new Event(null, "Graduation Party", "All college graduates welcome :)", 60, "closed", "Novi Sad", LocalDate.of(2025, 1, 13), LocalDate.of(2025, 1, 13), 400.0, partyType)
                );

                eventRepository.saveAll(events);
                System.out.println("✅ Podaci ubačeni u bazu!");
            }
        };
    }
}
