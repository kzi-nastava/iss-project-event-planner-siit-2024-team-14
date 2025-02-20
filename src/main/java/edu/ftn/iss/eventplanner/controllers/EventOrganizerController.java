package edu.ftn.iss.eventplanner.controllers;

import edu.ftn.iss.eventplanner.dtos.get.GetOrganizerDTO;
import edu.ftn.iss.eventplanner.dtos.registration.RegisterEoDTO;
import edu.ftn.iss.eventplanner.dtos.registration.RegisterResponseDTO;
import edu.ftn.iss.eventplanner.dtos.update.UpdateOrganizerDTO;
import edu.ftn.iss.eventplanner.dtos.update.UpdatedOrganizerDTO;
import edu.ftn.iss.eventplanner.entities.User;
import edu.ftn.iss.eventplanner.services.EventOrganizerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/organizers")
public class EventOrganizerController {

    @Autowired
    private EventOrganizerService organizerService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(@RequestBody RegisterEoDTO dto) {
        return organizerService.register(dto);
    }

    @GetMapping("/activate")
    public ResponseEntity<RegisterResponseDTO> activate(@RequestParam("token") String token) {
        return organizerService.activate(token);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<GetOrganizerDTO> get(@PathVariable("id") int id) {
        return organizerService.get(id);
    }

    @PutMapping("/update")
    public ResponseEntity<UpdatedOrganizerDTO> update(@RequestBody UpdateOrganizerDTO updateOrganizerDTO) {
        Integer userId = updateOrganizerDTO.getId(); // Get the id from the request body

        UpdatedOrganizerDTO updatedOrganizer = organizerService.update(userId, updateOrganizerDTO);

        return ResponseEntity.ok(updatedOrganizer);
    }


}
