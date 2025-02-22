package edu.ftn.iss.eventplanner.repositories;

import edu.ftn.iss.eventplanner.entities.BookingService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingServiceRepository extends JpaRepository<BookingService, Integer> {
    @Query("SELECT b FROM BookingService b WHERE b.service.id = :serviceId AND b.bookingDate = :date AND b.isConfirmed = true")
    List<BookingService> findConfirmedBookings(@Param("serviceId") Integer serviceId, @Param("date") LocalDate date);
}
