package com.anubhab.warrantyhub.service;

import com.anubhab.warrantyhub.dto.CompanyRequest;
import com.anubhab.warrantyhub.dto.CompanyResponse;
import com.anubhab.warrantyhub.model.Company;
import com.anubhab.warrantyhub.repository.CompanyRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    public CompanyService(CompanyRepository companyRepository, PasswordEncoder passwordEncoder) {
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public CompanyResponse createCompany(CompanyRequest request) {
        Company company = new Company();
        company.setCompanyName(request.getCompanyName());
        company.setEmail(request.getEmail());
        company.setPassword(passwordEncoder.encode(request.getPassword()));
        company.setPhone(request.getPhone());
        company.setStatus(request.getStatus());

        Company savedCompany = companyRepository.save(company);
        return new CompanyResponse(
                savedCompany.getCompanyId(),
                savedCompany.getCompanyName(),
                savedCompany.getEmail(),
                savedCompany.getPhone(),
                savedCompany.getStatus()
        );
    }
}
