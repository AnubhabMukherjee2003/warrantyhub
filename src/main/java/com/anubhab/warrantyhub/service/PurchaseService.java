package com.anubhab.warrantyhub.service;

import com.anubhab.warrantyhub.dto.PurchaseRequest;
import com.anubhab.warrantyhub.dto.PurchaseResponse;
import com.anubhab.warrantyhub.exception.CompanyNotFoundException;
import com.anubhab.warrantyhub.model.Company;
import com.anubhab.warrantyhub.model.Customer;
import com.anubhab.warrantyhub.model.Product;
import com.anubhab.warrantyhub.model.Purchase;
import com.anubhab.warrantyhub.model.Warranty;
import com.anubhab.warrantyhub.repository.CompanyRepository;
import com.anubhab.warrantyhub.repository.CustomerRepository;
import com.anubhab.warrantyhub.repository.ProductRepository;
import com.anubhab.warrantyhub.repository.PurchaseRepository;
import com.anubhab.warrantyhub.repository.WarrantyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PurchaseService {

    private final CompanyRepository companyRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final WarrantyRepository warrantyRepository;
    private final PurchaseRepository purchaseRepository;

    public PurchaseService(CompanyRepository companyRepository,
                           CustomerRepository customerRepository,
                           ProductRepository productRepository,
                           WarrantyRepository warrantyRepository,
                           PurchaseRepository purchaseRepository) {
        this.companyRepository = companyRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.warrantyRepository = warrantyRepository;
        this.purchaseRepository = purchaseRepository;
    }

    @Transactional
    public PurchaseResponse createPurchase(PurchaseRequest request) {
        Company company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() -> new CompanyNotFoundException(request.getCompanyId()));

        Customer customer = new Customer();
        customer.setName(request.getCustomerName());
        customer.setEmail(request.getCustomerEmail());
        customer.setPhone(request.getCustomerPhone());
        customer.setPassword(request.getCustomerPassword());
        Customer savedCustomer = customerRepository.save(customer);

        Product product = new Product();
        product.setCompany(company);
        product.setProductName(request.getProductName());
        product.setCategory(request.getProductCategory());
        product.setModelNumber(request.getModelNumber());
        Product savedProduct = productRepository.save(product);

        Warranty warranty = new Warranty();
        warranty.setWarrantyPeriod(request.getWarrantyPeriod());
        warranty.setWarrantyUnit(request.getWarrantyUnit());
        warranty.setTerms(request.getWarrantyTerms());
        Warranty savedWarranty = warrantyRepository.save(warranty);

        Purchase purchase = new Purchase();
        purchase.setCustomer(savedCustomer);
        purchase.setProduct(savedProduct);
        purchase.setWarranty(savedWarranty);
        purchase.setPurchaseDate(request.getPurchaseDate());
        purchase.setInvoiceNumber(request.getInvoiceNumber());

        Purchase savedPurchase = purchaseRepository.save(purchase);

        return new PurchaseResponse(
                savedPurchase.getPurchaseId(),
                savedCustomer.getCustomerId(),
                savedProduct.getProductId(),
                savedWarranty.getWarrantyId(),
                company.getCompanyId(),
                savedPurchase.getPurchaseDate(),
                savedPurchase.getInvoiceNumber()
        );
    }
}