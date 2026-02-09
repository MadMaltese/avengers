package com.tableorder.service;

import com.tableorder.dto.request.*;
import com.tableorder.dto.response.*;
import com.tableorder.entity.*;
import com.tableorder.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service @RequiredArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;
    private final CategoryRepository categoryRepository;
    private final StoreRepository storeRepository;

    public List<MenuResponse> getMenusByStore(Long storeId) {
        return menuRepository.findByStoreIdOrderByCategorySortOrderAscSortOrderAsc(storeId).stream().map(this::toResponse).toList();
    }

    public List<MenuResponse> getMenusByCategory(Long storeId, Long categoryId) {
        return menuRepository.findByStoreIdAndCategoryIdOrderBySortOrder(storeId, categoryId).stream().map(this::toResponse).toList();
    }

    public List<CategoryResponse> getCategories(Long storeId) {
        return categoryRepository.findByStoreIdOrderBySortOrder(storeId).stream()
                .map(c -> new CategoryResponse(c.getId(), c.getName(), c.getSortOrder())).toList();
    }

    @Transactional
    public MenuResponse createMenu(Long storeId, MenuCreateRequest req) {
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new EntityNotFoundException("Store not found"));
        Category category = categoryRepository.findById(req.categoryId()).orElseThrow(() -> new EntityNotFoundException("Category not found"));
        int nextSort = menuRepository.findMaxSortOrderByStoreIdAndCategoryId(storeId, req.categoryId()) + 1;
        Menu menu = new Menu();
        menu.setStore(store);
        menu.setCategory(category);
        menu.setName(req.name());
        menu.setPrice(req.price());
        menu.setDescription(req.description());
        menu.setSortOrder(nextSort);
        return toResponse(menuRepository.save(menu));
    }

    @Transactional
    public MenuResponse updateMenu(Long menuId, MenuUpdateRequest req) {
        Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new EntityNotFoundException("Menu not found"));
        if (req.name() != null) menu.setName(req.name());
        if (req.price() != null) menu.setPrice(req.price());
        if (req.description() != null) menu.setDescription(req.description());
        if (req.categoryId() != null) menu.setCategory(categoryRepository.findById(req.categoryId()).orElseThrow(() -> new EntityNotFoundException("Category not found")));
        return toResponse(menuRepository.save(menu));
    }

    public void deleteMenu(Long menuId) {
        if (!menuRepository.existsById(menuId)) throw new EntityNotFoundException("Menu not found");
        menuRepository.deleteById(menuId);
    }

    @Transactional
    public void updateMenuOrder(Long storeId, MenuOrderRequest req) {
        for (var item : req.items()) {
            Menu menu = menuRepository.findById(item.menuId()).orElseThrow(() -> new EntityNotFoundException("Menu not found"));
            menu.setSortOrder(item.sortOrder());
            menuRepository.save(menu);
        }
    }

    private MenuResponse toResponse(Menu m) {
        return new MenuResponse(m.getId(), m.getCategory().getId(), m.getCategory().getName(), m.getName(), m.getPrice(), m.getDescription(), m.getSortOrder());
    }
}
