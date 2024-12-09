package edu.ftn.iss.eventplanner.controllers;

import edu.ftn.iss.eventplanner.dtos.SolutionDTO;
import edu.ftn.iss.eventplanner.entities.Solution;
import edu.ftn.iss.eventplanner.entities.Service;
import edu.ftn.iss.eventplanner.entities.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class SolutionController {

    // Endpoint za Top 5 usluga i proizvoda
    @GetMapping("/api/solutions/top5")
    public ResponseEntity<List<SolutionDTO>> getTop5Solutions(@RequestParam String city) {
        List<Solution> topSolutions = getTop5SolutionsFromDb(city);

        // Pretvaranje u DTO i slanje odgovora
        List<SolutionDTO> solutionDTOs = topSolutions.stream()
                .map(solution -> new SolutionDTO(solution.getId(), solution.getName(), solution.getDescription(), solution.getPrice(), solution.getDiscount(), solution.getImageUrl(), solution.isAvailable()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(solutionDTOs);
    }

    // Endpoint za pretragu i filtriranje usluga/proizvoda
    @GetMapping("/api/solutions/search")
    public ResponseEntity<List<SolutionDTO>> searchSolutions(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String availability,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) Boolean isProduct,  // parametar za filtriranje samo proizvoda ili usluga
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        // Dohvatanje svih usluga/proizvoda sa pretragom
        List<Solution> allSolutions = searchSolutionsFromDb(name, type, availability, startDate, endDate, isProduct);

        // Paginacija
        List<SolutionDTO> paginatedSolutions = paginateSolutions(allSolutions, page, size);

        return ResponseEntity.ok(paginatedSolutions);
    }

    // Helper metoda za pretragu i filtriranje usluga/proizvoda (simulacija)
    private List<Solution> searchSolutionsFromDb(String name, String type, String availability, LocalDate startDate, LocalDate endDate, Boolean isProduct) {
        List<Solution> solutions = new ArrayList<>();

        // Filtriranje po tipu (proizvod ili usluga)
        if (isProduct != null) {
            if (isProduct) {
                solutions = solutions.stream()
                        .filter(solution -> solution instanceof Product)
                        .collect(Collectors.toList());
            } else {
                solutions = solutions.stream()
                        .filter(solution -> solution instanceof Service)
                        .collect(Collectors.toList());
            }
        }

        return solutions;
    }

    // Helper metoda za paginaciju
    private List<SolutionDTO> paginateSolutions(List<Solution> solutions, int page, int size) {
        int start = page * size;
        int end = Math.min(start + size, solutions.size());
        if (start > solutions.size()) {
            return new ArrayList<>();
        }

        return solutions.subList(start, end).stream()
                .map(solution -> new SolutionDTO(solution.getId(), solution.getName(), solution.getDescription(), solution.getPrice(), solution.getDiscount(), solution.getImageUrl(), solution.isAvailable()))
                .collect(Collectors.toList());
    }

    // Helper metoda za dobijanje Top 5 usluga/proizvoda
    private List<Solution> getTop5SolutionsFromDb(String city) {
        List<Solution> solutions = new ArrayList<>();

        return solutions;
    }
}
