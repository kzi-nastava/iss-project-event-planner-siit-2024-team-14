package edu.ftn.iss.eventplanner.services;

import edu.ftn.iss.eventplanner.entities.SolutionCategory;
import edu.ftn.iss.eventplanner.repositories.CategoryRepository;
import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
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
        return categories.findById(id)
                // TODO: Make custom exceptions
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
    }


    public Optional<SolutionCategory> findCategoryById(int id) {
        return categories.findById(id);
    }


    public Optional<SolutionCategory> findCategoryByName(String name) { return categories.findByName(name); }



    public Collection<SolutionCategory> getAllCategories() {
        return categories.findAll();
    }



    public SolutionCategory insertCategory(SolutionCategory category) {
        category.setId(null);
        return categories.save(category);
    }



    public SolutionCategory updateCategory(SolutionCategory category) {
        SolutionCategory oldCategoryState = getCategoryById(category.getId());
        boolean hasNameChanged = !oldCategoryState.getName().equals(category.getName());
        if (hasNameChanged) onCategoryNameChanged(category);
        category = categories.save(category);
        return category;
    }



    public void deleteCategoryById(int id) {
        SolutionCategory category = getCategoryById(id);
        // TODO: Prevent deletion if services/products that are categorized into this category exist
        categories.deleteById(id);
    }



    public void deleteAllCategories() {
        // TODO: Prevent deletion ...
        categories.deleteAll();
    }



    protected void onCategoryNameChanged(SolutionCategory category) {
        // TODO
        eventPublisher.publishEvent(/* new CategoryNameChanged(*/category/*) */);
    }
}
