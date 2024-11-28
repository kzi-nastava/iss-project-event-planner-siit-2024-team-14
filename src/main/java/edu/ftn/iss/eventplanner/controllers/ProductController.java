package edu.ftn.iss.eventplanner.controllers;

import edu.ftn.iss.eventplanner.dtos.CreateProductDTO;
import edu.ftn.iss.eventplanner.dtos.CreatedProductDTO;
import edu.ftn.iss.eventplanner.dtos.GetProductDTO;
import edu.ftn.iss.eventplanner.dtos.UpdateProductDTO;
import edu.ftn.iss.eventplanner.dtos.UpdatedProductDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    Collection<GetProductDTO> products = new ArrayList<>();

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<GetProductDTO>> getProducts() {

        GetProductDTO product1 = new GetProductDTO();
        product1.setName("Balloons");
        product1.setDescription("Best for Birthday Parties!");
        product1.setPrice(100);
        product1.setDiscount(20);
        product1.setImageUrl("photo");

        GetProductDTO product2 = new GetProductDTO();
        product2.setName("Cake");
        product2.setDescription("Best for Birthday Parties!");
        product2.setPrice(100);
        product2.setDiscount(20);
        product2.setImageUrl("photo");

        products.add(product1);
        products.add(product2);

        return new ResponseEntity<Collection<GetProductDTO>>(products, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetProductDTO> getProduct(@PathVariable("id") Long id) {
        GetProductDTO product = new GetProductDTO();

        if (product == null) {
            return new ResponseEntity<GetProductDTO>(HttpStatus.NOT_FOUND);
        }

        product.setName("Balloons");
        product.setDescription("Best for Birthday Parties!");
        product.setPrice(100);
        product.setDiscount(20);
        product.setImageUrl("photo");

        return new ResponseEntity<GetProductDTO>(product, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UpdatedProductDTO> updateProduct(@PathVariable Long id, @RequestBody UpdateProductDTO product) throws Exception{
        UpdatedProductDTO updatedProduct = new UpdatedProductDTO();

        updatedProduct.setId(id);
        product.setName(product.getName());
        product.setDescription("Best for Birthday Parties!");
        product.setPrice(100);
        product.setDiscount(20);
        product.setImageUrl("photo");

        return new ResponseEntity<UpdatedProductDTO>(updatedProduct, HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreatedProductDTO> createProduct(@RequestBody CreateProductDTO product) throws Exception{
        Collection<CreatedProductDTO> products = new ArrayList<>();

        CreatedProductDTO savedProduct = new CreatedProductDTO();

        savedProduct.setId(1L);
        savedProduct.setName(product.getName());
        savedProduct.setDescription(product.getDescription());
        savedProduct.setPrice(product.getPrice());
        savedProduct.setDiscount(product.getDiscount());
        savedProduct.setImageUrl(product.getImageUrl());
        savedProduct.setCategoryId(product.getCategoryId());
        savedProduct.setEventTypes(product.getEventTypes());
        savedProduct.setAvailable(product.isAvailable());
        savedProduct.setVisible(product.isVisible());
        products.add(savedProduct);
        return new ResponseEntity<CreatedProductDTO>(savedProduct, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") Long id) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/search")
    public ResponseEntity<List<GetProductDTO>> searchProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String eventType,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Boolean available,
            @RequestParam(required = false) Boolean visible,
            @RequestParam(required = false) String description,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        List<GetProductDTO> filteredProducts = filterProducts(name, category, eventType, minPrice, maxPrice, available, visible, description);

        List<GetProductDTO> paginatedProducts = paginateProducts(filteredProducts, page, size);

        return ResponseEntity.ok(paginatedProducts);
    }

    private List<GetProductDTO> filterProducts(String name, String category, String eventType,
                                               Double minPrice, Double maxPrice, Boolean available,
                                               Boolean visible, String description) {
        return products.stream()
                .filter(p -> (name == null || p.getName().toLowerCase().contains(name.toLowerCase())) &&
                        (category == null || p.getCategoryId() > 0) &&
                        (minPrice == null || p.getPrice() >= minPrice) &&
                        (maxPrice == null || p.getPrice() <= maxPrice) &&
                        (available == null || p.isAvailable() == available) &&
                        (description == null || p.getDescription().toLowerCase().contains(description.toLowerCase())))
                .collect(Collectors.toList());
    }

    private List<GetProductDTO> paginateProducts(List<GetProductDTO> products, int page, int size) {
        int fromIndex = Math.min(page * size, products.size());
        int toIndex = Math.min((page + 1) * size, products.size());
        return products.subList(fromIndex, toIndex);
    }
}
