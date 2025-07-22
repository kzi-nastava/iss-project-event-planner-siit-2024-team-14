package edu.ftn.iss.eventplanner.controllers;
import edu.ftn.iss.eventplanner.dtos.homepage.SolutionDTO;
import edu.ftn.iss.eventplanner.services.BlockedUserService;
import edu.ftn.iss.eventplanner.services.SolutionService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class SolutionController {

    private final SolutionService solutionService;
    private final BlockedUserService blockedUserService;

    public SolutionController(SolutionService solutionService, BlockedUserService blockedUserService) {
        this.solutionService = solutionService;
        this.blockedUserService = blockedUserService;
    }

    @GetMapping("/api/solutions/top5")
    public ResponseEntity<List<SolutionDTO>> getTop5Solutions(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) Integer userId) {

        List<Integer> blockedUserIds = userId != null ? blockedUserService.getBlockedUsers(userId) : List.of();
        return ResponseEntity.ok(solutionService.getTop5Solutions(city, blockedUserIds));
    }

    @GetMapping("api/solutions/all")
    public ResponseEntity<List<SolutionDTO>> getAllSolutions(@RequestParam(required = false) Integer userId) {
        List<Integer> blockedUserIds = List.of();
        if (userId != null) {
            blockedUserIds = blockedUserService.getBlockedUsers(userId);
        }
        return ResponseEntity.ok(solutionService.getSolutions(blockedUserIds));
    }

    @GetMapping("api/solutions/locations")
    public ResponseEntity<List<String>> getAllLocations() {
        return ResponseEntity.ok(solutionService.getAllLocations());
    }

    @GetMapping("api/solutions/categories")
    public ResponseEntity<List<String>> getAllCategories() {
        return ResponseEntity.ok(solutionService.getAllCategories());
    }


    @GetMapping("/api/solutions/filter")
    public ResponseEntity<Page<SolutionDTO>> searchSolutions(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String location,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Integer userId) {

        List<Integer> blockedUserIds = List.of();
        if (userId != null) {
            blockedUserIds = blockedUserService.getBlockedUsers(userId);
        }
        Page<SolutionDTO> solutions = solutionService.getFilteredSolutions(startDate, endDate, category, type, minPrice, maxPrice, location, page, size, blockedUserIds);
        return ResponseEntity.ok(solutions);
    }
}
