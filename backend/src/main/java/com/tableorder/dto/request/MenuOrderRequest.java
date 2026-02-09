package com.tableorder.dto.request;
import java.util.List;
public record MenuOrderRequest(List<MenuOrderItem> items) {
    public record MenuOrderItem(Long menuId, Integer sortOrder) {}
}
