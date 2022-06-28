package org.hankyu.simpleboard_v1.factory.dto;

import org.hankyu.simpleboard_v1.dto.category.CategoryCreateRequest;

public class CategoryCreateRequestFactory {

    public static CategoryCreateRequest createCategoryCreateRequest() {
        return new CategoryCreateRequest("category", null);
    }

    public static CategoryCreateRequest createCategoryCreateRequestWithName(String name) {
        return new CategoryCreateRequest(name, null);
    }
}
