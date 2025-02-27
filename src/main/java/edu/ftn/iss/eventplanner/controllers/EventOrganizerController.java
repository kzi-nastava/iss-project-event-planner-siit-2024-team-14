package edu.ftn.iss.eventplanner.controllers;

import edu.ftn.iss.eventplanner.dtos.get.GetOrganizerDTO;
import edu.ftn.iss.eventplanner.dtos.registration.RegisterEoDTO;
import edu.ftn.iss.eventplanner.dtos.registration.RegisterResponseDTO;
import edu.ftn.iss.eventplanner.dtos.update.UpdateOrganizerDTO;
import edu.ftn.iss.eventplanner.dtos.update.UpdatedOrganizerDTO;
import edu.ftn.iss.eventplanner.services.EventOrganizerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;

@RestController
@RequestMapping("/api/organizers")
public class EventOrganizerController {

    @Autowired
    private EventOrganizerService organizerService;

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RegisterResponseDTO> register(@RequestPart("dto") RegisterEoDTO dto,
                                                        @RequestPart(value = "photo", required = false) MultipartFile photo) {
        return organizerService.register(dto, photo);
    }

    @GetMapping("/activate")
    public ResponseEntity<RegisterResponseDTO> activate(@RequestParam("token") String token) {
        return organizerService.activate(token);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<GetOrganizerDTO> get(@PathVariable("id") int id) {
        return organizerService.get(id);
    }

    @GetMapping("/get-photo/{filename}")
    public ResponseEntity<Resource> getProfilePhoto(@PathVariable String filename) throws MalformedURLException {
        return organizerService.getProfilePhoto(filename);
    }

    @PutMapping("/update")
    public ResponseEntity<UpdatedOrganizerDTO> update(@RequestBody UpdateOrganizerDTO updateOrganizerDTO) {
        Integer userId = updateOrganizerDTO.getId(); // Get the id from the request body

        UpdatedOrganizerDTO updatedOrganizer = organizerService.update(userId, updateOrganizerDTO);

        return ResponseEntity.ok(updatedOrganizer);
    }


}
