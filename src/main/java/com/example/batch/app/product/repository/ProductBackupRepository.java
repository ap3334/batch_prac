package com.example.batch.app.product.repository;

import com.example.batch.app.product.entity.ProductBackup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductBackupRepository extends JpaRepository<ProductBackup, Long> {

    Optional<ProductBackup> findByProductId(Long id);

}

