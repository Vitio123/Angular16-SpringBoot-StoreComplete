package com.codeWithProjects.ecom.services.admin.category;

import com.codeWithProjects.ecom.dto.CategoryDto;
import com.codeWithProjects.ecom.entity.Category;

public interface CategoryService {
    Category createcategory(CategoryDto categoryDto);

}
