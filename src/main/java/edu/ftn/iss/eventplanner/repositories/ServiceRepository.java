package edu.ftn.iss.eventplanner.repositories;

import edu.ftn.iss.eventplanner.entities.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Optional;

public interface ServiceRepository extends JpaRepository<Service, Integer> { // TODO: See if logically deleted services can be left out

    Optional<Service> findByIdAndDeletedIsFalse(int id);

    Collection<Service> getAllByDeletedIsFalse();
    Collection<Service> getByProvider_IdAndDeletedIsFalse(int providerId);

    Collection<Service> getByCategory_IdAndDeletedIsFalse(int categoryId);
    Collection<Service> getByCategory_NameAndDeletedIsFalse(String name);

    Collection<Service> getByVisibleIsTrueAndDeletedIsFalse();

    @Modifying
    @Query("UPDATE Service s SET s.isDeleted = true WHERE s.id = :id")
    void softDeleteById(@Param("id") int id);
}
