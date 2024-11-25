package edu.ftn.iss.eventplanner.controllers;

import edu.ftn.iss.eventplanner.dtos.ServiceProductDTO;
import edu.ftn.iss.eventplanner.model.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductServiceController {

    @GetMapping("/top/{city}")
    public ResponseEntity<List<ServiceProductDTO>> getTopProducts(@PathVariable String city) {
        if (city == null || city.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        // Simulacija dobavljanja proizvoda/usluga iz baze
        List<Product> productsFromDatabase = getMockProducts(city);
        List<ServiceProductDTO> topProducts = productsFromDatabase.stream()
                .map(product -> new ServiceProductDTO(
                        product.getName(),
                        product.getDescription(),
                        product.getPrice(),
                        product.getImageUrl()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(topProducts);
    }

    private List<Product> getMockProducts(String city) {
        // Simulirani podaci
        return List.of(
                        new Product(1L, "Service 1", "Description 1", 100.0, 10.0, "image1.jpg", true, null, null),
                        new Product(2L, "Service 2", "Description 2", 200.0, 15.0, "image2.jpg", true, null, null),
                        new Product(3L, "Service 3", "Description 3", 300.0, 20.0, "image3.jpg", true, null, null),
                        new Product(4L, "Service 4", "Description 4", 400.0, 25.0, "image4.jpg", true, null, null),
                        new Product(5L, "Service 5", "Description 5", 500.0, 30.0, "image5.jpg", true, null, null)
                ).stream()
                .filter(product -> product.isAvailable()) // Dodajemo dostupnost
                .collect(Collectors.toList());
    }
}

