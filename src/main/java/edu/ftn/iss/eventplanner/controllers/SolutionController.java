package edu.ftn.iss.eventplanner.controllers;
import edu.ftn.iss.eventplanner.dtos.SolutionDTO;
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

    public SolutionController(SolutionService solutionService) {
        this.solutionService = solutionService;
    }

    // Endpoint za Top 5 događaja
    @GetMapping("/api/solutions/top5")
    public ResponseEntity<List<SolutionDTO>> getTop5Solutions(
            @RequestParam String city) {
        return ResponseEntity.ok(solutionService.getTop5Solutions(city));
    }

    @GetMapping("api/solutions/all")
    public ResponseEntity<List<SolutionDTO>> getAllSolutions() {
        return ResponseEntity.ok(solutionService.getSolutions());
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
            @RequestParam(defaultValue = "10") int size) {

        Page<SolutionDTO> solutions = solutionService.getFilteredSolutions(startDate, endDate, category, type, minPrice, maxPrice, location, page, size);
        return ResponseEntity.ok(solutions);
    }
}
