package com.anubhab.warrantyhub.repository;

import com.anubhab.warrantyhub.model.RequestStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestStatusHistoryRepository
        extends JpaRepository<RequestStatusHistory, Long> {
}