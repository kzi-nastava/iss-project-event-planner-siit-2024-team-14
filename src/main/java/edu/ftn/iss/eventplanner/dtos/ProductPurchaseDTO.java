package edu.ftn.iss.eventplanner.dtos;

import edu.ftn.iss.eventplanner.dtos.homepage.EventDTO;

import java.time.LocalDateTime;

@lombok.Data
public class ProductPurchaseDTO {
    private EventDTO event;
    private GetProductDTO product;
    private LocalDateTime purchaseDate;
}
