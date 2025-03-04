package edu.ftn.iss.eventplanner.controllers;
import edu.ftn.iss.eventplanner.entities.BlockedUser;
import edu.ftn.iss.eventplanner.services.BlockedUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor

public class BlockedUserController {
    private final BlockedUserService userService;

    @PostMapping("/{blockerId}/block/{blockedId}")
    public ResponseEntity<String> blockUser(@PathVariable Integer blockerId, @PathVariable Integer blockedId) {
        BlockedUser user = userService.blockUser(blockerId, blockedId);
        return ResponseEntity.ok("Success");
    }
}
