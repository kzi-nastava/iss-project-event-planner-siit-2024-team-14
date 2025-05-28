package edu.ftn.iss.eventplanner.services;

import edu.ftn.iss.eventplanner.dtos.eventType.CategoryNamesDTO;
import edu.ftn.iss.eventplanner.entities.SolutionCategory;
import edu.ftn.iss.eventplanner.entities.EventType;
import edu.ftn.iss.eventplanner.entities.CategoryNameChangedEvent;
import edu.ftn.iss.eventplanner.exceptions.BadRequestException;
import edu.ftn.iss.eventplanner.exceptions.InternalServerError;
import edu.ftn.iss.eventplanner.exceptions.NotFoundException;
import edu.ftn.iss.eventplanner.repositories.CategoryRepository;
import edu.ftn.iss.eventplanner.repositories.EventTypeRepository;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Setter
@Getter
@Service("CategoryCRUDServiceImpl")
public class CategoryService {

    private CategoryRepository categoryRepository;
    private ApplicationEventPublisher eventPublisher;


    @Autowired
    public CategoryService(CategoryRepository categoryRepository, ApplicationEventPublisher eventPublisher) {
        this.categoryRepository = categoryRepository;
        this.eventPublisher = eventPublisher;
    }

    public SolutionCategory getCategoryById(int id) {
        Optional<SolutionCategory> category;
        try {
            category = categoryRepository.findById(id);
        } catch (Exception ex) {
            throw new InternalServerError("An unexpected error has occurred. :(");
        }

        return category.orElseThrow(() -> new NotFoundException("Category not found."));
    }

    public Optional<SolutionCategory> findCategoryById(int id) {
        try {
            return categoryRepository.findById(id);
        } catch  (Exception ex) {
            throw new InternalServerError("An unexpected error has occurred. :(");
        }
    }

    public Optional<SolutionCategory> findCategoryByName(String name) {
        try {
            return categoryRepository.findByName(name);
        } catch (Exception ex) {
            throw new InternalServerError("An unexpected error has occurred. :(");
        }
    }

    public Collection<SolutionCategory> getAllCategories() {
        try {
            return categoryRepository.findAll();
        } catch (Exception ex) {
            throw new InternalServerError("An unexpected error has occurred. :(");
        }
    }

    public List<CategoryNamesDTO> getAllForET() {
        List<SolutionCategory> categories = categoryRepository.findAll();
        return categories.stream()
                .map(category -> new CategoryNamesDTO(
                        category.getName()
                ))
                .collect(Collectors.toList());
    }

    public List<CategoryNamesDTO> getByEventType(String eventTypeName) {
        EventType eventType = categoryRepository.findByNameWithCategories(eventTypeName);
        if (eventType == null) {
            throw new NotFoundException("EventType not found.");
        }
        return eventType.getSolutionCategories().stream()
                .map(category -> new CategoryNamesDTO(category.getName()))
                .collect(Collectors.toList());
    }

    public SolutionCategory insertCategory(@NotNull SolutionCategory categoryRequest) {
        SolutionCategory category = SolutionCategory.builder()
                .name(categoryRequest.getName())
                .description(categoryRequest.getDescription())
                .build();

        try {
            return categoryRepository.save(category);
        } catch (DataIntegrityViolationException | ConstraintViolationException | IllegalArgumentException ex) {
            throw new BadRequestException();
        } catch (Exception ex) {
            throw new InternalServerError("An unexpected error has occurred. :(");
        }
    }


    @Transactional
    public SolutionCategory updateCategory(@NotNull SolutionCategory categoryRequest) {
        try {
            SolutionCategory category = getCategoryById(categoryRequest.getId());
            final String oldName = category.getName();

            Optional.ofNullable(categoryRequest.getName())
                            .ifPresent(category::setName);
            Optional.ofNullable(categoryRequest.getDescription())
                            .ifPresent(category::setDescription);

            boolean hasNameChanged = !Objects.equals(oldName, category.getName());
            if (hasNameChanged) {
                eventPublisher.publishEvent(
                        new CategoryNameChangedEvent(category.getId(), oldName, category.getName())
                );
            }

            return category;
        } catch (NotFoundException ex) {
            throw new NotFoundException("Update failed, category does not exist.");
        } catch (Exception ex) {
            throw new InternalServerError("An unexpected error has occurred. :(");
        }
    }



    public void deleteCategoryById(int id) {
        if (!categoryRepository.existsById(id)) {
            throw new NotFoundException("Category not found!");
        }

        try {
            // TODO: Prevent deletion if services/products that are categorized into this category exist
            categoryRepository.deleteById(id);
        } catch (Exception ex) {
            throw new InternalServerError("An unexpected error has occurred. :(");
        }
    }



    public void deleteAllCategories() {
        try {
            // TODO: Prevent deletion ...
            categoryRepository.deleteAll();
        } catch (Exception ex) {
            throw new InternalServerError("An unexpected error has occurred. :(");
        }
    }
}
