package edu.ftn.iss.eventplanner.dtos.budget;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class SolutionItemDTO {
    private int id;
    private String name, description;
    private double price, discount;
    private String type;
}
