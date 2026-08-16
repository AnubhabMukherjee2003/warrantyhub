package com.anubhab.warrantyhub.repository;

import com.anubhab.warrantyhub.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}