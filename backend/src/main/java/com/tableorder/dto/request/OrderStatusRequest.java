package com.tableorder.dto.request;
import com.tableorder.entity.OrderStatus;
import lombok.*;
import javax.validation.constraints.*;
@Data @AllArgsConstructor @NoArgsConstructor
public class OrderStatusRequest { @NotNull private OrderStatus status; }
