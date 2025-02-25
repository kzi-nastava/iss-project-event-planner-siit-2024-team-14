package edu.ftn.iss.eventplanner.mappers;

import edu.ftn.iss.eventplanner.dtos.CategoryDTO;
import edu.ftn.iss.eventplanner.dtos.CreateCategoryDTO;
import edu.ftn.iss.eventplanner.dtos.UpdateCategoryDTO;
import edu.ftn.iss.eventplanner.entities.SolutionCategory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CategoryDTOMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public CategoryDTOMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }




    public SolutionCategory fromDTO(CreateCategoryDTO dto) { return modelMapper.map(dto, SolutionCategory.class); }

    public SolutionCategory fromDTO(UpdateCategoryDTO dto) { return modelMapper.map(dto, SolutionCategory.class); }

    public SolutionCategory fromDTO(CategoryDTO dto) { return modelMapper.map(dto, SolutionCategory.class); }

    public CategoryDTO toCategoryDTO(SolutionCategory category) { return modelMapper.map(category, CategoryDTO.class); }
}
