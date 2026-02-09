package com.tableorder.dto.request;
import lombok.*;
@Data @AllArgsConstructor @NoArgsConstructor
public class MenuUpdateRequest { private String name; private Integer price; private String description; private Long categoryId; }
