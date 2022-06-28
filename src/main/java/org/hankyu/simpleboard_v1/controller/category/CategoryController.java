package org.hankyu.simpleboard_v1.controller.category;

import lombok.RequiredArgsConstructor;
import org.hankyu.simpleboard_v1.dto.category.CategoryCreateRequest;
import org.hankyu.simpleboard_v1.service.category.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/categories")
    public void readAll() {
        categoryService.readAll();
    }

    @PostMapping("/categories")
    public void create(@Valid CategoryCreateRequest req) {
        categoryService.create(req);
    }

    @DeleteMapping("/categories")
    public void delete(@PathVariable Long id) {
        categoryService.delete(id);
    }
}
