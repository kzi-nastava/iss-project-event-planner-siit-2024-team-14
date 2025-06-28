package edu.ftn.iss.eventplanner.dtos.getUsers;

import lombok.Data;

@Data
public class GetProviderDTO {
    private String message;
    private ProviderDTO provider;
}
