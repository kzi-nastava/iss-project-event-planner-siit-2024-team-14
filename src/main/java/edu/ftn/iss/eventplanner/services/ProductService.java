package edu.ftn.iss.eventplanner.services;

import edu.ftn.iss.eventplanner.entities.Event;
import edu.ftn.iss.eventplanner.entities.Product;
import edu.ftn.iss.eventplanner.entities.PurchaseProduct;
import edu.ftn.iss.eventplanner.exceptions.BadRequestException;
import edu.ftn.iss.eventplanner.exceptions.InternalServerError;
import edu.ftn.iss.eventplanner.exceptions.NotFoundException;
import edu.ftn.iss.eventplanner.repositories.ProductPurchaseRepository;
import edu.ftn.iss.eventplanner.repositories.ProductRepository;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository products;
    private final ProductPurchaseRepository purchases;
    private final EventService events;


    @Autowired
    public ProductService(ProductRepository products, ProductPurchaseRepository purchases, EventService events) {
        this.products = products;
        this.purchases = purchases;
        this.events = events;
    }



    public Product getProductById(int id) {
        return products.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));
    }


    public PurchaseProduct purchaseProduct(int eventId, int productId) {
        try {
            Event event = events.getEventById(eventId);
            Product product = getProductById(productId);

            PurchaseProduct purchase = new PurchaseProduct(event, product);
            return purchases.save(purchase);
        } catch (IllegalArgumentException | IllegalStateException | ConstraintViolationException | DataIntegrityViolationException | NotFoundException e) {
            throw new BadRequestException(e.getMessage());
        } catch (Exception e) {
            throw new InternalServerError("An unexpected error has occurred");
        }
    }


    public List<Product> getPurchasedProductsForEvent(int eventId) {
        return getPurchasesForEvent(eventId).stream()
                .map(PurchaseProduct::getProduct)
                .toList();
    }


    @SuppressWarnings("unused")
    public List<Product> getAvailableProductsForEvent(int eventId) {
        return products.getByAvailableTrue();
    }


    public List<PurchaseProduct> getPurchasesForEvent(int eventId) {
        return purchases.getByEvent_Id(eventId);
    }


    public PurchaseProduct getProductPurchase(int eventId, int productId) {
        return purchases.getByEvent_IdAndProduct_Id(eventId, productId)
                .orElseThrow(() -> new NotFoundException("Product purchase not found"));
    }

}
