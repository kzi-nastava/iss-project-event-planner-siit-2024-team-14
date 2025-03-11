package edu.ftn.iss.eventplanner.entities;

import jakarta.annotation.Nullable;
import lombok.*;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class SolutionFilterParams implements java.util.function.Predicate<Solution> {

    @Nullable
    private Double price;

    @Builder.Default
    private double
            minPrice = Double.MIN_VALUE,
            maxPrice = Double.MAX_VALUE;

    @Nullable
    private Boolean available;
    @Nullable
    private Set<Integer> wantedCategories, wantedEventTypes;

    @Nullable
    private Integer providerId;



    @Override
    @SuppressWarnings("RedundantIfStatement")
    public boolean test(Solution solution) {
        if (solution == null)
            return false;

        if (providerId != null && (solution.getProvider() == null || !providerId.equals(solution.getProvider().getId())))
            return false;

        if (price != null && price != solution.getPrice())
            return false;
        else if (minPrice >= solution.getPrice() || solution.getPrice() >= maxPrice)
            return false;

        if (available != null && !available.equals(solution.isAvailable()))
            return false;

        if (wantedCategories != null && (solution.getCategory() == null || !wantedCategories.contains(solution.getCategory().getId())))
            return false;

        if (wantedEventTypes != null && Collections.disjoint(wantedEventTypes, solution.getApplicableEventTypes() == null ? Collections.emptySet() : solution.getApplicableEventTypes()))
            return false;

        return true;
    }



    public <T extends Solution> Specification<T> toSpecification() {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (providerId != null) {
                predicates.add(cb.equal(root.get("provider").get("id"), providerId));
            }

            if (price != null) {
                predicates.add(cb.equal(root.get("price"), price));
            } else {
                predicates.add(cb.between(root.get("price"), minPrice, maxPrice));
            }

            if (available != null) {
                predicates.add(cb.equal(root.get("isAvailable"), available));
            }

            if (wantedCategories != null) {
                predicates.add(root.get("category").get("id").in(wantedCategories));
            }

            if (wantedEventTypes != null) {
                predicates.add(root.join("applicableEventTypes").get("id").in(wantedEventTypes));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
