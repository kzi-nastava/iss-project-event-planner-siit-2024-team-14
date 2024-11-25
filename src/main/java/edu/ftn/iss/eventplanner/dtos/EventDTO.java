package edu.ftn.iss.eventplanner.dtos;

import lombok.Data;

@Data
public class EventDTO {
    private String name;
    private String description;
    private String location;
    private String startDate;
    private String endDate;

    public EventDTO(String name, String description, String location, String startDate, String endDate) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
