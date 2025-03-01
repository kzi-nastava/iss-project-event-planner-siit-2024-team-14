package edu.ftn.iss.eventplanner.dtos;

import edu.ftn.iss.eventplanner.dtos.get.ProviderDTO;
import lombok.Data;

import java.util.List;

@Data
public class GetProviderDTO {
    private String message;
    private ProviderDTO provider;
}
