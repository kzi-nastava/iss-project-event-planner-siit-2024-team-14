package edu.ftn.iss.eventplanner.dtos;

import edu.ftn.iss.eventplanner.dtos.getUsers.ProviderDTO;
import lombok.Data;

@Data
public class GetProviderDTO {
    private String message;
    private ProviderDTO provider;
}
