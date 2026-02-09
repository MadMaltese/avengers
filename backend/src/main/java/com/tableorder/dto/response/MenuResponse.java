package com.tableorder.dto.response;
import lombok.*;
@Data @AllArgsConstructor @NoArgsConstructor
public class MenuResponse { private Long id; private Long categoryId; private String categoryName; private String name; private Integer price; private String description; private int sortOrder; }
