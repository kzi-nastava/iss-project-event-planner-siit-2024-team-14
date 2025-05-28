package edu.ftn.iss.eventplanner.config;

import edu.ftn.iss.eventplanner.entities.*;
import edu.ftn.iss.eventplanner.enums.PrivacyType;
import edu.ftn.iss.eventplanner.enums.ReservationType;
import edu.ftn.iss.eventplanner.enums.Status;
import edu.ftn.iss.eventplanner.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class DatabaseSeeder {

    @Bean
    CommandLineRunner initDatabase(EventRepository eventRepository,
                                   EventTypeRepository eventTypeRepository,
                                   UserRepository userRepository,
                                   SolutionRepository solutionRepository,
                                   CategoryRepository solutionCategoryRepository,
                                   ServiceAndProductProviderRepository providerRepository,
                                   CommentRepository commentRepository, NotificationRepository notificationRepository) {
        return args -> {
            System.out.println("🔍 Provera podataka u bazi...");

            // Dodavanje SolutionCategory
            SolutionCategory decorationCategory = solutionCategoryRepository.findByName("Decoration")
                    .orElseGet(() -> solutionCategoryRepository.save(new SolutionCategory(
                            null,
                            "Decoration",
                            "Event decoration services",
                            Status.APPROVED
                    )));

            SolutionCategory cateringCategory = solutionCategoryRepository.findByName("Catering")
                    .orElseGet(() -> solutionCategoryRepository.save(new SolutionCategory(
                            null,
                            "Catering",
                            "Food and beverage services for events",
                            Status.APPROVED
                    )));

            SolutionCategory lightingCategory = solutionCategoryRepository.findByName("Lighting")
                    .orElseGet(() -> solutionCategoryRepository.save(new SolutionCategory(
                            null,
                            "Lighting",
                            "Professional event lighting solutions",
                            Status.APPROVED
                    )));

            SolutionCategory musicCategory = solutionCategoryRepository.findByName("Music")
                    .orElseGet(() -> solutionCategoryRepository.save(new SolutionCategory(
                            null,
                            "Music",
                            "Live bands and DJ services",
                            Status.APPROVED
                    )));


            // Proveri i dodaj EventType
            EventType partyType = eventTypeRepository.findByName("Party")
                    .orElseGet(() -> eventTypeRepository.save(new EventType(null, "Party", "Social gathering", true, new ArrayList<>())));
            EventType theatreType = eventTypeRepository.findByName("Theatre")
                    .orElseGet(() -> eventTypeRepository.save(new EventType(null, "Theatre", "Performing arts", true, new ArrayList<>())));

            Admin admin = (Admin) userRepository.findByEmail("admin@gmail.com").orElseGet(() -> {
                Admin newAdmin = new Admin();
                newAdmin.setEmail("admin@gmail.com");
                newAdmin.setFirstName("Admin");
                newAdmin.setLastName("Admin");
                newAdmin.setPassword("admin");
                newAdmin.setCity("Novi Sad");
                newAdmin.setVerified(true);
                newAdmin.setActive(true);
                newAdmin.setActivationToken("7e2b4df2-9fe6-44df-9d93-3d20d5e246fc");
                return userRepository.save(newAdmin);
            });

            EventOrganizer organizer = (EventOrganizer) userRepository.findByEmail("anajovanovic@example.com")
                    .orElseGet(() -> {
                        EventOrganizer newOrganizer = new EventOrganizer();
                        newOrganizer.setEmail("milicabosancic03@gmail.com");
                        newOrganizer.setPassword("ana123");
                        newOrganizer.setName("Ana");
                        newOrganizer.setSurname("Jovanovic");
                        newOrganizer.setProfilePhoto("assets/images/profile1.png");
                        newOrganizer.setCity("Novi Sad");
                        newOrganizer.setVerified(true);
                        newOrganizer.setActive(true);
                        newOrganizer.setCity("Belgrade");
                        newOrganizer.setSuspended(false);
                        newOrganizer.setActivationToken("7e2b4df2-9fe6-44df-9d93-3d50d5e246fe");
                        return userRepository.save(newOrganizer);
                    });

            EventOrganizer organizer2 = (EventOrganizer) userRepository.findByEmail("milosnikolic@example.com")
                    .orElseGet(() -> {
                        EventOrganizer newOrganizer = new EventOrganizer();
                        newOrganizer.setEmail("milosnikolic@example.com");
                        newOrganizer.setPassword("milos123");
                        newOrganizer.setName("Milos");
                        newOrganizer.setSurname("Nikolic");
                        newOrganizer.setProfilePhoto("assets/images/profile2.png");
                        newOrganizer.setCity("Belgrade");
                        newOrganizer.setVerified(true);
                        newOrganizer.setActive(true);
                        newOrganizer.setCity("Novi Sad");
                        newOrganizer.setSuspended(false);
                        return userRepository.save(newOrganizer);
                    });

            EventOrganizer organizer3 = (EventOrganizer) userRepository.findByEmail("nikolinapetrovic@example.com")
                    .orElseGet(() -> {
                        EventOrganizer newOrganizer = new EventOrganizer();
                        newOrganizer.setEmail("nikolinapetrovic@example.com");
                        newOrganizer.setPassword("nikolina123");
                        newOrganizer.setName("Nikolina");
                        newOrganizer.setSurname("Petrovic");
                        newOrganizer.setProfilePhoto("assets/images/profile3.png");
                        newOrganizer.setCity("Novi Sad");
                        newOrganizer.setVerified(true);
                        newOrganizer.setActive(true);
                        newOrganizer.setSuspended(false);
                        return userRepository.save(newOrganizer);
                    });

            EventOrganizer organizer4 = (EventOrganizer) userRepository.findByEmail("draganamilivojevic@example.com")
                    .orElseGet(() -> {
                        EventOrganizer newOrganizer = new EventOrganizer();
                        newOrganizer.setEmail("draganamilivojevic@example.com");
                        newOrganizer.setPassword("dragana123");
                        newOrganizer.setName("Dragana");
                        newOrganizer.setSurname("Milivojevic");
                        newOrganizer.setProfilePhoto("assets/images/profile4.png");
                        newOrganizer.setCity("Novi Sad");
                        newOrganizer.setVerified(true);
                        newOrganizer.setActive(true);
                        newOrganizer.setSuspended(false);
                        return userRepository.save(newOrganizer);
                    });

            EventOrganizer organizer5 = (EventOrganizer) userRepository.findByEmail("nikolamatijevic@example.com")
                    .orElseGet(() -> {
                        EventOrganizer newOrganizer = new EventOrganizer();
                        newOrganizer.setEmail("nikolamatijevic@example.com");
                        newOrganizer.setPassword("nikola123");
                        newOrganizer.setName("Nikola");
                        newOrganizer.setSurname("Matijevic");
                        newOrganizer.setProfilePhoto("assets/images/profile5.png");
                        newOrganizer.setCity("Novi Sad");
                        newOrganizer.setVerified(true);
                        newOrganizer.setActive(true);
                        newOrganizer.setSuspended(false);
                        return userRepository.save(newOrganizer);
                    });


// Dodavanje događaja
            if (eventRepository.findByName("Birthday Party").isEmpty()) {
                List<SolutionCategory> categories = solutionCategoryRepository.findAllById(List.of(1));
                System.out.println("Categories for 'Birthday Party': " + categories);
                eventRepository.save(new Event(null, organizer, "Birthday Party", "Entry with present", 50, PrivacyType.OPEN, "Belgrade",
                        LocalDate.of(2025, 6, 2), LocalDate.of(2025, 6, 2), "assets/images/event1.png", partyType, categories));
            }

            if (eventRepository.findByName("Horse Riding").isEmpty()) {
                List<SolutionCategory> categories = solutionCategoryRepository.findAllById(List.of(2));
                System.out.println("Categories for 'Horse Riding': " + categories);
                eventRepository.save(new Event(null, organizer, "Horse Riding", "For horse lovers, free entry", 30, PrivacyType.OPEN, "Novi Sad",
                        LocalDate.of(2025, 7, 25), LocalDate.of(2025, 7, 25), "assets/images/event2.png", partyType, categories));
            }

            if (eventRepository.findByName("Bakery opening").isEmpty()) {
                List<SolutionCategory> categories = solutionCategoryRepository.findAllById(List.of(3, 4));
                System.out.println("Categories for 'Bakery opening': " + categories);
                eventRepository.save(new Event(null, organizer3, "Bakery opening", "Come with an empty stomach!", 30, PrivacyType.OPEN, "Novi Sad",
                        LocalDate.of(2025, 7, 25), LocalDate.of(2025, 7, 25), "assets/images/event6.png", partyType, categories));
            }

            if (eventRepository.findByName("Rooftop theatre").isEmpty()) {
                List<SolutionCategory> categories = solutionCategoryRepository.findAllById(List.of(1));
                System.out.println("Categories for 'Rooftop theatre': " + categories);
                eventRepository.save(new Event(null, organizer, "Rooftop theatre", "Free entry, bring popcorn and drinks!", 30, PrivacyType.OPEN, "Novi Sad",
                        LocalDate.of(2025, 7, 25), LocalDate.of(2025, 7, 25), "assets/images/event3.png", partyType, categories));
            }

            if (eventRepository.findByName("Graduation party").isEmpty()) {
                List<SolutionCategory> categories = solutionCategoryRepository.findAllById(List.of(4));
                System.out.println("Categories for 'Graduation party': " + categories);
                eventRepository.save(new Event(null, organizer5, "Graduation party", "All college graduates wellcome :)", 30, PrivacyType.OPEN, "Novi Sad",
                        LocalDate.of(2025, 7, 25), LocalDate.of(2025, 7, 25), "assets/images/event5.png", theatreType, categories));
            }

            if (eventRepository.findByName("EXIT Festival").isEmpty()) {
                List<SolutionCategory> categories = solutionCategoryRepository.findAllById(List.of(3));
                System.out.println("Categories for 'EXIT Festival': " + categories);
                eventRepository.save(new Event(null, organizer2, "EXIT Festival", "Together, always", 30, PrivacyType.OPEN, "Novi Sad",
                        LocalDate.of(2025, 7, 7), LocalDate.of(2025, 7, 10), "assets/images/event8.png", partyType, categories));
            }

            if (eventRepository.findByName("Proba").isEmpty()) {
                List<SolutionCategory> categories = solutionCategoryRepository.findAllById(List.of(2));
                System.out.println("Categories for 'Proba': " + categories);
                eventRepository.save(new Event(null, organizer, "Proba", "For horse lovers, free entry", 30, PrivacyType.OPEN, "Novi Sad",
                        LocalDate.of(2025, 2, 27), LocalDate.of(2025, 2, 28), "assets/images/event2.png", partyType, categories));
            }





            // Add categories to event types
            partyType.getSolutionCategories().add(cateringCategory);
            partyType.getSolutionCategories().add(musicCategory);

            theatreType.getSolutionCategories().add(lightingCategory);
            theatreType.getSolutionCategories().add(musicCategory);

            // Save the updated EventTypes (this will automatically update the join table)
            eventTypeRepository.save(partyType);
            eventTypeRepository.save(theatreType);

            // Dodavanje ServiceAndProductProvider
            ServiceAndProductProvider provider1 = (ServiceAndProductProvider) userRepository.findByEmail("provider1@example.com")
                    .orElseGet(() -> {
                        ServiceAndProductProvider newProvider = new ServiceAndProductProvider();
                        newProvider.setEmail("provider1@example.com");
                        newProvider.setPassword("securepassword");
                        newProvider.setCompanyName("Prestige Champagne");
                        newProvider.setDescription("Sparkling wine");
                        newProvider.setCity("Novi Sad");
                        newProvider.setActive(true);
                        newProvider.setVerified(true);
                        newProvider.setPhotos(List.of("assets/images/profile2.png"));
                        return userRepository.save(newProvider);
                    });

            ServiceAndProductProvider provider2 = (ServiceAndProductProvider) userRepository.findByEmail("lighting1@example.com")
                    .orElseGet(() -> {
                        ServiceAndProductProvider newProvider = new ServiceAndProductProvider();
                        newProvider.setEmail("lighting1@example.com");
                        newProvider.setPassword("securepassword");
                        newProvider.setCompanyName("Freedom Riders");
                        newProvider.setCity("Novi Sad");
                        newProvider.setDescription("Galop beyond limits");
                        newProvider.setActive(true);
                        newProvider.setVerified(true);
                        newProvider.setPhotos(List.of("assets/images/profile3.png", "lights2.png"));
                        return userRepository.save(newProvider);
                    });

            ServiceAndProductProvider provider3 = (ServiceAndProductProvider) userRepository.findByEmail("music@example.com")
                    .orElseGet(() -> {
                        ServiceAndProductProvider newProvider = new ServiceAndProductProvider();
                        newProvider.setEmail("milicabosancic1612@gmail.com");
                        newProvider.setPassword("securepassword");
                        newProvider.setCompanyName("VibeX");
                        newProvider.setCity("Novi Sad");
                        newProvider.setDescription("DJ and live band services for weddings and parties.");
                        newProvider.setPhotos(List.of("dj1.png", "band1.png"));
                        newProvider.setActive(true);
                        newProvider.setVerified(true);
                        return userRepository.save(newProvider);
                    });

            ServiceAndProductProvider provider4 = (ServiceAndProductProvider) userRepository.findByEmail("food@example.com")
                    .orElseGet(() -> {
                        ServiceAndProductProvider newProvider = new ServiceAndProductProvider();
                        newProvider.setEmail("food@example.com");
                        newProvider.setPassword("securepassword");
                        newProvider.setCompanyName("Majestic bites");
                        newProvider.setCity("Novi Sad");
                        newProvider.setDescription("Taste the Majesty, savor the moment");
                        newProvider.setPhotos(List.of("dj1.png", "band1.png"));
                        newProvider.setActive(true);
                        newProvider.setVerified(true);
                        return userRepository.save(newProvider);
                    });


            // Dodavanje Solution (proizvodi/usluge)

            Product champagne = (Product) solutionRepository.findByName("Champagne").orElseGet(() -> {
                Product balloonDecoration = new Product();
                balloonDecoration.setName("Champagne");
                balloonDecoration.setDescription("Sparkling wine for every occasion.");
                balloonDecoration.setLocation("Belgrade");
                balloonDecoration.setPrice(22000.0);
                balloonDecoration.setDiscount(10.0);
                balloonDecoration.setImageUrl("assets/images/service1.png");
                balloonDecoration.setAvailable(true);
                balloonDecoration.setVisible(true);
                balloonDecoration.setDeleted(false);
                balloonDecoration.setStatus(Status.APPROVED);
                balloonDecoration.setCategory(decorationCategory);
                balloonDecoration.setProvider(provider1);

                return solutionRepository.save(balloonDecoration);
             });

            Product rooftop = (Product) solutionRepository.findByName("Rooftop theatre equipment").orElseGet(() -> {
                Product gourmetCatering = new Product();
                gourmetCatering.setName("Rooftop theatre equipment");
                gourmetCatering.setDescription("For the best movie night");
                gourmetCatering.setLocation("Novi Sad");
                gourmetCatering.setPrice(15000.0);
                gourmetCatering.setDiscount(15.0);
                gourmetCatering.setImageUrl("assets/images/service3.png");
                gourmetCatering.setAvailable(true);
                gourmetCatering.setVisible(true);
                gourmetCatering.setDeleted(false);
                gourmetCatering.setStatus(Status.APPROVED);
                gourmetCatering.setCategory(cateringCategory);
                gourmetCatering.setProvider(provider3);

                return solutionRepository.save(gourmetCatering);
            });

            Product catering = (Product) solutionRepository.findByName("Catering service").orElseGet(() -> {

                Product gourmetCatering = new Product();
                gourmetCatering.setName("Catering service");
                gourmetCatering.setDescription("The best food service in your town!");
                gourmetCatering.setLocation("Novi Sad");
                gourmetCatering.setPrice(15000.0);
                gourmetCatering.setDiscount(15.0);
                gourmetCatering.setImageUrl("assets/images/service4.png");
                gourmetCatering.setAvailable(true);
                gourmetCatering.setVisible(true);
                gourmetCatering.setDeleted(false);
                gourmetCatering.setStatus(Status.APPROVED);
                gourmetCatering.setCategory(cateringCategory);
                gourmetCatering.setProvider(provider4);

                return solutionRepository.save(gourmetCatering);
            });

            Product photobooth = (Product) solutionRepository.findByName("Photobooth rentals").orElseGet(() -> {
                Product ledLighting = new Product();
                ledLighting.setName("Photobooth rentals");
                ledLighting.setDescription("Interactive photo booths with custom backdrops and props.");
                ledLighting.setLocation("Belgrade");
                ledLighting.setPrice(20000.0);
                ledLighting.setDiscount(5.0);
                ledLighting.setImageUrl("assets/images/service8.png");
                ledLighting.setAvailable(true);
                ledLighting.setVisible(true);
                ledLighting.setDeleted(false);
                ledLighting.setStatus(Status.APPROVED);
                ledLighting.setCategory(lightingCategory);
                ledLighting.setProvider(provider3);
                return solutionRepository.save(ledLighting);
            });

            Service singer = (Service) solutionRepository.findByName("Singer").orElseGet(() -> {

                Service weddingDJ = new Service();
                weddingDJ.setName("Singer");
                weddingDJ.setDescription("Music lover");
                weddingDJ.setLocation("Novi Sad");
                weddingDJ.setPrice(60000.0);
                weddingDJ.setDiscount(10.0);
                weddingDJ.setImageUrl("assets/images/service6.png");
                weddingDJ.setAvailable(true);
                weddingDJ.setVisible(true);
                weddingDJ.setDeleted(false);
                weddingDJ.setDuration(Duration.ofHours(2));
                weddingDJ.setCancellationPeriod(Duration.ofHours(1));
                weddingDJ.setReservationPeriod(Duration.ofHours(4));
                weddingDJ.setReservationType(ReservationType.MANUAL);
                weddingDJ.setStatus(Status.APPROVED);
                weddingDJ.setCategory(musicCategory);
                weddingDJ.setProvider(provider3);
                return solutionRepository.save(weddingDJ);
            });

            Service band = (Service) solutionRepository.findByName("Band").orElseGet(() -> {
                Service weddingDJ = new Service();
                weddingDJ.setName("Band");
                weddingDJ.setDescription("High-energy band playing modern favourites.");
                weddingDJ.setLocation("Novi Sad");
                weddingDJ.setPrice(80000.0);
                weddingDJ.setDiscount(10.0);
                weddingDJ.setImageUrl("assets/images/service5.png");
                weddingDJ.setAvailable(true);
                weddingDJ.setVisible(true);
                weddingDJ.setDeleted(false);
                weddingDJ.setCancellationPeriod(Duration.ofHours(1));
                weddingDJ.setReservationPeriod(Duration.ofHours(4));
                weddingDJ.setMaxDuration(Duration.ofHours(3));
                weddingDJ.setMinDuration(Duration.ofHours(1));
                weddingDJ.setReservationType(ReservationType.MANUAL);
                weddingDJ.setStatus(Status.APPROVED);
                weddingDJ.setCategory(musicCategory);
                weddingDJ.setProvider(provider3);
                return solutionRepository.save(weddingDJ);
            });

            Service lessons = (Service) solutionRepository.findByName("Horse riding lessons").orElseGet(() -> {

                Service weddingDJ = new Service();
                weddingDJ.setName("Horse riding lessons");
                weddingDJ.setDescription("For horse lovers");
                weddingDJ.setLocation("Novi Sad");
                weddingDJ.setPrice(6000.0);
                weddingDJ.setDiscount(10.0);
                weddingDJ.setImageUrl("assets/images/service2.png");
                weddingDJ.setAvailable(true);
                weddingDJ.setVisible(true);
                weddingDJ.setDeleted(false);
                weddingDJ.setDuration(Duration.ofHours(2));
                weddingDJ.setCancellationPeriod(Duration.ofHours(1));
                weddingDJ.setReservationPeriod(Duration.ofHours(4));
                weddingDJ.setReservationType(ReservationType.MANUAL);
                weddingDJ.setStatus(Status.APPROVED);
                weddingDJ.setCategory(musicCategory);
                weddingDJ.setProvider(provider2);
                return solutionRepository.save(weddingDJ);
            });

            Service team = (Service) solutionRepository.findByName("Marketing team").orElseGet(() -> {

                Service weddingDJ = new Service();
                weddingDJ.setName("Marketing team");
                weddingDJ.setDescription("Burn your social medias");
                weddingDJ.setLocation("Novi Sad");
                weddingDJ.setPrice(30000.0);
                weddingDJ.setDiscount(10.0);
                weddingDJ.setImageUrl("assets/images/service7.png");
                weddingDJ.setAvailable(true);
                weddingDJ.setVisible(true);
                weddingDJ.setDeleted(false);
                weddingDJ.setDuration(Duration.ofHours(2));
                weddingDJ.setCancellationPeriod(Duration.ofHours(1));
                weddingDJ.setReservationPeriod(Duration.ofHours(4));
                weddingDJ.setReservationType(ReservationType.MANUAL);
                weddingDJ.setStatus(Status.APPROVED);
                weddingDJ.setCategory(musicCategory);
                weddingDJ.setProvider(provider3);
                return solutionRepository.save(weddingDJ);
            });

            // Dodavanje komentara sa statusom PENDING
            Comment comment1 = new Comment();
            comment1.setContent("Great event, looking forward to it!");
            comment1.setRating(5);
            comment1.setDate(LocalDate.now());
            comment1.setStatus(Status.PENDING);
            comment1.setProduct(champagne);
            comment1.setCommenter(organizer); // Koristimo organizatora kao komentatora
            commentRepository.save(comment1);

            Comment comment2 = new Comment();
            comment2.setContent("Excited about this event!");
            comment2.setRating(4);
            comment2.setDate(LocalDate.now());
            comment2.setStatus(Status.PENDING);
            comment2.setProduct(photobooth);
            comment2.setCommenter(organizer); // Koristimo organizatora kao komentatora
            commentRepository.save(comment2);

            System.out.println("✅ Podaci uspešno dodati u bazu.");

        };
    }
}
