# WarrantyHub --- Architecture & Stack

## Architecture Style

WarrantyHub uses a simple **microservices architecture** with three
Spring Boot services.

``` text
                    Angular
                       |
                  REST / HTTP
                       |
        +--------------+--------------+
        |              |              |
        ↓              ↓              ↓
 Company &        Product,        Service Request
 Customer         Warranty &      Service
 Service          Purchase Service   Service
        |              |              |
        +--------------+--------------+
                       |
                 Shared Database
                       |
                       H2
```

For the current project, the services use **one shared H2 database**.
The database contains the tables used by the company executive and
customer.

## Database Tables

### 1. Company

  Field          Key
  -------------- -----
  company_id     PK
  company_name   
  email          
  password       
  phone          
  status         
  created_at     

### 2. Customer

  Field         Key
  ------------- -----
  customer_id   PK
  name          
  email         
  phone         
  password      
  created_at    

A customer is not linked directly to a company because the same customer
can buy products from different companies.

### 3. Product

  Field          Key
  -------------- --------------
  product_id     PK
  company_id     FK → Company
  product_name   
  category       
  model_number   

### 4. Warranty

  Field             Key
  ----------------- -----
  warranty_id       PK
  warranty_period   
  warranty_unit     
  terms             

A warranty can represent different warranty periods such as 1 year, 2
years, or 3 years.

### 5. Purchase

  Field            Key
  ---------------- ---------------
  purchase_id      PK
  customer_id      FK → Customer
  product_id       FK → Product
  warranty_id      FK → Warranty
  purchase_date    
  invoice_number   

Purchase connects the customer, product, and warranty.

### 6. Service_Request

  Field               Key
  ------------------- ---------------
  request_id          PK
  purchase_id         FK → Purchase
  issue_category      
  issue_description   
  priority            
  current_status      
  created_at          
  updated_at          

This is the main table for customer service requests.

### 7. Request_Status_History

  Field        Key
  ------------ ----------------------
  history_id   PK
  request_id   FK → Service_Request
  status       
  remarks      
  changed_by   
  changed_at   

This stores every status change so the customer can see the request
history.

## Main Relationships

``` text
Company
   |
   └── Product

Customer
   |
   └── Purchase
          |
          ├── Product
          |
          └── Warranty
                 |
                 └── Service_Request
                          |
                          └── Request_Status_History
```

## Microservices

### 1. Company & Customer Service

Handles:

-   Company
-   Customer

### 2. Product, Warranty & Purchase Service

Handles:

-   Product
-   Warranty
-   Purchase

### 3. Service Request Service

Handles:

-   Service_Request
-   Request_Status_History

The Service Request data is used by both the customer side and company
executive side. The company executive mainly changes the request status.

## Backend Stack

``` text
Java
  ↓
Spring Boot
  ├── Spring Web       → REST APIs
  ├── Spring Data JPA  → Database access
  ├── Validation       → Input validation
  └── Spring Security  → Login/security
  ↓
JPA
  ↓
Hibernate
  ↓
JDBC
  ↓
H2
```

### Other Tools

-   **Maven** --- project and dependency management
-   **JUnit + Mockito** --- testing
-   **Postman** --- API testing
-   **H2 File Database** --- local database that keeps data after
    application restart

The goal is to keep the technology stack simple and focus on the
WarrantyHub business flow.
