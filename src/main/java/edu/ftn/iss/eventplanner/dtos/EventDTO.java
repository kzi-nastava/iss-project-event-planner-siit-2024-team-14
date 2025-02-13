package edu.ftn.iss.eventplanner.dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EventDTO {

    private Integer id;
    private String name;
    private String description;
    private String location;
    private String privacyType;
    private LocalDate startDate;
    private LocalDate endDate;
    private double budget;
    private String imageUrl;
    private String organizerFirstName;
    private String organizerLastName;
    private String organizerProfilePicture;
}
