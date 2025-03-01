package edu.ftn.iss.eventplanner.mappers;

import edu.ftn.iss.eventplanner.dtos.GetProductDTO;
import edu.ftn.iss.eventplanner.dtos.ProductPurchaseDTO;
import edu.ftn.iss.eventplanner.entities.Product;
import edu.ftn.iss.eventplanner.entities.PurchaseProduct;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProductDTOMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public ProductDTOMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }


    public GetProductDTO toProductDTO(Product product) {
        GetProductDTO productDTO = modelMapper.map(product, GetProductDTO.class);

        Optional.ofNullable(product.getCategory())
                        .ifPresent(category -> productDTO.setCategoryId(category.getId()));

        return productDTO;
    }

    public ProductPurchaseDTO toProductPurchaseDTO(PurchaseProduct purchase) {
        return modelMapper.map(purchase, ProductPurchaseDTO.class);
    }
}
