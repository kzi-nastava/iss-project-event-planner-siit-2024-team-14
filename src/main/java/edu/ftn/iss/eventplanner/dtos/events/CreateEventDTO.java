package edu.ftn.iss.eventplanner.dtos.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateEventDTO {
    private String name;
    private String description;
    private List<String> categories;
    private int guestNumber;
    private String type;
    private String location;
    private LocalDate startDate;
    private LocalDate endDate;
    private String eventType;
    private int organizer;
    private String photo;
}
