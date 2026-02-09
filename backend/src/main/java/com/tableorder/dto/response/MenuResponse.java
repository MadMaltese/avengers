package com.tableorder.dto.response;
public record MenuResponse(Long id, Long categoryId, String categoryName, String name, Integer price, String description, int sortOrder) {}
