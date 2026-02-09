package com.tableorder.service;

import com.tableorder.dto.request.*;
import com.tableorder.dto.response.*;
import com.tableorder.entity.*;
import com.tableorder.repository.*;
import javax.persistence.EntityNotFoundException;
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
        return menuRepository.findByStoreIdOrderByCategorySortOrderAscSortOrderAsc(storeId).stream().map(this::toResponse).collect(java.util.stream.Collectors.toList());
    }

    public List<MenuResponse> getMenusByCategory(Long storeId, Long categoryId) {
        return menuRepository.findByStoreIdAndCategoryIdOrderBySortOrder(storeId, categoryId).stream().map(this::toResponse).collect(java.util.stream.Collectors.toList());
    }

    public List<CategoryResponse> getCategories(Long storeId) {
        return categoryRepository.findByStoreIdOrderBySortOrder(storeId).stream()
                .map(c -> new CategoryResponse(c.getId(), c.getName(), c.getSortOrder())).collect(java.util.stream.Collectors.toList());
    }

    @Transactional
    public MenuResponse createMenu(Long storeId, MenuCreateRequest req) {
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new EntityNotFoundException("Store not found"));
        Category category = categoryRepository.findById(req.getCategoryId()).orElseThrow(() -> new EntityNotFoundException("Category not found"));
        int nextSort = menuRepository.findMaxSortOrderByStoreIdAndCategoryId(storeId, req.getCategoryId()) + 1;
        Menu menu = new Menu();
        menu.setStore(store);
        menu.setCategory(category);
        menu.setName(req.getName());
        menu.setPrice(req.getPrice());
        menu.setDescription(req.getDescription());
        menu.setSortOrder(nextSort);
        return toResponse(menuRepository.save(menu));
    }

    @Transactional
    public MenuResponse updateMenu(Long menuId, MenuUpdateRequest req) {
        Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new EntityNotFoundException("Menu not found"));
        if (req.getName() != null) menu.setName(req.getName());
        if (req.getPrice() != null) menu.setPrice(req.getPrice());
        if (req.getDescription() != null) menu.setDescription(req.getDescription());
        if (req.getCategoryId() != null) menu.setCategory(categoryRepository.findById(req.getCategoryId()).orElseThrow(() -> new EntityNotFoundException("Category not found")));
        return toResponse(menuRepository.save(menu));
    }

    public void deleteMenu(Long menuId) {
        if (!menuRepository.existsById(menuId)) throw new EntityNotFoundException("Menu not found");
        menuRepository.deleteById(menuId);
    }

    @Transactional
    public void updateMenuOrder(Long storeId, MenuOrderRequest req) {
        for (var item : req.getItems()) {
            Menu menu = menuRepository.findById(item.getMenuId()).orElseThrow(() -> new EntityNotFoundException("Menu not found"));
            menu.setSortOrder(item.getSortOrder());
            menuRepository.save(menu);
        }
    }

    private MenuResponse toResponse(Menu m) {
        return new MenuResponse(m.getId(), m.getCategory().getId(), m.getCategory().getName(), m.getName(), m.getPrice(), m.getDescription(), m.getSortOrder());
    }
}
