package edu.ftn.iss.eventplanner.repositories;

import edu.ftn.iss.eventplanner.entities.SolutionBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SolutionBookingRepository extends JpaRepository<SolutionBooking, Integer> {

    @Query("SELECT sb.solution.id FROM SolutionBooking sb WHERE " +
            "(:startDate IS NOT NULL AND :endDate IS NOT NULL AND (" +
            "(:startDate BETWEEN sb.bookingDate AND FUNCTION('ADDTIME', sb.startTime, sb.duration)) OR " +
            "(:endDate BETWEEN sb.bookingDate AND FUNCTION('ADDTIME', sb.startTime, sb.duration)) OR " +
            "(sb.bookingDate BETWEEN :startDate AND :endDate)))")
    List<Long> findBookedSolutionIds(@Param("startDate") LocalDate startDate,
                                     @Param("endDate") LocalDate endDate);


}

