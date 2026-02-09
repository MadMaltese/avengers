package com.tableorder.dto.request;
import lombok.*;
import java.util.List;
@Data @AllArgsConstructor @NoArgsConstructor
public class MenuOrderRequest {
    private List<MenuOrderItem> items;
    @Data @AllArgsConstructor @NoArgsConstructor
    public static class MenuOrderItem { private Long menuId; private Integer sortOrder; }
}
