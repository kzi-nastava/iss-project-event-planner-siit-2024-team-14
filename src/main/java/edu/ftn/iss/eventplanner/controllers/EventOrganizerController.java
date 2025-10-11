package edu.ftn.iss.eventplanner.controllers;

import edu.ftn.iss.eventplanner.dtos.getUsers.GetOrganizerDTO;
import edu.ftn.iss.eventplanner.dtos.registration.RegisterEoDTO;
import edu.ftn.iss.eventplanner.dtos.registration.RegisterResponseDTO;
import edu.ftn.iss.eventplanner.dtos.reports.ViewOrganizerProfileDTO;
import edu.ftn.iss.eventplanner.dtos.updateUsers.UpdateOrganizerDTO;
import edu.ftn.iss.eventplanner.dtos.updateUsers.UpdateToOrganizerDTO;
import edu.ftn.iss.eventplanner.dtos.updateUsers.UpdateToProviderDTO;
import edu.ftn.iss.eventplanner.dtos.updateUsers.UpdatedOrganizerDTO;
import edu.ftn.iss.eventplanner.entities.Event;
import edu.ftn.iss.eventplanner.services.EventOrganizerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/{id}")
    public ResponseEntity<ViewOrganizerProfileDTO> getOrganizerById(@PathVariable Integer id) {
        ViewOrganizerProfileDTO organizer = organizerService.getOrganizerById(id);
        if (organizer != null) {
            return ResponseEntity.ok(organizer);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/get-photo/{id}")
    public ResponseEntity<Resource> getProfilePhoto(@PathVariable("id") int id) throws MalformedURLException {
        System.out.println("ENTERED getProfilePhoto CONTROLLER");
        return organizerService.getProfilePhoto(id);
    }

    @PutMapping("/update")
    public ResponseEntity<UpdatedOrganizerDTO> update(@RequestBody UpdateOrganizerDTO updateOrganizerDTO) {
        Integer userId = updateOrganizerDTO.getId();

        UpdatedOrganizerDTO updatedOrganizer = organizerService.update(userId, updateOrganizerDTO);

        return ResponseEntity.ok(updatedOrganizer);
    }

    @PutMapping("/update-photo/{id}")
    public ResponseEntity<RegisterResponseDTO> updateProfilePhoto(@PathVariable("id") int id,
                                                                  @RequestParam("photo") MultipartFile photo) {
        return organizerService.updateProfilePhoto(id, photo);
    }

    @PostMapping("/upgrade-to-organizer")
    public ResponseEntity<Void> upgradeToOrganizer(
            @RequestPart("dto") UpdateToOrganizerDTO dto,
            @RequestPart("photo") MultipartFile photo
    ) {
        try {
            organizerService.upgradeUserToOrganizer(dto, photo);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/deactivate/{id}")
    public ResponseEntity<RegisterResponseDTO> deactivate(@PathVariable int id) {
        return organizerService.deactivate(id);
    }


    @GetMapping("/{userId}/favorite-events")
    public ResponseEntity<List<Event>> getFavoriteEvents(@PathVariable Integer userId) {
        List<Event> favoriteEvents = organizerService.getFavoriteEventsByUserId(userId);
        return ResponseEntity.ok(favoriteEvents);
    }

    @PostMapping("/{userId}/favorite-events/{eventId}")
    public ResponseEntity<Map<String, Boolean>> toggleFavoriteEvent(
            @PathVariable Integer userId,
            @PathVariable Integer eventId) {
        boolean isFavorite = organizerService.toggleFavoriteEvent(userId, eventId);
        return ResponseEntity.ok(Map.of("isFavorite", isFavorite));
    }

}
