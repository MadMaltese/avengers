package com.tableorder.dto.request;
import lombok.*;
import javax.validation.constraints.*;
@Data @AllArgsConstructor @NoArgsConstructor
public class OrderItemRequest { @NotNull private Long menuId; @Min(1) @NotNull private Integer quantity; }
