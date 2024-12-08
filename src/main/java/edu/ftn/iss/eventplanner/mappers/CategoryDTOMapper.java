package edu.ftn.iss.eventplanner.mappers;

import edu.ftn.iss.eventplanner.dtos.CategoryDTO;
import edu.ftn.iss.eventplanner.dtos.CreateCategoryDTO;
import edu.ftn.iss.eventplanner.dtos.UpdateCategoryDTO;
import edu.ftn.iss.eventplanner.entities.Category;
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




    public Category fromDTO(CreateCategoryDTO dto) { return modelMapper.map(dto, Category.class); }

    public Category fromDTO(UpdateCategoryDTO dto) { return modelMapper.map(dto, Category.class); }

    public Category fromDTO(CategoryDTO dto) { return modelMapper.map(dto, Category.class); }

    public CategoryDTO toCategoryDTO(Category category) { return modelMapper.map(category, CategoryDTO.class); }
}
