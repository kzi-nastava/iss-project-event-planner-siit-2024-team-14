package edu.ftn.iss.eventplanner.services;

import edu.ftn.iss.eventplanner.entities.ServiceAndProductProvider;
import edu.ftn.iss.eventplanner.exceptions.NotFoundException;
import edu.ftn.iss.eventplanner.repositories.ServiceAndProductProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceAndProductProviderService {

    @Autowired
    private ServiceAndProductProviderRepository providerRepository;

    public ServiceAndProductProvider getProviderById(int id) {
        return providerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Provider not found"));
    }
}
