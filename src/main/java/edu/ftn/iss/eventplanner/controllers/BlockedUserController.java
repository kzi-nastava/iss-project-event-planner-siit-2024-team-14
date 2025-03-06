package edu.ftn.iss.eventplanner.controllers;
import edu.ftn.iss.eventplanner.entities.BlockedUser;
import edu.ftn.iss.eventplanner.services.BlockedUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/blocked-users")
    public ResponseEntity<List<Integer>> getBlockedUsers(@RequestParam Integer userId) {
        return  ResponseEntity.ok(userService.getBlockedUsers(userId));
    }
}
