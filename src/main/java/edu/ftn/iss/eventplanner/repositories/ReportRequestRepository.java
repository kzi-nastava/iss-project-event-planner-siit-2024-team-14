package edu.ftn.iss.eventplanner.repositories;

import edu.ftn.iss.eventplanner.entities.ReportRequest;
import edu.ftn.iss.eventplanner.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRequestRepository extends JpaRepository<ReportRequest, Integer> {

    List<ReportRequest> findByStatus(Status status);

}
