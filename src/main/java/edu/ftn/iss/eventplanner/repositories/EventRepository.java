package edu.ftn.iss.eventplanner.repositories;

import edu.ftn.iss.eventplanner.entities.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    // Pronaći prva 5 događaja po lokaciji
    List<Event> findFirst5ByLocationOrderByStartDateDesc(String location);

    // Pronaći događaj po imenu
    Optional<Event> findByName(String name);

    // Pronaći događaje po lokaciji
    List<Event> findByLocation(String location);

    @Query("SELECT DISTINCT e.location FROM Event e")
    List<String> findAllLocations();

    @Query("SELECT DISTINCT e.eventType.name FROM Event e")
    List<String> findAllCategories();

    @Query("SELECT e FROM Event e WHERE " +
            "(:category IS NULL OR e.eventType.name = :category) AND " +
            "(:minDate IS NULL OR e.startDate >= :minDate) AND " +
            "(:maxDate IS NULL OR e.endDate <= :maxDate) AND " +
            "(:location IS NULL OR e.location = :location)")
    Page<Event> findFilteredEvents(@Param("category") String category,
                                   @Param("minDate") LocalDate minDate,
                                   @Param("maxDate") LocalDate maxDate,
                                   @Param("location") String location,
                                   Pageable pageable);
}
