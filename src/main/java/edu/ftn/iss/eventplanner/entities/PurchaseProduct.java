package edu.ftn.iss.eventplanner.entities;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.util.Optional;

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
    private LocalDateTime purchaseDate = LocalDateTime.now();

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
        // this.purchaseDate = LocalDateTime.now();
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

    public void setEvent(Event event) {
        if (event == null) {
            throw new IllegalArgumentException("Event cannot be null");
        }

        if (event.getEndDate() != null && event.getEndDate().isBefore(ChronoLocalDate.from(purchaseDate))) {
            //throw new IllegalStateException("Event cannot be over at the time of the purchase");
        }

        this.event = event;
    }

    public void setPurchaseDate(LocalDateTime purchaseDate) {
        if (purchaseDate == null) {
            throw new IllegalArgumentException("PurchaseDate cannot be null");
        }

        if (event.getEndDate() != null && event.getEndDate().isBefore(ChronoLocalDate.from(purchaseDate))) {
            throw new IllegalStateException("Purchase date cannot be after the event has passed");
        }

        this.purchaseDate = purchaseDate;
    }
// maybe could do the validation with annotations?

    @PostPersist
    protected void addBudgetItemIfNecessary() {
        var category = product.getCategory();
        var budget = Optional.ofNullable(event.getBudget())
                .orElseGet(() -> {
                    event.setBudget(new Budget());
                    return event.getBudget();
                });

        if (budget.getItem(category).isEmpty())
            budget.addItem(category, 0d);
    }
}