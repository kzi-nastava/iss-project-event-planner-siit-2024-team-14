package edu.ftn.iss.eventplanner.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SolutionSearchRequest {
    private String query;
    private SolutionFilterParams filterParams;
    private String submitterEmail;
}
