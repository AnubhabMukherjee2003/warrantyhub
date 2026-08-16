package com.anubhab.warrantyhub.repository;

import com.anubhab.warrantyhub.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository
        extends JpaRepository<Company, Long> {
}