package edu.ftn.iss.eventplanner.services;

import edu.ftn.iss.eventplanner.entities.SolutionCategory;
import edu.ftn.iss.eventplanner.events.CategoryNameChangedEvent;
import edu.ftn.iss.eventplanner.exceptions.BadRequestException;
import edu.ftn.iss.eventplanner.exceptions.InternalServerError;
import edu.ftn.iss.eventplanner.exceptions.NotFoundException;
import edu.ftn.iss.eventplanner.repositories.CategoryRepository;
import jakarta.validation.ConstraintViolationException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

@Setter
@Getter
@Service("CategoryCRUDServiceImpl")
public class CategoryService {

    private CategoryRepository categories;
    private ApplicationEventPublisher eventPublisher;


    @Autowired
    public CategoryService(CategoryRepository categoryRepository, ApplicationEventPublisher eventPublisher) {
        this.categories = categoryRepository;
        this.eventPublisher = eventPublisher;
    }



    public SolutionCategory getCategoryById(int id) {
        Optional<SolutionCategory> category;
        try {
            category = categories.findById(id);
        } catch (Exception ex) {
            throw new InternalServerError("An unexpected error has occurred. :(");
        }

        return category.orElseThrow(() -> new NotFoundException("Category not found!"));
    }


    public Optional<SolutionCategory> findCategoryById(int id) {
        try {
            return categories.findById(id);
        } catch  (Exception ex) {
            throw new InternalServerError("An unexpected error has occurred. :(");
        }
    }


    public Optional<SolutionCategory> findCategoryByName(String name) {
        try {
            return categories.findByName(name);
        } catch (Exception ex) {
            throw new InternalServerError("An unexpected error has occurred. :(");
        }
    }



    public Collection<SolutionCategory> getAllCategories() {
        try {
            return categories.findAll();
        } catch (Exception ex) {
            throw new InternalServerError("An unexpected error has occurred. :(");
        }
    }



    public SolutionCategory insertCategory(SolutionCategory categoryRequest) {
        categoryRequest.setId(null); // probably should create a new category

        try {
            return categories.save(categoryRequest);
        } catch (DataIntegrityViolationException | ConstraintViolationException | IllegalArgumentException ex) {
            throw new BadRequestException();
        } catch (Exception ex) {
            throw new InternalServerError("An unexpected error has occurred. :(");
        }
    }


    @Transactional
    public SolutionCategory updateCategory(SolutionCategory categoryRequest) {
        SolutionCategory category = getCategoryById(categoryRequest.getId());
        String oldName = category.getName();

        SolutionCategory updatedCategory;
        try {
            updatedCategory = categories.save(categoryRequest);
        } catch (Exception ex) {
            throw new InternalServerError("An unexpected error has occurred. :(");
        }

        // TODO: Maybe add domain events instead
        boolean hasNameChanged = !oldName.equals(updatedCategory.getName());
        if (hasNameChanged)
            eventPublisher.publishEvent(new CategoryNameChangedEvent(category.getId(), oldName, updatedCategory.getName()));

        return updatedCategory;
    }



    public void deleteCategoryById(int id) {
        try {
            SolutionCategory category = getCategoryById(id);
            // TODO: Prevent deletion if services/products that are categorized into this category exist
            categories.deleteById(id);
        } catch (Exception ex) {
            throw new InternalServerError("An unexpected error has occurred. :(");
        }
    }



    public void deleteAllCategories() {
        try {
            // TODO: Prevent deletion ...
            categories.deleteAll();
        } catch (Exception ex) {
            throw new InternalServerError("An unexpected error has occurred. :(");
        }
    }
}
