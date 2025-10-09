package edu.ftn.iss.eventplanner.repositories;

import edu.ftn.iss.eventplanner.entities.BookingService;
import edu.ftn.iss.eventplanner.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.sql.Time;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingServiceRepository extends JpaRepository<BookingService, Integer> {


    @Query("SELECT b FROM BookingService b WHERE b.service.id = :serviceId AND b.bookingDate = :date " +
            "AND b.confirmed = edu.ftn.iss.eventplanner.enums.Status.APPROVED")
    List<BookingService> findConfirmedBookings(@Param("serviceId") Integer serviceId, @Param("date") LocalDate date);

    @Query("SELECT b FROM BookingService b WHERE b.bookingDate = CURRENT_DATE " +
            "AND b.startTime = ?1 AND b.confirmed = edu.ftn.iss.eventplanner.enums.Status.APPROVED")
    List<BookingService> findBookingsStartingAt(Time oneHourLater);

    List<BookingService> findByConfirmed(Status confirmed);

}
