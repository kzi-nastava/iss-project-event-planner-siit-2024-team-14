package edu.ftn.iss.eventplanner.services;

import edu.ftn.iss.eventplanner.entities.Service;
import edu.ftn.iss.eventplanner.enums.Role;
import edu.ftn.iss.eventplanner.exceptions.InternalServerError;
import edu.ftn.iss.eventplanner.exceptions.NotFoundException;
import edu.ftn.iss.eventplanner.repositories.ServiceRepository;

import java.util.Collection;


@org.springframework.stereotype.Service
public class ServiceService {

    private ServiceRepository services;


    public Collection<Service> getAllServices() {
        return services.getAllByDeletedIsFalse();
    }


    public Collection<Service> getAllServicesFor(Role role) {
        switch (role) {
            case ADMIN:
            {
                return getAllServices();
            }
            default: {
                return services.getByVisibleIsTrueAndDeletedIsFalse();
            }
        }
    }


    public Collection<Service> getAllProviderServices(int providerId) {
        return services.getByProvider_IdAndDeletedIsFalse(providerId);
    }


    public Service getServiceById(int id) {
        return services.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new NotFoundException(""));
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
