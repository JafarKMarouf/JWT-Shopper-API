package com.jafarmarouf.jwtshopper.services.category;

import com.jafarmarouf.jwtshopper.models.Category;

import java.util.List;

public interface ICategoryService {
    List<Category> getAllCategories();

    Category addCategory(Category category);

    Category updateCategory(Category category, Long id);

    void deleteCategoryById(Long id);

    Category getCategoryById(Long id);

    Category getCategoriesByName(String name);
}
