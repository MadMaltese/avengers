package com.tableorder.dto.request;
import lombok.*;
import javax.validation.constraints.*;
@Data @AllArgsConstructor @NoArgsConstructor
public class MenuCreateRequest { @NotBlank private String name; @Min(0) @NotNull private Integer price; private String description; @NotNull private Long categoryId; }
