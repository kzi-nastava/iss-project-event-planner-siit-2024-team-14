package edu.ftn.iss.eventplanner.dtos;

import edu.ftn.iss.eventplanner.entities.SolutionFilterParams;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Objects;
import java.util.Set;

@Data
public class SolutionFilterParamsDto {
    Integer provider;
    Double price, minPrice, maxPrice;
    Integer[] category, eventType;
    String status;


    @NotNull
    public SolutionFilterParams toFilterParams() {
        var builder = SolutionFilterParams.builder()
                .providerId(provider)
                .price(price);

        if (!Objects.isNull(minPrice))
            builder.minPrice(minPrice);

        if (!Objects.isNull(maxPrice))
            builder.maxPrice(maxPrice);

        if (!Objects.isNull(category))
            builder.wantedCategories(Set.of(category));

        if (!Objects.isNull(eventType))
            builder.wantedEventTypes(Set.of(eventType));

        if (!Objects.isNull(status))
            switch (status) {
                case "available": {
                    builder.available(true);
                    break;
                }
                case "unavailable": {
                    builder.available(false);
                    break;
                }
            }

        return builder.build();
    }


}
