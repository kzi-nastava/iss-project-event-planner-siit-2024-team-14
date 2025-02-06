package edu.ftn.iss.eventplanner.controllers;
import edu.ftn.iss.eventplanner.dtos.SolutionDTO;
import edu.ftn.iss.eventplanner.services.SolutionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseEntity<List<SolutionDTO>> getAllSolutions( @RequestParam String city) {
        return ResponseEntity.ok(solutionService.getSolutions(city));
    }

    /*
    // Endpoint za pretragu i filtriranje usluga/proizvoda
    @GetMapping("/api/solutions/search")

    public ResponseEntity<List<SolutionDTO>> searchSolutions(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String availability,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) Boolean isProduct,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

    }

     */
}
