package edu.ftn.iss.eventplanner.controllers;

import edu.ftn.iss.eventplanner.dtos.SolutionFilterParamsDto;
import edu.ftn.iss.eventplanner.dtos.serviceDetails.ServiceDTO;
import edu.ftn.iss.eventplanner.dtos.UpdateServiceDTO;
import edu.ftn.iss.eventplanner.entities.Service;
import edu.ftn.iss.eventplanner.entities.ServiceAndProductProvider;
import edu.ftn.iss.eventplanner.entities.SolutionFilterParams;
import edu.ftn.iss.eventplanner.entities.SolutionSearchRequest;
import edu.ftn.iss.eventplanner.exceptions.BadRequestException;
import edu.ftn.iss.eventplanner.exceptions.InternalServerError;
import edu.ftn.iss.eventplanner.mappers.ServiceDTOMapper;
import edu.ftn.iss.eventplanner.services.ServiceService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.security.Principal;
import java.util.Optional;
import java.util.Set;


@RestController
@RequestMapping(path = {"api/services"}, produces = MediaType.APPLICATION_JSON_VALUE)
public class ServiceController {

    private final ServiceService serviceService;
    private final ServiceDTOMapper modelMapper;

    @Autowired
    public ServiceController(ServiceService serviceService, ServiceDTOMapper modelMapper) {
        this.serviceService = serviceService;
        this.modelMapper = modelMapper;
    }

    // GET */api/services (Result differs across user roles)
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Page<ServiceDTO> getAllServices(
            @RequestParam(required = false) String q,
            @ModelAttribute SolutionFilterParamsDto filters,
            Pageable pageable,
            Principal principal
    ) {
        SolutionSearchRequest searchRequest = new SolutionSearchRequest(q, filters.toFilterParams(), principal == null ? null : principal.getName());
        Page<Service> services = serviceService.getAllServices(searchRequest, pageable);
        return services.map(modelMapper::toServiceDTO);
    }

    // GET @*/api/services/1
    @GetMapping(path = "/{id:\\d+}")
    ResponseEntity<ServiceDTO> getServiceById(
            @PathVariable(name = "id") int id
    ) {
        Service service = serviceService.getServiceById(id); // maybe hide services that are not publicly visible
        return ResponseEntity.ok(modelMapper.toServiceDTO(service));
    }


    @PostMapping
    @PreAuthorize("hasRole('PROVIDER')")
    void createServiceRedirect(
            HttpServletRequest request,
            HttpServletResponse response,
            @AuthenticationPrincipal ServiceAndProductProvider principal,
            UriComponentsBuilder uriBuilder
    )
    {
        String path = uriBuilder.path("/api/providers/{id}/services")
                .buildAndExpand(principal.getId())
                .getPath();

        try {
            request.getRequestDispatcher(path).forward(request, response);
        } catch (ServletException | IOException e) {
            throw new InternalServerError(e.getMessage());
        }
    }


    // PUT provider[Provides the service]|admin@*/api/services/1
    @PutMapping(path = "/{id:\\d+}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROVIDER') and true") //
    ResponseEntity<ServiceDTO> putUpdateService(
            @PathVariable int id,
            @RequestBody @Validated UpdateServiceDTO updateServiceDTO
    ) {
        updateServiceDTO.setId(id);
        Service service = serviceService.updateService(modelMapper.toUpdateServiceRequest(updateServiceDTO));
        return ResponseEntity.ok(modelMapper.toServiceDTO(service));
    }

    // DELETE provider[Provides the service]|admin@*/api/services/1
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/{id:\\d+}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROVIDER') and true")
    void deleteService(
            @PathVariable(name = "id") int id
    ) {
        serviceService.deleteService(id);
    }


    // DELETE admin@*/api/services
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    void deleteAllServices() {
        serviceService.deleteAllServices();
    }

}