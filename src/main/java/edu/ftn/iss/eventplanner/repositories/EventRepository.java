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

    // Filtriranje po eventType i opsegu datuma
    @Query("SELECT e FROM Event e WHERE e.eventType = :eventType AND e.startDate BETWEEN :startDate AND :endDate")
    Page<Event> findByEventTypeAndDateRange(@Param("eventType") String eventType, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, Pageable pageable);

    // Filtriranje po eventType i datumu početka
    @Query("SELECT e FROM Event e WHERE e.eventType = :eventType AND e.startDate >= :startDate")
    Page<Event> findByEventTypeAndStartDateGreaterThanEqual(@Param("eventType") String eventType, @Param("startDate") LocalDate startDate, Pageable pageable);

    // Filtriranje po eventType i datumu završetka
    @Query("SELECT e FROM Event e WHERE e.eventType = :eventType AND e.endDate <= :endDate")
    Page<Event> findByEventTypeAndEndDateLessThanEqual(@Param("eventType") String eventType, @Param("endDate") LocalDate endDate, Pageable pageable);

    // Filtriranje po eventType
    Page<Event> findByEventType(String eventType, Pageable pageable);

    // Filtriranje po opsegu datuma
    @Query("SELECT e FROM Event e WHERE e.startDate BETWEEN :startDate AND :endDate")
    Page<Event> findByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, Pageable pageable);

    // Filtriranje po datumu početka
    @Query("SELECT e FROM Event e WHERE e.startDate >= :startDate")
    Page<Event> findByStartDateGreaterThanEqual(@Param("startDate") LocalDate startDate, Pageable pageable);

    // Filtriranje po datumu završetka
    @Query("SELECT e FROM Event e WHERE e.endDate <= :endDate")
    Page<Event> findByEndDateLessThanEqual(@Param("endDate") LocalDate endDate, Pageable pageable);
}
