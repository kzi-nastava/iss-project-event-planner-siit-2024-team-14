package edu.ftn.iss.eventplanner.entities;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        uniqueConstraints = { @UniqueConstraint(columnNames = {"event_id", "product_id"}) }
)
public class PurchaseProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, updatable = false)
    private LocalDateTime purchaseDate;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;


    public PurchaseProduct(Event event, Product product) {
        setEvent(event);
        setProduct(product);
    }

    @PrePersist
    protected void onCreate() {
        this.purchaseDate = LocalDateTime.now();
    }


    public void setProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }

        if (!product.isAvailable()) {
            throw new IllegalStateException("Product is not available");
        }

        this.product = product;
    }

}