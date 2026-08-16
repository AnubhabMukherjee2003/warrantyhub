# WarrantyHub — Development Plan

## 1. Completed

### Phase 0 — Setup
- Spring Boot Maven project
- Java 17
- Spring Boot 4.1.0
- Spring Web MVC
- Spring Data JPA
- H2
- Validation
- DevTools
- File-based H2 configuration

### Phase 1 — REST API
Learned:
- `@RestController`
- `@RequestMapping`
- `@GetMapping`
- `@PostMapping`
- `@PutMapping`
- `@DeleteMapping`
- `@RequestBody`
- `@PathVariable`

Completed:
- `HealthController`

### Phase 2 — Layers
Learned and demonstrated:
- Controller → Service → Repository
- Constructor dependency injection

Completed:
- `CustomerController`
- `CustomerService`

### Phase 3 — JPA / H2 / CRUD
Learned:
- `@Entity`
- `@Id`
- `@GeneratedValue`
- `JpaRepository`
- `save()`
- `findById()`
- `findAll()`
- `existsById()`
- `deleteById()`
- File-based H2
- `ddl-auto=update`

Completed repositories:
- `CustomerRepository`
- `CompanyRepository`
- `ProductRepository`
- `WarrantyRepository`
- `PurchaseRepository`
- `ServiceRequestRepository`
- `RequestStatusHistoryRepository`

### Phase 4 — DTO / Validation / Exceptions
Completed:
- `CustomerRequest`
- `CustomerResponse`
- `CustomerNotFoundException`
- `GlobalExceptionHandler`
- `@Valid`
- `@NotBlank`
- `@Email`
- 404 handling

### Phase 5 — WarrantyHub Data Model
Completed entities:
- `Company`
- `Customer`
- `Product`
- `Warranty`
- `Purchase`
- `ServiceRequest`
- `RequestStatusHistory`

Relationships:
- Product → Company (`@ManyToOne`)
- Purchase → Customer (`@ManyToOne`)
- Purchase → Product (`@ManyToOne`)
- Purchase → Warranty (`@ManyToOne`)
- ServiceRequest → Purchase (`@ManyToOne`)
- RequestStatusHistory → ServiceRequest (`@ManyToOne`)

---

# 2. Current Phase — Phase 6: Business Logic

No major new Spring concepts are planned here. Implement the business flow using the layers already learned.

## Checkpoint 1 — Company + Purchase Data Entry

### API
`POST /api/companies`
- Create company/executive entry.

### Files

`controller/CompanyController.java`
- `createCompany(CompanyRequest request)` — receive company data and delegate to service.

`service/CompanyService.java`
- `createCompany(CompanyRequest request)` — create Company, save it, return response.

`repository/CompanyRepository.java`
- Already done.

`dto/CompanyRequest.java`
- Fields required to create Company.

`dto/CompanyResponse.java`
- Safe Company response fields; do not expose password.

---

### API
`POST /api/purchases`

One API creates:
- Customer
- Product
- Warranty
- Purchase

### Files

`controller/PurchaseController.java`
- `createPurchase(PurchaseRequest request)` — receive the combined purchase-entry request and delegate to service.

`service/PurchaseService.java`
- `createPurchase(PurchaseRequest request)` — create Customer, Product, Warranty and Purchase, link them, and return response.
- Mark the method `@Transactional`.

`repository/PurchaseRepository.java`
- Already done.

`repository/CustomerRepository.java`
- Already done.

`repository/ProductRepository.java`
- Already done.

`repository/WarrantyRepository.java`
- Already done.

`dto/PurchaseRequest.java`
- Contains the information required to create Customer, Product, Warranty and Purchase.

`dto/PurchaseResponse.java`
- Returns the created purchase and relevant linked IDs/data.

### Transaction requirement

```text
Purchase API
    ↓
Create Customer
    ↓
Create Product
    ↓
Create Warranty
    ↓
Create Purchase
```

All four must succeed or the transaction rolls back.

---

# 3. Checkpoint 2 — Customer Service Request

## API
`POST /api/service-requests`

### Files

`controller/ServiceRequestController.java`
- `createServiceRequest(ServiceRequestCreateRequest request)` — receive request and delegate to service.

`service/ServiceRequestService.java`
- `createServiceRequest(ServiceRequestCreateRequest request)` — find Purchase, create ServiceRequest, set initial status, create initial history record.
- `getServiceRequest(Long id)` — retrieve one service request.

