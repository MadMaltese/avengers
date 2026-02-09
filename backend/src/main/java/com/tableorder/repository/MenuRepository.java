package com.tableorder.repository;
import com.tableorder.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.*;
public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findByStoreIdOrderByCategorySortOrderAscSortOrderAsc(Long storeId);
    List<Menu> findByStoreIdAndCategoryIdOrderBySortOrder(Long storeId, Long categoryId);
    @Query("SELECT COALESCE(MAX(m.sortOrder), 0) FROM Menu m WHERE m.store.id = :storeId AND m.category.id = :categoryId")
    int findMaxSortOrderByStoreIdAndCategoryId(Long storeId, Long categoryId);
}
