package edu.ftn.iss.eventplanner.services;

import edu.ftn.iss.eventplanner.dtos.CreateProductDTO;
import edu.ftn.iss.eventplanner.dtos.UpdateProductDTO;
import edu.ftn.iss.eventplanner.entities.*;
import edu.ftn.iss.eventplanner.enums.OfferingVisibility;
import edu.ftn.iss.eventplanner.enums.Status;
import edu.ftn.iss.eventplanner.exceptions.BadRequestException;
import edu.ftn.iss.eventplanner.exceptions.InternalServerError;
import edu.ftn.iss.eventplanner.exceptions.NotFoundException;
import edu.ftn.iss.eventplanner.repositories.ProductRepository;
import edu.ftn.iss.eventplanner.repositories.ServiceAndProductProviderRepository;
import edu.ftn.iss.eventplanner.repositories.SolutionCategoryRepository;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ServiceAndProductProviderRepository providerRepository;
    private final SolutionCategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final EventTypeService eventTypeService;
    private final ServiceAndProductProviderService providerService;
    private final CategoryService categoryService;

    public Product createProduct(@Valid CreateProductRequest productRequest) {
        try {
            Product product = mapRequestToProduct(productRequest);

            product.setProvider(providerService.getById(productRequest.getProviderId()) );
            if (productRequest.requestsNewCategory()) {
                product.setCategory(requestNewCategory(productRequest));
                product.setStatus(Status.PENDING);
            } else {
                product.setCategory(categoryService.getCategoryById(productRequest.getCategoryId()));
                product.setStatus(Status.APPROVED);
            }

            List<EventType> applicableEventTypes = productRequest.getApplicableEventTypeIds()
                    .stream()
                    .map(eventTypeService::getEventTypeById)
                    .collect(Collectors.toList());

            product.setApplicableEventTypes(applicableEventTypes);

            return productRepository.save(product);
        } catch (NotFoundException e) {
            throw new NotFoundException("Failed to create new product: " + e.getMessage());
        } catch (DataIntegrityViolationException | ConstraintViolationException | IllegalArgumentException e) {
            throw new BadRequestException("Failed to create a new product: " + e.getMessage());
        } catch (Exception e) {
            throw new InternalServerError("Failed to create new product. An unexpected error has occurred.");
        }
    }

    private Product mapRequestToProduct(CreateProductRequest productRequest) {
        // blah, some problems with mapper
        Product product = new Product();
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setSpecificities(productRequest.getSpecificities());
        product.setPrice(productRequest.getPrice());
        product.setDiscount(productRequest.getDiscount());
        product.setVisible(productRequest.getVisibility() == OfferingVisibility.PUBLIC);
        product.setAvailable(productRequest.isAvailable());
        return product;
    }

    private SolutionCategory requestNewCategory(CreateProductRequest request) {
        // TODO: Add a method for requesting categories
        SolutionCategory category = new SolutionCategory();
        category.setName(request.getCategoryName());
        category.setDescription(request.getCategoryDescription());
        category.setStatus(Status.PENDING);
        // TODO: Notify admin of category request
        return categoryService.insertCategory(category);
    }

    public Page<Product> getAllProducts(SolutionSearchRequest searchRequest, Pageable pageable) {
        if (searchRequest.getFilterParams() != null && searchRequest.getFilterParams().getProviderId() != null) {
            Integer providerId = searchRequest.getFilterParams().getProviderId();
            ServiceAndProductProvider provider = providerRepository.findById(providerId)
                    .orElseThrow(() -> new NotFoundException("Provider not found"));
            return productRepository.findByProvider_Id(provider.getId(), pageable);
        }

        return productRepository.findAll(pageable);
    }


    public Product getProductById(int id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found with id: " + id));
    }

    @Transactional
    public Product updateProduct(UpdateProductRequest request) {
        try {
            Product product = getProductById(request.getId());
            modelMapper.map(request, product); // doesn't map null

            if (request.getApplicableEventTypeIds() != null) {
                List<EventType> applicableEventTypes = request.getApplicableEventTypeIds()
                        .stream()
                        .map(eventTypeService::getEventTypeById)
                        .collect(Collectors.toList());

                product.setApplicableEventTypes(applicableEventTypes);
            }

            return product;
        } catch (NotFoundException e) {
            throw new NotFoundException("Failed to update product: " + e.getMessage());
        } catch (DataIntegrityViolationException | ConstraintViolationException | IllegalArgumentException e) {
            throw new BadRequestException("Failed to update product: " + e.getMessage());
        } catch (Exception e) {
            throw new InternalServerError("Failed to update product. An unexpected error has occurred.");
        }
    }

    public void deleteProduct(int productId) {
        productRepository.deleteById(productId);
    }


    // ✅ DELETE (all)
    @Transactional
    public void deleteAllProducts() {
        productRepository.deleteAll();
    }
}
