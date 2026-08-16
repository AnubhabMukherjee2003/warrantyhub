package com.anubhab.warrantyhub.repository;

import com.anubhab.warrantyhub.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository
        extends JpaRepository<Product, Long> {
}