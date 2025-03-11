package edu.ftn.iss.eventplanner.services;

import edu.ftn.iss.eventplanner.entities.*;
import edu.ftn.iss.eventplanner.enums.OfferingVisibility;
import edu.ftn.iss.eventplanner.enums.Role;
import edu.ftn.iss.eventplanner.enums.Status;
import edu.ftn.iss.eventplanner.exceptions.BadRequestException;
import edu.ftn.iss.eventplanner.exceptions.InternalServerError;
import edu.ftn.iss.eventplanner.exceptions.NotFoundException;
import edu.ftn.iss.eventplanner.repositories.ServiceRepository;
import edu.ftn.iss.eventplanner.repositories.SolutionSearchRepositoryMixin;
import edu.ftn.iss.eventplanner.repositories.UserRepository;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@org.springframework.stereotype.Service
public class ServiceService {

    private final ServiceRepository services;
    private final ServiceAndProductProviderService providerService;
    private final CategoryService categoryService;
    private final ModelMapper modelMapper;
    private final EventTypeService eventTypeService;
    private final UserRepository userRepository;


    @Autowired
    public ServiceService(ServiceRepository services, CategoryService categoryService, ServiceAndProductProviderService providerService, EventTypeService eventTypeService, ModelMapper modelMapper, UserRepository userRepository) {
        this.services = services;
        this.categoryService = categoryService;
        this.providerService = providerService;
        this.eventTypeService = eventTypeService;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
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


    public Service createService(@Valid CreateServiceRequest serviceRequest) {
        try {
            Service service = mapRequestToService(serviceRequest);

            service.setProvider( providerService.getById(serviceRequest.getProviderId()) );
            if (serviceRequest.requestsNewCategory()) {
                service.setCategory(requestNewCategory(serviceRequest));
                service.setStatus(Status.PENDING);
            } else {
                service.setCategory(categoryService.getCategoryById(serviceRequest.getCategoryId()));
                service.setStatus(Status.APPROVED);
            }

            List<EventType> applicableEventTypes = serviceRequest.getApplicableEventTypeIds()
                    .stream()
                    .map(eventTypeService::getEventTypeById)
                    .collect(Collectors.toList());

            service.setApplicableEventTypes(applicableEventTypes);

            return services.save(service);
        } catch (NotFoundException e) {
            throw new NotFoundException("Failed to create new service: " + e.getMessage());
        } catch (DataIntegrityViolationException | ConstraintViolationException | IllegalArgumentException e) {
            throw new BadRequestException("Failed to create a new service: " + e.getMessage());
        } catch (Exception e) {
            throw new InternalServerError("Failed to create new service. An unexpected error has occurred.");
        }
    }


    @Transactional
    public Service updateService(UpdateServiceRequest request) {
        try {
            Service service = getServiceById(request.getId());
            modelMapper.map(request, service); // doesn't map null

            if (request.getImages() != null && !request.getImages().isEmpty()) {
                service.setImageUrl(request.getImages().get(0));
            }

            List<EventType> applicableEventTypes = request.getApplicableEventTypeIds()
                    .stream()
                    .map(eventTypeService::getEventTypeById)
                    .collect(Collectors.toList());

            service.setApplicableEventTypes(applicableEventTypes);

            return service;
        } catch (NotFoundException e) {
            throw new NotFoundException("Failed to update service: " + e.getMessage());
        } catch (DataIntegrityViolationException | ConstraintViolationException | IllegalArgumentException e) {
            throw new BadRequestException("Failed to update service: " + e.getMessage());
        } catch (Exception e) {
            throw new InternalServerError("Failed to update service. An unexpected error has occurred.");
        }
    }


    public void deleteService(int serviceId) {
        // TODO: Prevent deletion if there are any reservations
        services.deleteById(serviceId);
    }


    public void deleteAllServices() {
        services.deleteAll();
    }


    private Service mapRequestToService(CreateServiceRequest serviceRequest) {
        // blah, some problems with mapper
        Service service = new Service();
        service.setName(serviceRequest.getName());
        service.setDescription(serviceRequest.getDescription());
        service.setSpecificities(serviceRequest.getSpecificities());
        service.setPrice(serviceRequest.getPrice());
        service.setDiscount(serviceRequest.getDiscount());
        if (serviceRequest.getImages() != null && !serviceRequest.getImages().isEmpty()) {
            service.setImageUrl(serviceRequest.getImages().get(0));
        }
        service.setVisible(serviceRequest.getVisibility() == OfferingVisibility.PUBLIC);
        service.setAvailable(serviceRequest.isAvailable());
        service.setReservationType(serviceRequest.getReservationType());
        service.setDuration(serviceRequest.getDuration());
        service.setMinDuration(serviceRequest.getMinDuration());
        service.setMaxDuration(serviceRequest.getMaxDuration());
        service.setReservationPeriod(serviceRequest.getReservationPeriod());
        service.setCancellationPeriod(serviceRequest.getCancellationPeriod());
        return service;
    }

    private SolutionCategory requestNewCategory(CreateServiceRequest request) {
        // TODO: Add a method for requesting categories
        SolutionCategory category = new SolutionCategory();
        category.setName(request.getCategoryName());
        category.setDescription(request.getCategoryDescription());
        category.setStatus(Status.PENDING);
        // TODO: Notify admin of category request
        return categoryService.insertCategory(category);
    }


    public List<Service> getAllServices(SolutionSearchRequest request)  {
        return services.findAll().stream().filter(request.getFilterParams()).toList();
    }


    public Page<Service> getAllServices(SolutionSearchRequest request, Pageable pageable) {
        // TODO: restrict access based on role userRepository.findByEmail(request.getSubmitterEmail()).orElseThrow();

        return services.search(
                request.getQuery(),
                request.getFilterParams() == null ? null : request.getFilterParams().toSpecification(),
                pageable
        );
    }
}