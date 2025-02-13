package edu.ftn.iss.eventplanner.controllers;

import edu.ftn.iss.eventplanner.dtos.PriceDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping(path = {"/api/providers/{providerId}/prices"})
public class PriceListController { // not sure how the service this will interact with will look

    protected static final String
            SERVICE_URL_FORMAT = "api/services/%d",
            PRODUCT_URL_FORMAT = "api/products/%d";

    @GetMapping
    ResponseEntity<Collection<PriceDTO>> getProviderPriceList(
            @PathVariable(name = "providerId") Long providerId,
            @RequestParam(name = "exclude-products", required = false) String excludeProducts,
            @RequestParam(name = "exclude-services", required = false) String excludeServices,
            @RequestParam(name = "name", required = false) String name
    ) {
        if (excludeProducts != null && excludeServices != null) {
            // throw new "Can't exclude both services and products!";
            return ResponseEntity.badRequest().build();
        }

        PriceDTO dummy = new PriceDTO();
        dummy.setOffering(SERVICE_URL_FORMAT.formatted(1));
        dummy.setOfferingName(name);
        dummy.setPrice(101);
        dummy.setDiscount(0.2);

        return ResponseEntity.ok(List.of(dummy));
    }

}

