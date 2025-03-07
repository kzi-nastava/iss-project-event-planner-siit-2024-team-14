package edu.ftn.iss.eventplanner.repositories;

import edu.ftn.iss.eventplanner.entities.Solution;
import jakarta.annotation.Nullable;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Objects;


public interface SolutionSearchRepositoryMixin<T extends Solution> extends JpaSpecificationExecutor<T> {

    default List<T> search(String query) {
        return search(query, null);
    }

    default List<T> search(String query, @Nullable Specification<T> spec) {
        return search(query, spec, Pageable.unpaged()).toList();
    }


    default Page<T> search(String query, @Nullable Specification<T> spec, Pageable pageable) {
        return findAll(
                Specification
                        .where(spec)
                        .and(query == null ? null : orderByBestNameMatchSpecification(query)),
                pageable
        );
    }


    default Specification<T> orderByBestNameMatchSpecification(String query) {
        String lowerQuery = Objects.requireNonNull(query).toLowerCase();

        return (root, cq, cb) -> {
            Expression<String> lowerName = cb.lower(root.get("name"));

            Expression<Object> caseExpr = cb.selectCase()
                    // full name match
                    .when(cb.equal(lowerName, lowerQuery), 3)
                    // starts with
                    .when(cb.like(lowerName, lowerQuery + "%"), 2)
                    // contains
                    .when(cb.like(lowerName, "%" + lowerQuery + "%"), 1)
                    // no match
                    .otherwise(0);

            assert cq != null;
            cq.orderBy(cb.desc(caseExpr));
            return cb.conjunction();
        };
    }

}
