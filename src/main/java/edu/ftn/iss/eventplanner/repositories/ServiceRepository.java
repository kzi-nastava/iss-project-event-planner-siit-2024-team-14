package edu.ftn.iss.eventplanner.repositories;

import edu.ftn.iss.eventplanner.entities.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ServiceRepository extends JpaRepository<Service, Integer> {

    List<Service> findByProvider_Id(int providerId);
    Page<Service> findByProvider_Id(int providerId, Pageable pageable);

    List<Service> findByCategory_Id(int categoryId);
    Page<Service> findByCategory_Id(int categoryId, Pageable pageable);
}
