package edu.ftn.iss.eventplanner.dtos;

import lombok.Data;

@Data
public class PriceDTO {
    private String offering;
    private String offeringName;
    private double price;
    private double discount;

    public double getPriceWithDiscount() {
        return price - price * discount;
    }
}
