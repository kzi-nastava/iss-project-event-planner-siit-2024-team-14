package edu.ftn.iss.eventplanner.controllers;

import edu.ftn.iss.eventplanner.dtos.registration.RegisterResponseDTO;
import edu.ftn.iss.eventplanner.dtos.registration.RegisterSppDTO;
import edu.ftn.iss.eventplanner.dtos.reports.ViewOrganizerProfileDTO;
import edu.ftn.iss.eventplanner.dtos.reports.ViewProviderProfileDTO;
import edu.ftn.iss.eventplanner.services.ServiceAndProductProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/providers")
public class ServiceAndProductProviderController {

    @Autowired
    private ServiceAndProductProviderService providerService;

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
}
