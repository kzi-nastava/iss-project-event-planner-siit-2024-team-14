package edu.ftn.iss.eventplanner.dtos;

import edu.ftn.iss.eventplanner.dtos.homepage.EventDTO;

import java.time.LocalDateTime;


public class ProductPurchaseDTO {
    private EventDTO event;
    private GetProductDTO product;
    private LocalDateTime purchaseDate;
}
