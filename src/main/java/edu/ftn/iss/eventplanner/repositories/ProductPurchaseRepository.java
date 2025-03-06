package edu.ftn.iss.eventplanner.repositories;

import edu.ftn.iss.eventplanner.entities.PurchaseProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductPurchaseRepository extends JpaRepository<PurchaseProduct, Integer> {
    List<PurchaseProduct> getByEvent_Id(int eventId);
    Optional<PurchaseProduct> getByEvent_IdAndProduct_Id(int eventId, int productId);
}
