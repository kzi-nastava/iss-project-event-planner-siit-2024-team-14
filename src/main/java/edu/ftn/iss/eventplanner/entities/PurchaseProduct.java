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
        this.event = event;
        this.product = product;
    }

    @PrePersist
    protected void onCreate() {
        this.purchaseDate = LocalDateTime.now();
    }
}