package edu.ftn.iss.eventplanner.mappers;

import edu.ftn.iss.eventplanner.dtos.CreateProductDTO;
import edu.ftn.iss.eventplanner.dtos.productDetails.ProductDTO;
import edu.ftn.iss.eventplanner.entities.CreateProductRequest;
import edu.ftn.iss.eventplanner.entities.Product;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import edu.ftn.iss.eventplanner.dtos.UpdateProductDTO;
import edu.ftn.iss.eventplanner.entities.UpdateProductRequest;
import edu.ftn.iss.eventplanner.enums.OfferingVisibility;

import java.time.Duration;
import java.util.Optional;


@Component
public class ProductDTOMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public ProductDTOMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Product fromDTO(Object dto) { return this.modelMapper.map(dto, Product.class); }

    public ProductDTO toProductDTO(Product product) {
        ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
        productDTO.setVisibility(product.isVisible() ? OfferingVisibility.PUBLIC : OfferingVisibility.PRIVATE);

        return productDTO;
    }

    public CreateProductRequest toCreateProductRequest(CreateProductDTO productRequestDTO, int providerId) {
        CreateProductRequest createProductRequest = modelMapper.map(productRequestDTO, CreateProductRequest.class);
        createProductRequest.setProviderId(providerId);

        Optional.ofNullable(productRequestDTO.getCategory())
                .ifPresentOrElse(
                        cr -> {
                            if (cr.getId() != null) {
                                createProductRequest.setCategoryId(cr.getId());
                            } else {
                                createProductRequest.setCategoryName(cr.getName());
                                createProductRequest.setCategoryDescription(cr.getDescription());
                            }
                        },
                        () -> {/* maybe throw exception */}
                );

        return createProductRequest;
    }

    public UpdateProductRequest toUpdateProductRequest(UpdateProductDTO dto) {

        return modelMapper.map(dto, UpdateProductRequest.class);
    }

}
