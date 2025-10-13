package edu.ftn.iss.eventplanner.services;

import edu.ftn.iss.eventplanner.dtos.CreateProductDTO;
import edu.ftn.iss.eventplanner.dtos.UpdateProductDTO;
import edu.ftn.iss.eventplanner.entities.*;
import edu.ftn.iss.eventplanner.enums.OfferingVisibility;
import edu.ftn.iss.eventplanner.enums.Status;
import edu.ftn.iss.eventplanner.exceptions.NotFoundException;
import edu.ftn.iss.eventplanner.repositories.ProductRepository;
import edu.ftn.iss.eventplanner.repositories.ServiceAndProductProviderRepository;
import edu.ftn.iss.eventplanner.repositories.SolutionCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ServiceAndProductProviderRepository providerRepository;
    private final SolutionCategoryRepository categoryRepository;

    // ✅ CREATE
    @Transactional
    public Product createProduct(CreateProductDTO dto) {
        Product product = new Product();

        // Basic fields
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setSpecificities(dto.getSpecificities());
        product.setPrice(dto.getPrice());
        product.setDiscount(dto.getDiscount());
        product.setImageUrl(dto.getImageUrl());
        product.setAvailable(dto.getAvailable());
        product.setVisible(dto.getVisible());

        // Status
        product.setStatus(dto.getCategoryId() != null ? Status.APPROVED : Status.PENDING);

        // Provider
        ServiceAndProductProvider provider = providerRepository.findById(dto.getProviderId())
                .orElseThrow(() -> new NotFoundException("Provider not found"));
        product.setProvider(provider);

        // Category
        if (dto.getCategoryId() != null) {
            SolutionCategory category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new NotFoundException("Category not found"));
            product.setCategory(category);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category information is missing");
        }

        return productRepository.save(product);
    }


    // ✅ READ (get all)
    public Page<Product> getAllProducts(SolutionSearchRequest searchRequest, Pageable pageable) {
        // You can adjust this to your actual filtering logic
        return productRepository.findAll(pageable);
    }

    // ✅ READ (get one)
    public Product getProductById(int id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found with id: " + id));
    }

    @Transactional
    public Product updateProduct(UpdateProductDTO dto) {
        Product product = productRepository.findById(dto.getId())
                .orElseThrow(() -> new NotFoundException("Product not found"));

        if (dto.getName() != null) product.setName(dto.getName());
        if (dto.getDescription() != null) product.setDescription(dto.getDescription());
        if (dto.getSpecificities() != null) product.setSpecificities(dto.getSpecificities());
        if (dto.getPrice() != null) product.setPrice(dto.getPrice());
        if (dto.getDiscount() != null) product.setDiscount(dto.getDiscount());

        // ✅ Handle image list safely
        if (dto.getImages() != null && !dto.getImages().isEmpty()) {
            product.setImageUrl(dto.getImages().get(0)); // only store 1 image
        }

        // ✅ Handle availability
        if (dto.getAvailable() != null) {
            product.setAvailable(dto.getAvailable());
        }

        // ✅ Handle visibility (OfferingVisibility.PUBLIC/PRIVATE)
        if (dto.getVisibility() != null) {
            product.setVisible(dto.getVisibility() == OfferingVisibility.PUBLIC);
        }

        return productRepository.save(product);
    }


    // ✅ DELETE (one)
    @Transactional
    public void deleteProduct(int id) {
        if (!productRepository.existsById(id)) {
            throw new NotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    // ✅ DELETE (all)
    @Transactional
    public void deleteAllProducts() {
        productRepository.deleteAll();
    }
}
