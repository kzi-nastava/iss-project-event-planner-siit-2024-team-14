package edu.ftn.iss.eventplanner.dtos.homepage;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolutionDTO {
    private Integer id;
    private String name;
    private String description;
    private String location;
    private double price;
    private double discount;
    private String imageUrl;
    private boolean isAvailable;
    private String providerCompanyName;
    private Integer providerId;
    private String solutionType;
}
