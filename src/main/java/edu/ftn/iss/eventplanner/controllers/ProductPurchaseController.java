package edu.ftn.iss.eventplanner.controllers;

import edu.ftn.iss.eventplanner.dtos.GetProductDTO;
import edu.ftn.iss.eventplanner.dtos.ProductPurchaseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = {"/api/events/{eventId:\\d+}"}, produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductPurchaseController {

    // GET */api/events/1/products?status=purchased
    @GetMapping("/products")
    public List<GetProductDTO> getAvailableXorPurchasedProductsForEvent(
            @PathVariable int eventId,
            @RequestParam String status // purchased | available
    ) {
        return List.of();
    }


    // GET */api/events/1/purchases
    @GetMapping("/purchases")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductPurchaseDTO> getPurchasesForEvent(
            @PathVariable int eventId
    ) {
        return List.of();
    }

    // GET */api/events/1/purchases/1
    @GetMapping("/purchases/{productId:\\d+}")
    @ResponseStatus(HttpStatus.OK)
    public ProductPurchaseDTO getPurchaseForEvent(
            @PathVariable int eventId,
            @PathVariable int productId
    ) {
        return null;
    }


    // POST organizer[Organizes the event 1]@*/api/events/1/purchases
    @PostMapping("/purchases")
    public ResponseEntity<ProductPurchaseDTO> purchaseProductForEvent(
            @PathVariable int eventId
    ) {
        return null;
    }

    // DELETE organizer[Organizes the event 1]@*/api/events/1/purchases
    @DeleteMapping("/purchases/{productId:\\d+}")
    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    public void cancelProductPurchaseForEvent(
            @PathVariable int eventId,
            @PathVariable int productId
    ) {}

}
