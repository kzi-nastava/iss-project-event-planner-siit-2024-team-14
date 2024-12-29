package edu.ftn.iss.eventplanner.services;

import edu.ftn.iss.eventplanner.entities.Service;
import edu.ftn.iss.eventplanner.enums.Role;
import jakarta.validation.constraints.NotNull;

import java.util.Collection;
import java.util.Optional;

public interface ServiceService {

    Collection<Service> getAllServices();
    Collection<Service> getAllProviderServices(int providerId);
    Collection<Service> getAllServicesFor(Role role);

    @NotNull Service getServiceById(int id);

    @NotNull Service createService(@NotNull Service serviceRequest);
    @NotNull Service updateService(@NotNull Service serviceUpdateRequest);

    void deleteService(int serviceId);
    default void deleteAllServices() { throw new UnsupportedOperationException(); };

}
