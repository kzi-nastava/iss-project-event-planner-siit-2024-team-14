package edu.ftn.iss.eventplanner.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping(path = {"/api", "/"})
public class FavouriteOfferingsController {

    // TODO: Come up with a more appropriate path. (maybe format like `api/[profile | user]/favourites/[services | products | events]`)
    @GetMapping(path = {"/offerings/favourites"}, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Collection<Object>> getMyFavouriteOfferings() {
        return ResponseEntity.ok(List.of());
    }

    @PostMapping(path = {"/services/{serviceId}/mark-as-favourite"}, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Object> addFavouriteService(
            @PathVariable(name = "serviceId") Long serviceId
    ) {
        return ResponseEntity.accepted().body(serviceId);
    }

    @PostMapping(path = {"/products/{productId}/mark-as-favourite"}, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Object> addFavouriteProduct(
            @PathVariable(name = "productId") Long productId
    ) {
        return ResponseEntity.accepted().body(productId);
    }
}
