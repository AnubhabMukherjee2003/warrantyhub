package com.anubhab.warrantyhub.repository;

import com.anubhab.warrantyhub.model.Warranty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WarrantyRepository
        extends JpaRepository<Warranty, Long> {
}