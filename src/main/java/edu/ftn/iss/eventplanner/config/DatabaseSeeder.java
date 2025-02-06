package edu.ftn.iss.eventplanner.config;

import edu.ftn.iss.eventplanner.entities.*;
import edu.ftn.iss.eventplanner.enums.Status;
import edu.ftn.iss.eventplanner.repositories.*;
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
                                   UserRepository userRepository,
                                   SolutionRepository solutionRepository,
                                   CategoryRepository solutionCategoryRepository,
                                   ServiceAndProductProviderRepository providerRepository) {
        return args -> {
            System.out.println("🔍 Provera podataka u bazi...");

            // Proveri i dodaj EventType
            EventType partyType = eventTypeRepository.findByName("Party")
                    .orElseGet(() -> eventTypeRepository.save(new EventType(null, "Party", "Social gathering", true, null)));
            EventType theatreType = eventTypeRepository.findByName("Theatre")
                    .orElseGet(() -> eventTypeRepository.save(new EventType(null, "Theatre", "Performing arts", true, null)));


            EventOrganizer organizer = (EventOrganizer) userRepository.findByEmail("organizer1@example.com")
                    .orElseGet(() -> {
                        EventOrganizer newOrganizer = new EventOrganizer();
                        newOrganizer.setEmail("organizer1@example.com");
                        newOrganizer.setPassword("securepassword");
                        newOrganizer.setFirstName("John");
                        newOrganizer.setLastName("Doe");
                        newOrganizer.setProfilePicture("assets/images/profile1.png");
                        newOrganizer.setVerified(true);
                        newOrganizer.setSuspended(false);
                        return userRepository.save(newOrganizer);
                    });

            // Dodavanje događaja
            if (eventRepository.findByName("Birthday Party").isEmpty()) {
                eventRepository.save(new Event(null, organizer, "Birthday Party", "Entry with present", 50, "open", "Novi Sad",
                        LocalDate.of(2025, 6, 2), LocalDate.of(2025, 6, 2), "assets/images/event1.png", partyType));
            }

            if (eventRepository.findByName("Horse Riding").isEmpty()) {
                eventRepository.save(new Event(null, organizer, "Horse Riding", "For horse lovers, free entry", 30, "open", "Novi Sad",
                        LocalDate.of(2025, 7, 25), LocalDate.of(2025, 7, 25), "assets/images/event2.png", partyType));
            }

            if (eventRepository.findByName("Bakery Opening").isEmpty()) {
                eventRepository.save(new Event(null, organizer, "Bakery opening", "Come with an empty stomach!", 30, "open", "Novi Sad",
                        LocalDate.of(2025, 7, 25), LocalDate.of(2025, 7, 25), "assets/images/event3.png", partyType));
            }

            if (eventRepository.findByName("Rooftop theatre").isEmpty()) {
                eventRepository.save(new Event(null, organizer, "Rooftop theatre", "Free entry, bring popcorn and drinks!", 30, "open", "Novi Sad",
                        LocalDate.of(2025, 7, 25), LocalDate.of(2025, 7, 25),"assets/images/event4.png", partyType));
            }

            if (eventRepository.findByName("Graduation party").isEmpty()) {
                eventRepository.save(new Event(null, organizer, "Graduation party", "All college graduates wellcome :)", 30, "open", "Novi Sad",
                        LocalDate.of(2025, 7, 25), LocalDate.of(2025, 7, 25), "assets/images/event5.png", partyType));
            }


            // Dodavanje SolutionCategory
            SolutionCategory decorationCategory = solutionCategoryRepository.findByName("Decoration")
                    .orElseGet(() -> solutionCategoryRepository.save(new SolutionCategory(
                            null,
                            "Decoration",
                            "Event decoration services",
                            null,
                            Status.APPROVED
                    )));

            SolutionCategory cateringCategory = solutionCategoryRepository.findByName("Catering")
                    .orElseGet(() -> solutionCategoryRepository.save(new SolutionCategory(
                            null,
                            "Catering",
                            "Food and beverage services for events",
                            null,
                            Status.APPROVED
                    )));

            SolutionCategory lightingCategory = solutionCategoryRepository.findByName("Lighting")
                    .orElseGet(() -> solutionCategoryRepository.save(new SolutionCategory(
                            null,
                            "Lighting",
                            "Professional event lighting solutions",
                            null,
                            Status.APPROVED
                    )));

            SolutionCategory musicCategory = solutionCategoryRepository.findByName("Music")
                    .orElseGet(() -> solutionCategoryRepository.save(new SolutionCategory(
                            null,
                            "Music",
                            "Live bands and DJ services",
                            null,
                            Status.APPROVED
                    )));

            // Dodavanje ServiceAndProductProvider
            ServiceAndProductProvider provider = (ServiceAndProductProvider) userRepository.findByEmail("provider1@example.com")
                    .orElseGet(() -> {
                        ServiceAndProductProvider newProvider = new ServiceAndProductProvider();
                        newProvider.setEmail("provider1@example.com");
                        newProvider.setPassword("securepassword");
                        newProvider.setCompanyName("Event Solutions Ltd.");
                        newProvider.setContactInfo("123-456-7890");
                        newProvider.setDescription("We provide event planning solutions.");
                        newProvider.setPictures(List.of("assets/images/profile2.png"));
                        return userRepository.save(newProvider);
                    });

            ServiceAndProductProvider lightingProvider = (ServiceAndProductProvider) userRepository.findByEmail("lighting1@example.com")
                    .orElseGet(() -> {
                        ServiceAndProductProvider newProvider = new ServiceAndProductProvider();
                        newProvider.setEmail("lighting1@example.com");
                        newProvider.setPassword("securepassword");
                        newProvider.setCompanyName("Bright Lights Co.");
                        newProvider.setContactInfo("321-654-0987");
                        newProvider.setDescription("Providing top-notch lighting for all events.");
                        newProvider.setPictures(List.of("assets/images/profile3.png", "lights2.png"));
                        return userRepository.save(newProvider);
                    });

            ServiceAndProductProvider musicProvider = (ServiceAndProductProvider) userRepository.findByEmail("music@example.com")
                    .orElseGet(() -> {
                        ServiceAndProductProvider newProvider = new ServiceAndProductProvider();
                        newProvider.setEmail("music@example.com");
                        newProvider.setPassword("securepassword");
                        newProvider.setCompanyName("Ultimate Sound");
                        newProvider.setContactInfo("987-654-3210");
                        newProvider.setDescription("DJ and live band services for weddings and parties.");
                        newProvider.setPictures(List.of("dj1.png", "band1.png"));
                        return userRepository.save(newProvider);
                    });


            // Dodavanje Solution (proizvodi/usluge)
            if (solutionRepository.findByName("Balloon Decoration1").isEmpty()) {
                Solution balloonDecoration = new Solution();
                balloonDecoration.setName("Balloon Decoration1");
                balloonDecoration.setDescription("Colorful balloon decorations for all occasions.");
                balloonDecoration.setLocation("Belgrade");
                balloonDecoration.setPrice(150.0);
                balloonDecoration.setDiscount(10.0);
                balloonDecoration.setImageUrl("assets/images/service2.png");
                balloonDecoration.setAvailable(true);
                balloonDecoration.setVisible(true);
                balloonDecoration.setDeleted(false);
                balloonDecoration.setStatus(Status.APPROVED);
                balloonDecoration.setCategory(decorationCategory);
                balloonDecoration.setProvider(provider);

                solutionRepository.save(balloonDecoration);
            }

            if (solutionRepository.findByName("Gourmet Catering1").isEmpty()) {
                Solution gourmetCatering = new Solution();
                gourmetCatering.setName("Gourmet Catering1");
                gourmetCatering.setDescription("Exclusive gourmet catering for weddings and events.");
                gourmetCatering.setLocation("Novi Sad");
                gourmetCatering.setPrice(500.0);
                gourmetCatering.setDiscount(15.0);
                gourmetCatering.setImageUrl("assets/images/service3.png");
                gourmetCatering.setAvailable(true);
                gourmetCatering.setVisible(true);
                gourmetCatering.setDeleted(false);
                gourmetCatering.setStatus(Status.APPROVED);
                gourmetCatering.setCategory(cateringCategory);
                gourmetCatering.setProvider(provider);

                solutionRepository.save(gourmetCatering);
            }

            if (solutionRepository.findByName("Gourmet1").isEmpty()) {
                Solution gourmetCatering = new Solution();
                gourmetCatering.setName("Gourmet1");
                gourmetCatering.setDescription("Exclusive gourmet catering for weddings and events.");
                gourmetCatering.setLocation("Novi Sad");
                gourmetCatering.setPrice(500.0);
                gourmetCatering.setDiscount(15.0);
                gourmetCatering.setImageUrl("assets/images/service4.png");
                gourmetCatering.setAvailable(true);
                gourmetCatering.setVisible(true);
                gourmetCatering.setDeleted(false);
                gourmetCatering.setStatus(Status.APPROVED);
                gourmetCatering.setCategory(cateringCategory);
                gourmetCatering.setProvider(provider);

                solutionRepository.save(gourmetCatering);
            }

            if (solutionRepository.findByName("LED Stage Lighting1").isEmpty()) {
                Solution ledLighting = new Solution();
                ledLighting.setName("LED Stage Lighting1");
                ledLighting.setDescription("High-quality LED lighting for stage performances.");
                ledLighting.setLocation("Belgrade");
                ledLighting.setPrice(200.0);
                ledLighting.setDiscount(5.0);
                ledLighting.setImageUrl("assets/images/service5.png");
                ledLighting.setAvailable(true);
                ledLighting.setVisible(true);
                ledLighting.setDeleted(false);
                ledLighting.setStatus(Status.APPROVED);
                ledLighting.setCategory(lightingCategory);
                ledLighting.setProvider(lightingProvider);
                solutionRepository.save(ledLighting);
            }

            if (solutionRepository.findByName("Live Jazz Band1").isEmpty()) {
                Solution jazzBand = new Solution();
                jazzBand.setName("Live Jazz Band1");
                jazzBand.setDescription("Smooth jazz performances for elegant events.");
                jazzBand.setLocation("Novi Sad");
                jazzBand.setPrice(600.0);
                jazzBand.setDiscount(20.0);
                jazzBand.setImageUrl("assets/images/service6.png");
                jazzBand.setAvailable(true);
                jazzBand.setVisible(true);
                jazzBand.setDeleted(false);
                jazzBand.setStatus(Status.APPROVED);
                jazzBand.setCategory(musicCategory);
                jazzBand.setProvider(musicProvider);
                solutionRepository.save(jazzBand);
            }

            if (solutionRepository.findByName("Wedding DJ1").isEmpty()) {
                Solution weddingDJ = new Solution();
                weddingDJ.setName("Wedding DJ1");
                weddingDJ.setDescription("Professional DJ with a wide range of music for weddings.");
                weddingDJ.setLocation("Subotica");
                weddingDJ.setPrice(400.0);
                weddingDJ.setDiscount(10.0);
                weddingDJ.setImageUrl("assets/images/service7.png");
                weddingDJ.setAvailable(true);
                weddingDJ.setVisible(true);
                weddingDJ.setDeleted(false);
                weddingDJ.setStatus(Status.APPROVED);
                weddingDJ.setCategory(musicCategory);
                weddingDJ.setProvider(musicProvider);
                solutionRepository.save(weddingDJ);
            }

            if (solutionRepository.findByName("Wedding1").isEmpty()) {
                Solution weddingDJ = new Solution();
                weddingDJ.setName("Wedding1");
                weddingDJ.setDescription("Professional DJ with a wide range of music for weddings.");
                weddingDJ.setLocation("Novi Sad");
                weddingDJ.setPrice(400.0);
                weddingDJ.setDiscount(10.0);
                weddingDJ.setImageUrl("assets/images/service8.png");
                weddingDJ.setAvailable(true);
                weddingDJ.setVisible(true);
                weddingDJ.setDeleted(false);
                weddingDJ.setStatus(Status.APPROVED);
                weddingDJ.setCategory(musicCategory);
                weddingDJ.setProvider(musicProvider);
                solutionRepository.save(weddingDJ);
            }

            System.out.println("✅ Podaci uspešno dodati u bazu.");
        };
    }
}