`repository/ServiceRequestRepository.java`
- Already done.

`repository/PurchaseRepository.java`
- Already done.

`repository/RequestStatusHistoryRepository.java`
- Already done.

`dto/ServiceRequestCreateRequest.java`
- Already created; contains `purchaseId`, issue category, issue description and priority.

`dto/ServiceRequestResponse.java`
- Return safe service-request information.

### Business flow

```text
Purchase ID
    ↓
Find Purchase
    ↓
Create ServiceRequest
    ↓
Set initial status
    ↓
Create RequestStatusHistory
```

This operation should be transactional.

---

# 4. Checkpoint 3 — Service Request Status

## API
`PUT /api/service-requests/{id}/status`

### Files

`controller/ServiceRequestController.java`
- `changeStatus(Long id, StatusChangeRequest request)` — receive new status/remarks and delegate to service.

`service/ServiceRequestService.java`
- `changeStatus(Long id, StatusChangeRequest request)` — update `currentStatus`, update `updatedAt`, create new status-history record.
- Mark the method `@Transactional`.

`dto/StatusChangeRequest.java`
- `status`
- `remarks`

`dto/StatusHistoryResponse.java`
- Return history information.

## History API

`GET /api/service-requests/{id}/history`

Controller method:
- `getStatusHistory(Long id)` — return status history.

Service method:
- `getStatusHistory(Long id)` — verify request exists, retrieve related history and convert to response DTOs.

---

# 5. Phase 6 Folder/File Target

```text
src/main/java/com/anubhab/warrantyhub/

├── controller/
│   ├── HealthController.java                 DONE
│   ├── CustomerController.java               DONE
│   ├── CompanyController.java                PHASE 6
│   ├── PurchaseController.java               PHASE 6
│   └── ServiceRequestController.java         PHASE 6
│
├── service/
│   ├── CustomerService.java                  DONE
│   ├── CompanyService.java                   PHASE 6
│   ├── PurchaseService.java                 PHASE 6
│   └── ServiceRequestService.java            PHASE 6
│
├── repository/
│   ├── CustomerRepository.java               DONE
│   ├── CompanyRepository.java                DONE
│   ├── ProductRepository.java                DONE
│   ├── WarrantyRepository.java              DONE
│   ├── PurchaseRepository.java              DONE
│   ├── ServiceRequestRepository.java        DONE
│   └── RequestStatusHistoryRepository.java DONE
│
├── model/
│   ├── Customer.java                          DONE
│   ├── Company.java                           DONE
│   ├── Product.java                           DONE
│   ├── Warranty.java                          DONE
│   ├── Purchase.java                          DONE
│   ├── ServiceRequest.java                    DONE
│   └── RequestStatusHistory.java             DONE
│
├── dto/
│   ├── CustomerRequest.java                   DONE
│   ├── CustomerResponse.java                  DONE
│   ├── CompanyRequest.java                    PHASE 6
│   ├── CompanyResponse.java                   PHASE 6
│   ├── PurchaseRequest.java                   PHASE 6
│   ├── PurchaseResponse.java                  PHASE 6
│   ├── ServiceRequestCreateRequest.java       DONE / refine
│   ├── ServiceRequestResponse.java            PHASE 6
│   ├── StatusChangeRequest.java               PHASE 6
│   └── StatusHistoryResponse.java             PHASE 6
│
└── exception/
    ├── CustomerNotFoundException.java        DONE
    └── GlobalExceptionHandler.java            DONE / extend
```

---

# 6. Phase 6 API Checklist

```text
POST /api/companies
POST /api/purchases

POST /api/service-requests
GET  /api/service-requests/{id}

PUT  /api/service-requests/{id}/status
GET  /api/service-requests/{id}/history
```

## Phase 6 completion condition

```text
Company
  ↓
Purchase API
  ↓
Customer + Product + Warranty + Purchase
  ↓
Customer creates Service Request
  ↓
OPEN + Status History
  ↓
Company changes status
  ↓
New Status History
  ↓
Customer can view current status/history
```

When this complete flow passes in Postman, Phase 6 is complete.

---

# 7. Next Phase

## Phase 7 — Security

Only after Phase 6 is working:

- Customer authentication
- Company Executive authentication
- Password hashing
- Roles
- Authorization
- JWT, if required by the final implementation
