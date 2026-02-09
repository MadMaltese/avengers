package com.tableorder.repository;
import com.tableorder.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByStoreAndUsername(Store store, String username);
}
