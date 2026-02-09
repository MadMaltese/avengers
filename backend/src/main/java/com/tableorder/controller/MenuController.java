package com.tableorder.controller;

import com.tableorder.dto.request.*;
import com.tableorder.dto.response.*;
import com.tableorder.service.MenuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequiredArgsConstructor
public class MenuController {
    private final MenuService menuService;

    @GetMapping("/api/stores/{storeId}/menus")
    public List<MenuResponse> getMenus(@PathVariable Long storeId, @RequestParam(required = false) Long category) {
        return category != null ? menuService.getMenusByCategory(storeId, category) : menuService.getMenusByStore(storeId);
    }

    @GetMapping("/api/stores/{storeId}/categories")
    public List<CategoryResponse> getCategories(@PathVariable Long storeId) {
        return menuService.getCategories(storeId);
    }

    @PostMapping("/api/stores/{storeId}/menus")
    public ResponseEntity<MenuResponse> createMenu(@PathVariable Long storeId, @Valid @RequestBody MenuCreateRequest req) {
        return ResponseEntity.status(201).body(menuService.createMenu(storeId, req));
    }

    @PutMapping("/api/menus/{menuId}")
    public MenuResponse updateMenu(@PathVariable Long menuId, @RequestBody MenuUpdateRequest req) {
        return menuService.updateMenu(menuId, req);
    }

    @DeleteMapping("/api/menus/{menuId}")
    public ResponseEntity<Void> deleteMenu(@PathVariable Long menuId) {
        menuService.deleteMenu(menuId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/api/stores/{storeId}/menus/order")
    public ResponseEntity<Void> updateMenuOrder(@PathVariable Long storeId, @RequestBody MenuOrderRequest req) {
        menuService.updateMenuOrder(storeId, req);
        return ResponseEntity.ok().build();
    }
}
