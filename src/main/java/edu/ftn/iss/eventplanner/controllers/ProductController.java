package edu.ftn.iss.eventplanner.controllers;

import edu.ftn.iss.eventplanner.dtos.*;
import edu.ftn.iss.eventplanner.dtos.SolutionFilterParamsDto;
import edu.ftn.iss.eventplanner.dtos.productDetails.ProductDTO;
import edu.ftn.iss.eventplanner.dtos.serviceDetails.ServiceDTO;
import edu.ftn.iss.eventplanner.entities.*;
import edu.ftn.iss.eventplanner.entities.SolutionSearchRequest;
import edu.ftn.iss.eventplanner.enums.OfferingVisibility;
import edu.ftn.iss.eventplanner.exceptions.InternalServerError;
import edu.ftn.iss.eventplanner.exceptions.NotFoundException;
import edu.ftn.iss.eventplanner.mappers.ProductDTOMapper;
import edu.ftn.iss.eventplanner.repositories.CategoryRepository;
import edu.ftn.iss.eventplanner.repositories.ProductRepository;
import edu.ftn.iss.eventplanner.services.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping(path = {"api/products"}, produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductController {

    private final ProductService productService;
    private final ProductDTOMapper modelMapper;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public ProductController(ProductService productService, ProductDTOMapper modelMapper, ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productService = productService;
        this.modelMapper = modelMapper;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    // GET */api/products (Result differs across user roles)
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<ProductDTO> getAllProducts(
            @RequestParam(required = false) String q,
            @ModelAttribute SolutionFilterParamsDto filters,
            Pageable pageable,
            Principal principal
    ) {
        SolutionSearchRequest searchRequest = new SolutionSearchRequest(q, filters.toFilterParams(), principal == null ? null : principal.getName());
        Page<Product> products = productService.getAllProducts(searchRequest, pageable);
        return products.map(modelMapper::toProductDTO);
    }

    // GET @*/api/products/1
    @GetMapping(path = "/{id:\\d+}")
    public ResponseEntity<ProductDTO> getProductById(
            @PathVariable(name = "id") int id
    ) {
        Product product = productService.getProductById(id); // maybe hide products that are not publicly visible
        return ResponseEntity.ok(modelMapper.toProductDTO(product));
    }

    @PostMapping
    @PreAuthorize("hasRole('PROVIDER')")
    void createProductRedirect(
            HttpServletRequest request,
            HttpServletResponse response,
            @AuthenticationPrincipal ServiceAndProductProvider principal,
            UriComponentsBuilder uriBuilder
    )
    {
        String path = uriBuilder.path("/api/providers/{id}/products")
                .buildAndExpand(principal.getId())
                .getPath();

        try {
            request.getRequestDispatcher(path).forward(request, response);
        } catch (ServletException | IOException e) {
            throw new InternalServerError(e.getMessage());
        }
    }


    @PutMapping(path = "/{id:\\d+}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROVIDER') and true") //
    ResponseEntity<ProductDTO> putUpdateProduct(
            @PathVariable int id,
            @RequestBody @Validated UpdateProductDTO updateProductDTO
    ) {
        updateProductDTO.setId(id);
        Product product = productService.updateProduct(modelMapper.toUpdateProductRequest(updateProductDTO));
        return ResponseEntity.ok(modelMapper.toProductDTO(product));
    }

    @Transactional
    public Product updateProduct(UpdateProductDTO dto) {
        Product product = productRepository.findById(dto.getId())
                .orElseThrow(() -> new NotFoundException("Product not found"));

        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setSpecificities(dto.getSpecificities());
        product.setPrice(dto.getPrice());
        product.setDiscount(dto.getDiscount());
        product.setAvailable(dto.getAvailable() != null ? dto.getAvailable() : product.isAvailable());
        product.setVisible(dto.getVisibility() != null && dto.getVisibility() == OfferingVisibility.PUBLIC);

        if (dto.getCategoryIds() != null && !dto.getCategoryIds().isEmpty()) {
            SolutionCategory category = categoryRepository.findById(dto.getCategoryIds().get(0))
                    .orElseThrow(() -> new NotFoundException("Category not found"));
            product.setCategory(category);
        }

        return productRepository.save(product);
    }

    // DELETE provider[Provides the product]|admin@*/api/products/1
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/{id:\\d+}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROVIDER') and true")
    void deleteProduct(
            @PathVariable(name = "id") int id
    ) {
        productService.deleteProduct(id);
    }

    // DELETE admin@*/api/products
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteAllProducts() {
        productService.deleteAllProducts();
    }

}
