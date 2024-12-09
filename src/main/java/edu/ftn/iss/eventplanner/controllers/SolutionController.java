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
                .map(solution -> new SolutionDTO(solution.getId(), solution.getName(), solution.getDescription(), solution.getPrice(), solution.getDiscount(), solution.getImageUrl(), solution.isAvailable(), solution.getComments()))
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

        solutions.add(new Product(null, "Product 1", "Description 1", 100.0, 10.0, "image1.jpg", true, true, new ArrayList<>()));
        solutions.add(new Product(null, "Product 2", "Description 2", 150.0, 15.0, "image2.jpg", true, true, new ArrayList<>()));
        solutions.add(new Service(null, "Service 1", "Description 3", 120.0, 12.0, "image3.jpg", true, 60, "online", LocalDate.now(), LocalDate.now().plusDays(5), new ArrayList<>()));
        solutions.add(new Service(null, "Service 2", "Description 4", 130.0, 13.0, "image4.jpg", false, 45, "offline", LocalDate.now().plusDays(1), LocalDate.now().plusDays(7), new ArrayList<>()));

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
                .map(solution -> new SolutionDTO(solution.getId(), solution.getName(), solution.getDescription(), solution.getPrice(), solution.getDiscount(), solution.getImageUrl(), solution.isAvailable(), solution.getComments()))
                .collect(Collectors.toList());
    }

    // Helper metoda za dobijanje Top 5 usluga/proizvoda
    private List<Solution> getTop5SolutionsFromDb(String city) {
        List<Solution> solutions = new ArrayList<>();
        solutions.add(new Product(1L, "Product 1", "Description 1", 100.0, 10.0, "image1.jpg", true, true, new ArrayList<>()));
        solutions.add(new Product(2L, "Product 2", "Description 2", 120.0, 12.0, "image2.jpg", true, true, new ArrayList<>()));
        solutions.add(new Service(3L, "Service 1", "Description 3", 130.0, 13.0, "image3.jpg", true, 60, "online", LocalDate.now(), LocalDate.now().plusDays(5), new ArrayList<>()));
        solutions.add(new Service(4L, "Service 2", "Description 4", 140.0, 14.0, "image4.jpg", false, 45, "offline", LocalDate.now().plusDays(1), LocalDate.now().plusDays(7), new ArrayList<>()));
        solutions.add(new Product(5L, "Product 3", "Description 5", 110.0, 11.0, "image5.jpg", true, true, new ArrayList<>()));

        return solutions;
    }
}
