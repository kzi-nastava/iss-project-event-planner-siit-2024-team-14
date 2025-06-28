package edu.ftn.iss.eventplanner.services;
import edu.ftn.iss.eventplanner.dtos.homepage.SolutionDTO;
import edu.ftn.iss.eventplanner.entities.Solution;
import edu.ftn.iss.eventplanner.repositories.SolutionBookingRepository;
import edu.ftn.iss.eventplanner.repositories.SolutionRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.PageRequest;

@Service
public class SolutionService {
    private final SolutionRepository solutionRepository;
    private final SolutionBookingRepository solutionBookingRepository;

    /**
     * Constructor injection of required repositories.
     */
    public SolutionService(SolutionRepository solutionRepository, SolutionBookingRepository solutionBookingRepository) {
        this.solutionRepository = solutionRepository;
        this.solutionBookingRepository = solutionBookingRepository;
    }

    /**
     * Retrieves the top 5 available, visible, and not deleted solutions in a given city.
     *
     * @param city The city to filter solutions by
     * @return List of SolutionDTOs representing top 5 solutions
     */
    public List<SolutionDTO> getTop5Solutions(String city) {
        List<Solution> solutions = solutionRepository.findFirst5ByLocation(city).stream()
                .filter(s -> !s.isDeleted() && s.isVisible() && s.isAvailable())
                .limit(5)
                .toList();
        return mapToDTO(solutions);
    }

    /**
     * Retrieves all available, visible, and not deleted solutions.
     *
     * @return List of SolutionDTOs representing all solutions
     */
    public List<SolutionDTO> getSolutions() {
        List<Solution> solutions = solutionRepository.findAll().stream()
                .filter(s -> !s.isDeleted() && s.isVisible() && s.isAvailable())
                .toList();
        return mapToDTO(solutions);
    }

    /**
     * Gets all unique locations where solutions are available.
     *
     * @return List of location names as strings
     */
    public List<String> getAllLocations() {
        return solutionRepository.findAllLocations();
    }

    /**
     * Gets all unique categories of solutions.
     *
     * @return List of category names as strings
     */
    public List<String> getAllCategories() {
        return solutionRepository.findAllCategories();
    }

    /**
     * Retrieves a paginated list of filtered solutions based on various parameters.
     *
     * @param startDate Filter start date as String (nullable)
     * @param endDate Filter end date as String (nullable)
     * @param category Category filter (nullable)
     * @param type Type of solution: "product" or "service" (nullable)
     * @param minPrice Minimum price filter (nullable)
     * @param maxPrice Maximum price filter (nullable)
     * @param location Location filter (nullable)
     * @param page Page number for pagination
     * @param size Page size for pagination
     * @return Page of SolutionDTOs matching the filters
     */
    public Page<SolutionDTO> getFilteredSolutions(String startDate, String endDate, String category,
                                                  String type, Double minPrice, Double maxPrice,
                                                  String location, int page, int size) {

        // Parse input dates if provided
        LocalDate start = (startDate != null && !startDate.isEmpty()) ? LocalDate.parse(startDate) : null;
        LocalDate end = (endDate != null && !endDate.isEmpty()) ? LocalDate.parse(endDate) : null;

        // Map solution type string to entity class
        Class<?> mappedType = null;
        if (type != null) {
            switch (type.toLowerCase()) {
                case "product":
                    mappedType = edu.ftn.iss.eventplanner.entities.Product.class;
                    break;
                case "service":
                    mappedType = edu.ftn.iss.eventplanner.entities.Service.class;
                    break;
            }
        }

        // Find IDs of solutions already booked in the given date range (if dates provided)
        List<Long> bookedSolutionIds = (start != null && end != null)
                ? solutionBookingRepository.findBookedSolutionIds(start, end)
                : null;

        // Retrieve filtered and available solutions with pagination
        Page<Solution> solutionPage = solutionRepository.findAvailableSolutions(
                category, mappedType, minPrice, maxPrice, location, bookedSolutionIds, PageRequest.of(page, size)
        );

        // Map entities to DTOs
        return solutionPage.map(this::mapToDTO);
    }

    /**
     * Converts a Solution entity to a SolutionDTO.
     *
     * @param solution Solution entity to convert
     * @return SolutionDTO with mapped fields
     */
    private SolutionDTO mapToDTO(Solution solution) {
        SolutionDTO dto = new SolutionDTO();
        dto.setId(solution.getId());
        dto.setName(solution.getName());
        dto.setDescription(solution.getDescription());
        dto.setPrice(solution.getPrice());
        dto.setDiscount(solution.getDiscount());
        dto.setImageUrl(solution.getImageUrl());
        dto.setLocation(solution.getLocation());
        dto.setSolutionType(solution.getClass().getSimpleName());

        if (solution.getProvider() != null) {
            dto.setProviderCompanyName(solution.getProvider().getCompanyName());
            dto.setProviderId(solution.getProvider().getId());
        }
        return dto;
    }

    /**
     * Converts a list of Solution entities to a list of SolutionDTOs.
     *
     * @param solutions List of Solution entities
     * @return List of mapped SolutionDTOs
     */
    private List<SolutionDTO> mapToDTO(List<Solution> solutions) {
        return solutions.stream().map(solution -> {
            SolutionDTO dto = new SolutionDTO();
            dto.setId(solution.getId());
            dto.setName(solution.getName());
            dto.setDescription(solution.getDescription());
            dto.setPrice(solution.getPrice());
            dto.setDiscount(solution.getDiscount());
            dto.setImageUrl(solution.getImageUrl());
            dto.setLocation(solution.getLocation());
            dto.setSolutionType(solution.getClass().getSimpleName());

            if (solution.getProvider() != null) {
                dto.setProviderCompanyName(solution.getProvider().getCompanyName());
                dto.setProviderId(solution.getProvider().getId());
            }

            return dto;
        }).collect(Collectors.toList());
    }
}
