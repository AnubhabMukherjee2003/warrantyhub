package com.anubhab.warrantyhub.service;

import com.anubhab.warrantyhub.dto.CustomerRequest;
import com.anubhab.warrantyhub.dto.CustomerResponse;
import com.anubhab.warrantyhub.dto.ProductResponse;
import com.anubhab.warrantyhub.exception.CustomerNotFoundException;
import com.anubhab.warrantyhub.model.Customer;
import com.anubhab.warrantyhub.model.Product;
import com.anubhab.warrantyhub.repository.CustomerRepository;
import com.anubhab.warrantyhub.repository.PurchaseRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final PurchaseRepository purchaseRepository;

    public CustomerService(CustomerRepository customerRepository, PasswordEncoder passwordEncoder, PurchaseRepository purchaseRepository) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.purchaseRepository = purchaseRepository;
    }

    @Transactional(readOnly = true)
    public CustomerResponse getCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));
        return toResponse(customer);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getCustomerProducts(String email) {
        return purchaseRepository.findByCustomer_Email(email)
                .stream()
                .map(purchase -> {
                    Product p = purchase.getProduct();
                    return new ProductResponse(
                            purchase.getPurchaseId(),
                            p.getProductId(),
                            p.getProductName(),
                            p.getCategory(),
                            p.getModelNumber(),
                            p.getCompany().getCompanyId(),
                            p.getCompany().getCompanyName()
                    );
                })
                .toList();
    }

    @Transactional
    public CustomerResponse createCustomer(CustomerRequest request) {
        Customer customer = new Customer();
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setPassword(passwordEncoder.encode(request.getPassword()));

        return toResponse(customerRepository.save(customer));
    }

    @Transactional
    public CustomerResponse updateCustomer(Long id, CustomerRequest request) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));

        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setPassword(passwordEncoder.encode(request.getPassword()));

        return toResponse(customerRepository.save(customer));
    }

    @Transactional
    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new CustomerNotFoundException(id);
        }
        customerRepository.deleteById(id);
    }

    private CustomerResponse toResponse(Customer customer) {
        return new CustomerResponse(
                customer.getCustomerId(),
                customer.getName(),
                customer.getEmail(),
                customer.getPhone()
        );
    }
}
