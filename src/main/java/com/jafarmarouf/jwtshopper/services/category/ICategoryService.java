package com.jafarmarouf.jwtshopper.services.category;

import com.jafarmarouf.jwtshopper.models.Category;

import java.util.List;

public interface ICategoryService {
    Category addCategory(Category category);

    Category updateCategory(Category category, Long id);

    void deleteCategoryById(Long id);

    Category getCategoryById(Long id);

    List<Category> getAllCategories();

    Category getCategoriesByName(String name);
}
