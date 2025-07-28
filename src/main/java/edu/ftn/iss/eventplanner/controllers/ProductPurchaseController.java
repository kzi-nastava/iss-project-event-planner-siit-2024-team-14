package edu.ftn.iss.eventplanner.controllers;

import edu.ftn.iss.eventplanner.dtos.GetProductDTO;
import edu.ftn.iss.eventplanner.dtos.ProductPurchaseDTO;
import edu.ftn.iss.eventplanner.entities.Product;
import edu.ftn.iss.eventplanner.entities.PurchaseProduct;
import edu.ftn.iss.eventplanner.exceptions.BadRequestException;
import edu.ftn.iss.eventplanner.mappers.ProductDTOMapper;
import edu.ftn.iss.eventplanner.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = {"/api/events/{eventId:\\d+}"}, produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductPurchaseController {

    private final ProductService productService;
    private final ProductDTOMapper modelMapper;


    @Autowired
    public ProductPurchaseController(ProductService productService, ProductDTOMapper modelMapper) {
        this.productService = productService;
        this.modelMapper = modelMapper;
    }



    // GET */api/events/1/products?status=purchased
    @GetMapping("/products")
    public List<GetProductDTO> getAvailableXorPurchasedProductsForEvent(
            @PathVariable int eventId,
            @RequestParam(required = false, defaultValue = "available") String status // purchased | available
    ) {
        List<Product> products = switch (status.toLowerCase()) {
            case "purchased" -> productService.getPurchasedProductsForEvent(eventId);
            case "available" -> productService.getAvailableProductsForEvent(eventId);
            default ->
                    throw new BadRequestException(String.format("Status must be either 'purchased' or 'available', not '%s'.", status.toLowerCase()));
        };

        return products.stream()
                .map(modelMapper::toProductDTO)
                .toList();
    }


    // GET */api/events/1/purchases
    @GetMapping("/purchases")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductPurchaseDTO> getPurchasesForEvent(
            @PathVariable int eventId
    ) {
        return productService.getPurchasesForEvent(eventId).stream()
                .map(modelMapper::toProductPurchaseDTO)
                .toList();
    }


    // GET */api/events/1/purchases/1
    @GetMapping("/purchases/{productId:\\d+}")
    @ResponseStatus(HttpStatus.OK)
    public ProductPurchaseDTO getPurchaseForEvent(
            @PathVariable int eventId,
            @PathVariable int productId
    ) {
        return modelMapper.toProductPurchaseDTO(
                productService.getProductPurchase(eventId, productId)
        );
    }


    // POST organizer[Organizes the event 1]@*/api/events/1/purchases
    @PostMapping("/purchases")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<ProductPurchaseDTO> purchaseProductForEvent(
            @PathVariable int eventId,
            @RequestBody int productId,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        PurchaseProduct purchase = productService.purchaseProduct(eventId, productId);

        URI location = uriComponentsBuilder
                .path("/api/events/{eventId}/purchases/{productId}")
                .buildAndExpand(eventId, productId)
                .toUri();

        return ResponseEntity
                .created(location)
                .body(modelMapper.toProductPurchaseDTO(purchase));
    }

    // DELETE organizer[Organizes the event 1]@*/api/events/1/purchases/1
    @DeleteMapping("/purchases/{productId:\\d+}")
    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    public void cancelProductPurchaseForEvent(
            @PathVariable int eventId,
            @PathVariable int productId
    ) {}

}
