package edu.ftn.iss.eventplanner.controllers;

import edu.ftn.iss.eventplanner.dtos.getUsers.GetProviderDTO;
import edu.ftn.iss.eventplanner.dtos.homepage.SolutionDTO;
import edu.ftn.iss.eventplanner.dtos.registration.RegisterResponseDTO;
import edu.ftn.iss.eventplanner.dtos.registration.RegisterSppDTO;
import edu.ftn.iss.eventplanner.dtos.reports.ViewProviderProfileDTO;
import edu.ftn.iss.eventplanner.dtos.updateUsers.UpdateProviderDTO;
import edu.ftn.iss.eventplanner.dtos.updateUsers.UpdateToProviderDTO;
import edu.ftn.iss.eventplanner.dtos.updateUsers.UpdatedProviderDTO;
import edu.ftn.iss.eventplanner.services.ServiceAndProductProviderService;
import edu.ftn.iss.eventplanner.services.SolutionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/providers")
public class ServiceAndProductProviderController {

    @Autowired
    private ServiceAndProductProviderService providerService;
    @Autowired
    private SolutionService solutionService;

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RegisterResponseDTO> register(@RequestPart("dto") RegisterSppDTO dto,
                                                        @RequestPart(value = "photos", required = false) List<MultipartFile> photos) {
        return providerService.register(dto, photos);
    }

    @GetMapping("/activate")
    public ResponseEntity<RegisterResponseDTO> activate(@RequestParam("token") String token) {
        return providerService.activate(token);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ViewProviderProfileDTO> getProviderById(@PathVariable Integer id) {
        ViewProviderProfileDTO provider = providerService.getProviderById(id);
        if (provider != null) {
            return ResponseEntity.ok(provider);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<GetProviderDTO> get(@PathVariable("id") int id) {
        return providerService.get(id);
    }

    @GetMapping("/get-photos/{id}")
    public ResponseEntity<List<String>> getPhotos(@PathVariable int id) {
        List<String> photoUrls = providerService.getPhotos(id);

        if (photoUrls.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(photoUrls);
    }

    @PutMapping("/update")
    public ResponseEntity<UpdatedProviderDTO> update (@RequestBody UpdateProviderDTO updateProviderDTO) {
        Integer userId = updateProviderDTO.getId();

        UpdatedProviderDTO updatedProviderDTO = providerService.update(userId, updateProviderDTO);

        return ResponseEntity.ok(updatedProviderDTO);
    }

    @PutMapping("/update-photo/{id}")
    public ResponseEntity<RegisterResponseDTO> updatePhoto(@PathVariable("id") int userId,
                                                           @RequestParam("photo") MultipartFile photo,
                                                           @RequestParam("photoIndex") int photoIndex) {

        return providerService.updatePhoto(userId, photo, photoIndex);
    }

    @PostMapping(value = "/upgrade-to-provider", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> upgradeToProvider(
            @RequestPart("dto") UpdateToProviderDTO dto,
            @RequestPart(value = "photos", required = false) List<MultipartFile> photos
    ) {
        try {
            providerService.upgradeUserToProvider(dto, photos);
            return ResponseEntity.ok("User upgraded to Service and Product Provider successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error saving photos: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body("Upgrade failed: " + e.getMessage());
        }
    }

    @PutMapping("/deactivate/{id}")
    public ResponseEntity<RegisterResponseDTO> deactivate(@PathVariable int id) {
        return providerService.deactivate(id);
    }


    @GetMapping(path = {"/{id:\\d+}/solutions"})
    @PreAuthorize("hasAnyRole('PROVIDER', 'ADMIN')")
    Object getProviderSolutions(
            @PathVariable int id,
            Pageable pageable,
            ModelMapper mapper
    ) {
        return solutionService.getProviderSolutions(id, pageable)
                .map(s -> {
                    var dto = mapper.map(s, SolutionDTO.class);
                    dto.setSolutionType(s.getClass().getSimpleName());
                    return dto;
                });
    }

}
