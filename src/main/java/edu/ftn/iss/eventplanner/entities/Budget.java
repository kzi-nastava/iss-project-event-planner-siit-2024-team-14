package edu.ftn.iss.eventplanner.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Budget {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(optional = false)
    @JoinColumn(name = "event_id", nullable = false, unique = true)
    private Event event;

    @OneToMany(mappedBy = "budget", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<BudgetItem> items = new ArrayList<>();



    public Budget(Event event) {
        this.event = Objects.requireNonNull(event);
    }



    @Transient
    public double getAmount() {
        return items.stream()
                .mapToDouble(BudgetItem::getAmount)
                .sum();
    }


    public double getAmount(SolutionCategory category) {
        return getItem(category).orElseThrow().getAmount();
    }


    @Transient
    public double getSpent() {
        return event.getHeldSolutions().stream()
                .mapToDouble(Solution::getPrice)
                .sum();
    }


    public double getSpent(SolutionCategory category) {
        return getItem(category).orElseThrow().getSpent();
    }


    public Optional<BudgetItem> getItem(SolutionCategory category) {
        return items.stream()
                .filter(bi -> bi.getCategory().equals(category))
                .findFirst();
    }


    public void addItem(SolutionCategory category, double amount) {
        getItem(category).ifPresentOrElse(
                bi -> bi.setAmount(amount),
                () -> items.add(new BudgetItem(this, category, amount))
        );
    }


    @Transient
    public boolean isEventOverBudget() {
        return items.stream().anyMatch(BudgetItem::isEventOverBudget) ||
                getSpent() > getAmount();
    }

}

// TODO: All solution categories found in event held solutions should have a budget item