package edu.ftn.iss.eventplanner.controllers;

import edu.ftn.iss.eventplanner.dtos.eventType.CategoryNamesDTO;
import edu.ftn.iss.eventplanner.dtos.serviceDetails.CategoryDTO;
import edu.ftn.iss.eventplanner.dtos.CreateCategoryDTO;
import edu.ftn.iss.eventplanner.dtos.UpdateCategoryDTO;
import edu.ftn.iss.eventplanner.entities.SolutionCategory;
import edu.ftn.iss.eventplanner.mappers.CategoryDTOMapper;
import edu.ftn.iss.eventplanner.services.CategoryService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@RestController
@RequestMapping(path = {"/api/categories"}, produces = MediaType.APPLICATION_JSON_VALUE)
public class CategoryController {

    private CategoryService categoryService;
    private final CategoryDTOMapper modelMapper;


    @Autowired
    public CategoryController(CategoryService categoryService, CategoryDTOMapper modelMapper) {
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
    }

    // GET */api/categories
    @GetMapping
    public ResponseEntity<Collection<CategoryDTO>> getAllCategories() {
        Collection<CategoryDTO> categories = categoryService.getAllCategories()
                .stream().map(modelMapper::toCategoryDTO).toList();

        return ResponseEntity.ok(categories);
    }

    @GetMapping("/get-all-et")
    public ResponseEntity<List<CategoryNamesDTO>> getAllForET() {
        List<CategoryNamesDTO> categories = categoryService.getAllForET();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/get-by-event-type")
    public ResponseEntity<List<CategoryNamesDTO>> getByEventType(@RequestParam String eventType) {
        List<CategoryNamesDTO> categories = categoryService.getByEventType(eventType);
        return ResponseEntity.ok(categories);
    }

    // GET */api/categories/1
    @GetMapping(path = "/{id:\\d+}")
    public ResponseEntity<CategoryDTO> getCategoryById(
            @PathVariable(name = "id") int id
    ) {
        SolutionCategory category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(modelMapper.toCategoryDTO(category));
    }


    // Not sure about this :)
    // @GetMapping(path = "/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    // public ResponseEntity<CategoryDTO> getCategoryByName(
    //         @PathVariable(name = "name") String name
    // ) {
    //     return categoryService.findCategoryByName(name)  // TODO: Add handling for custom exceptions and make this (and by id) method throw ex. CategoryNotFoundException
    //             .map(category -> ResponseEntity.ok(toCategoryDTO(category)))
    //             .orElseGet(ResponseEntity.notFound()::build);

    // }



    // POST admin@*/api/categories
    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(
            @RequestBody @Valid CreateCategoryDTO categoryData,
            UriComponentsBuilder uriBuilder
    ) {
        SolutionCategory createdCategory = categoryService.insertCategory(modelMapper.fromDTO(categoryData));

        URI location = uriBuilder
                .path("/api/categories/{id}")
                .buildAndExpand(createdCategory.getId())
                .toUri();

        return ResponseEntity.created(location).body(modelMapper.toCategoryDTO(createdCategory));
    }



    // PUT admin@*/api/categories/1
    @PutMapping(path = {"/{id:\\d+}"})
    public ResponseEntity<CategoryDTO> putUpdateCategory(
            @PathVariable int id,
            @RequestBody @Valid UpdateCategoryDTO categoryData
    ) {
        categoryData.setId(id);
        SolutionCategory updatedCategory = categoryService.updateCategory(modelMapper.fromDTO(categoryData));
        return ResponseEntity.ok(modelMapper.toCategoryDTO(updatedCategory));
    }



    // DELETE admin@*/api/categories/1
    @DeleteMapping(path = {"/{id:\\d+}"})
    public ResponseEntity<Void> deleteCategory(
            @PathVariable(name = "id") int id
    ) {
        categoryService.deleteCategoryById(id);
        return ResponseEntity.noContent().build();
    }



    // DELETE admin@*/api/categories
    @DeleteMapping
    public ResponseEntity<Void> deleteAllCategories() {
        categoryService.deleteAllCategories();
        return ResponseEntity.noContent().build();
    }

}
