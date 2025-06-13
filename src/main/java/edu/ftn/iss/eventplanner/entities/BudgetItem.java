package edu.ftn.iss.eventplanner.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import jakarta.persistence.*;
import org.hibernate.annotations.Formula;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        uniqueConstraints = { @UniqueConstraint(columnNames = {"category_id", "budget_id"}) }
)
public class BudgetItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private double amount = 0d;

    @ManyToOne(optional = false)
    @JoinColumn(name = "budget_id", nullable = false)
    private Budget budget;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private SolutionCategory category;



    public BudgetItem(Budget budget, SolutionCategory category) {
        this(budget, category, 0d);
    }


    public BudgetItem(Budget budget, SolutionCategory category, double amount) {
        this.budget = Objects.requireNonNull(budget);
        this.category = Objects.requireNonNull(category);
        this.amount = amount;
    }


    @Transient
    public double getSpent() {
        Stream<Solution> solutions = Stream.concat(
                getPurchasedProducts(),
                getReservedServices()

        ).filter(s -> category.getId().equals(s.getCategory().getId()));

        return solutions.mapToDouble(Solution::getPrice).sum();
    }


    @Transient
    public boolean isEventOverBudget() {
        return getSpent() > amount;
    }


    private Stream<Product> getPurchasedProducts() { // should probably be in the event
        return budget.getEvent().getPurchases().stream().map(PurchaseProduct::getProduct);
    }


    private Stream<Service> getReservedServices() {
        return budget.getEvent().getReservations().stream().map(BookingService::getService);
    }


    @Transient
    public List<Solution> getItems() {
        return Stream.concat(getPurchasedProducts(), getReservedServices())
                .filter(solution -> Objects.equals(category.getId(), solution.getCategory().getId()))
                .toList();
    }

}
