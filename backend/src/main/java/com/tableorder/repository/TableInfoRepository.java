package com.tableorder.repository;
import com.tableorder.entity.TableInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface TableInfoRepository extends JpaRepository<TableInfo, Long> {
    Optional<TableInfo> findByStoreIdAndTableNo(Long storeId, Integer tableNo);
    List<TableInfo> findByStoreId(Long storeId);
}
