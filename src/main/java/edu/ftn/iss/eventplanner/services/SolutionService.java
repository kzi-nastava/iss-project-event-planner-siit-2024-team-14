package edu.ftn.iss.eventplanner.services;

import edu.ftn.iss.eventplanner.dtos.SolutionDTO;
import edu.ftn.iss.eventplanner.entities.Solution;
import edu.ftn.iss.eventplanner.repositories.SolutionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SolutionService {
    private final SolutionRepository solutionRepository;

    public SolutionService(SolutionRepository solutionRepository) {
        this.solutionRepository = solutionRepository;
    }

    public List<SolutionDTO> getTop5Solutions(String city) {
        List<Solution> solutions = solutionRepository.findFirst5ByLocation(city);
        return mapToDTO(solutions);
    }

    public List<SolutionDTO> getSolutions(String city) {
        List<Solution> solutions = solutionRepository.findByLocation(city);
        return mapToDTO(solutions);
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
            //dto.setIsAvailable(solution.getIsAvailable());

            if (solution.getProvider() != null) {
                dto.setProviderCompanyName(solution.getProvider().getCompanyName());
            }

            return dto;
        }).collect(Collectors.toList());
    }
}
