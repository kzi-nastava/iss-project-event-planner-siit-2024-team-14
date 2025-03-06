package edu.ftn.iss.eventplanner.services;
import edu.ftn.iss.eventplanner.dtos.SolutionDTO;
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

    public SolutionService(SolutionRepository solutionRepository, SolutionBookingRepository solutionBookingRepository) {
        this.solutionRepository = solutionRepository;
        this.solutionBookingRepository = solutionBookingRepository;
    }

    public List<SolutionDTO> getTop5Solutions(String city) {
        List<Solution> solutions = solutionRepository.findFirst5ByLocation(city);
        return mapToDTO(solutions);
    }

    public List<SolutionDTO> getSolutions() {
        List<Solution> solutions = solutionRepository.findAll();
        return mapToDTO(solutions);
    }

    public List<String> getAllLocations() {
        return solutionRepository.findAllLocations();
    }

    public List<String> getAllCategories() {
        return solutionRepository.findAllCategories();
    }

    public Page<SolutionDTO> getFilteredSolutions(String startDate, String endDate, String category,
                                                  String type, Double minPrice, Double maxPrice,
                                                  String location, int page, int size) {

        LocalDate start = (startDate != null && !startDate.isEmpty()) ? LocalDate.parse(startDate) : null;
        LocalDate end = (endDate != null && !endDate.isEmpty()) ? LocalDate.parse(endDate) : null;

        // Mapiranje tipa rešenja
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

        // Pronalazak zauzetih rešenja (samo ako su oba datuma prosleđena)
        List<Long> bookedSolutionIds = (start != null && end != null)
                ? solutionBookingRepository.findBookedSolutionIds(start, end)
                : null;

        // Filtriranje dostupnih rešenja
        Page<Solution> solutionPage = solutionRepository.findAvailableSolutions(
                category, mappedType, minPrice, maxPrice, location, bookedSolutionIds, PageRequest.of(page, size)
        );

        return solutionPage.map(this::mapToDTO);
    }


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
            //dto.setIsAvailable(solution.getIsAvailable());

            if (solution.getProvider() != null) {
                dto.setProviderCompanyName(solution.getProvider().getCompanyName());
                dto.setProviderId(solution.getProvider().getId());
            }

            return dto;
        }).collect(Collectors.toList());
    }


}
