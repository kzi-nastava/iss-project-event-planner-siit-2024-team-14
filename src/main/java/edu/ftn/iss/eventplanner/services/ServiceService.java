package edu.ftn.iss.eventplanner.services;

import edu.ftn.iss.eventplanner.entities.Service;
import edu.ftn.iss.eventplanner.enums.Role;
import edu.ftn.iss.eventplanner.exceptions.InternalServerError;
import edu.ftn.iss.eventplanner.exceptions.NotFoundException;
import edu.ftn.iss.eventplanner.repositories.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@org.springframework.stereotype.Service
public class ServiceService {

    private final ServiceRepository services;

    @Autowired
    public ServiceService(ServiceRepository services) {
        this.services = services;
    }


    public List<Service> getAllServices() {
        return services.findAll();
    }

    public Page<Service> getAllServices(Pageable pageable) {
        return services.findAll(pageable);
    }

    public Page<Service> getAllServices(Pageable pageable, String username) {
        // TODO: filter services based on user and role
        return services.findAll(pageable);
    }


    public List<Service> getAllProviderServices(int providerId) {
        return services.findByProvider_Id(providerId);
    }

    public Page<Service> getAllProviderServices(int providerId, Pageable pageable) {
        return services.findByProvider_Id(providerId, pageable);
    }

    public Page<Service> getAllProviderServices(int providerId, Pageable pageable, String username) {
        // TODO: filter services based on user and role
        return getAllProviderServices(providerId, pageable);
    }


    public Service getServiceById(int id) {
        return services.findById(id)
                .orElseThrow(() -> new NotFoundException("Service with id " + id + " not found"));
    }


    public Service createService(Service serviceRequest) {
        throw new InternalServerError();
    }


    public Service updateService(Service serviceUpdateRequest) {
        throw new InternalServerError();
    }


    public void deleteService(int serviceId) {
        // TODO: Prevent deletion if there are any reservations
        services.softDeleteById(serviceId);
    }
}
