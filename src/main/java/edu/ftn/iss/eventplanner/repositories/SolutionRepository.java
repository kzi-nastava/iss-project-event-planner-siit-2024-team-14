package edu.ftn.iss.eventplanner.repositories;
import edu.ftn.iss.eventplanner.entities.Solution;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SolutionRepository extends JpaRepository<Solution, Integer> {

    List<Solution> findFirst5ByLocation(String location);

    List<Solution> findFirst5ByLocationAndDeletedFalseAndAvailableTrueAndVisibleTrue(String city);

    Optional<Solution> findByName(String name);

    List<Solution> findAllByDeletedFalseAndAvailableTrueAndVisibleTrue();

    List<Solution> findByLocation(String location);

    @Query("SELECT DISTINCT s.location FROM Solution s WHERE s.isDeleted = false AND s.isVisible = true")
    List<String> findAllLocations();

    @Query("SELECT DISTINCT s.category.name FROM Solution s WHERE s.isDeleted = false AND s.isVisible = true")
    List<String> findAllCategories();

    @Query("SELECT s FROM Solution s WHERE " +
            "(:category IS NULL OR s.category.name = :category) AND " +
            "(:type IS NULL OR TYPE(s) = :type) AND " +
            "(:minPrice IS NULL OR s.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR s.price <= :maxPrice) AND " +
            "(:location IS NULL OR s.location = :location) AND " +
            "(:bookedSolutionIds IS NULL OR s.id NOT IN :bookedSolutionIds)")
    Page<Solution> findAvailableSolutions(@Param("category") String category,
                                          @Param("type") Class<?> type,
                                          @Param("minPrice") Double minPrice,
                                          @Param("maxPrice") Double maxPrice,
                                          @Param("location") String location,
                                          @Param("bookedSolutionIds") List<Long> bookedSolutionIds,
                                          Pageable pageable);

}
