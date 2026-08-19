package com.anubhab.warrantyhub.service;

import com.anubhab.warrantyhub.model.Company;
import com.anubhab.warrantyhub.model.Customer;
import com.anubhab.warrantyhub.repository.CompanyRepository;
import com.anubhab.warrantyhub.repository.CustomerRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final CustomerRepository customerRepository;
    private final CompanyRepository companyRepository;

    public CustomUserDetailsService(
            CustomerRepository customerRepository,
            CompanyRepository companyRepository) {
        this.customerRepository = customerRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
    
        Optional<Customer> customer =
                customerRepository.findByEmail(email);
    
        if (customer.isPresent()) {
    
            Customer c = customer.get();
    
            return User.builder()
                    .username(c.getEmail())
                    .password(c.getPassword())
                    .roles("CUSTOMER")
                    .build();
        }
    
        Optional<Company> company =
                companyRepository.findByEmail(email);
    
        if (company.isPresent()) {
    
            Company c = company.get();
    
            return User.builder()
                    .username(c.getEmail())
                    .password(c.getPassword())
                    .roles("COMPANY")
                    .build();
        }
    
        throw new UsernameNotFoundException(
                "User not found");
    }
}
