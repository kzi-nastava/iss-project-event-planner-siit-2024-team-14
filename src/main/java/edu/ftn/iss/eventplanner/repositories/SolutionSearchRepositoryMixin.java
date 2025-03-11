package edu.ftn.iss.eventplanner.repositories;

import edu.ftn.iss.eventplanner.entities.Solution;
import jakarta.annotation.Nullable;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;


public interface SolutionSearchRepositoryMixin<T extends Solution> extends JpaSpecificationExecutor<T> {

    default List<T> search(String query) {
        return search(query, null);
    }

    default List<T> search(String query, @Nullable Specification<T> spec) {
        return search(query, spec, Pageable.unpaged()).toList();
    }


    default Page<T> search(String query, @Nullable Specification<T> spec, Pageable pageable) {
        return findAll(
                Specification.allOf(spec, orderByBestNameMatchSpecification(query)),
                pageable
        );
    }

    @Nullable
    private static <T extends Solution> Specification<T> orderByBestNameMatchSpecification(@Nullable String query) {
        if (query == null) return null;
        String lowerQuery = query.toLowerCase();

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
