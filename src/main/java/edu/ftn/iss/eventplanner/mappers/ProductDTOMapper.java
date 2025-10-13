package edu.ftn.iss.eventplanner.mappers;

import edu.ftn.iss.eventplanner.dtos.productDetails.ProductDTO;
import edu.ftn.iss.eventplanner.entities.Product;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import edu.ftn.iss.eventplanner.dtos.UpdateProductDTO;
import edu.ftn.iss.eventplanner.entities.UpdateProductRequest;
import edu.ftn.iss.eventplanner.enums.OfferingVisibility;


@Component
public class ProductDTOMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public ProductDTOMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ProductDTO toProductDTO(Product product) {
        ProductDTO dto = modelMapper.map(product, ProductDTO.class);
        dto.setImages(product.getImageUrl() != null ? new String[]{product.getImageUrl()} : new String[0]);
        return dto;
    }

    public UpdateProductRequest toUpdateProductRequest(UpdateProductDTO dto) {
        UpdateProductRequest request = new UpdateProductRequest();

        request.setId(dto.getId());
        request.setName(dto.getName());
        request.setDescription(dto.getDescription());
        request.setSpecificities(dto.getSpecificities());
        request.setPrice(dto.getPrice());
        request.setDiscount(dto.getDiscount());
        request.setImageUrl(dto.getImages() != null && !dto.getImages().isEmpty() ? dto.getImages().get(0) : null);
        request.setAvailable(dto.getAvailable());
        request.setVisible(dto.getVisibility() != null && dto.getVisibility() == OfferingVisibility.PUBLIC);
        request.setCategoryId(dto.getCategoryIds() != null && !dto.getCategoryIds().isEmpty() ? dto.getCategoryIds().get(0) : null);

        return request;
    }

}
