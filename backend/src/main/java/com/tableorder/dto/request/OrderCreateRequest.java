package com.tableorder.dto.request;
import lombok.*;
import javax.validation.constraints.*;
import java.util.List;
@Data @AllArgsConstructor @NoArgsConstructor
public class OrderCreateRequest { @NotEmpty private List<OrderItemRequest> items; }
