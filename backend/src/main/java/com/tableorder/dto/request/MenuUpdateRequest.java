package com.tableorder.dto.request;
public record MenuUpdateRequest(String name, Integer price, String description, Long categoryId) {}
