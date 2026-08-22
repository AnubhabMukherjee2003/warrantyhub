# WarrantyHub

## 1. Project Plan

**WarrantyHub** is a SaaS-based warranty and service-request platform for consumer electronics companies in the CBG domain.

### Core Business Flow
``` text
Company subscribes to WarrantyHub
        ↓
Company executive enters customer, product, warranty and purchase details
        ↓
Customer faces a product issue
        ↓
Customer logs in to WarrantyHub
        ↓
Customer raises a Service Request
        ↓
System checks purchase & warranty details
        ↓
Company executive sees the request
        ↓
Company executive changes the request status
        ↓
Customer sees the updated status
        ↓
Request is resolved
        ↓
Request is closed
        ↓
Status history is retained
```

### Main Actors
- **Company Executive**: Manages customer, product, warranty, and purchase information. Views service requests. Changes the service request status.
- **Customer**: Logs in. Views their purchased products. Raises a service request. Tracks the service request and its status.
- **System**: Validates request-related purchase and warranty information. Creates service requests. Maintains current status and status history.

### Future Works
We plan to extend the platform beyond software-only tracking by adding:
- **Technician Management Platform**: A dedicated module for managing physical service work, scheduling, and technician activity.
- **Claim Settlement Model**: A system for dispatching technicians and parts from the company executive side to handle physical replacements and service tasks seamlessly.

---

## 2. Architecture & Stack

### Architecture Style
WarrantyHub uses a simple **microservices architecture** with three Spring Boot services communicating with a shared H2 database.

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
 Service          Purchase Service   
        |              |              |
        +--------------+--------------+
                       |
                 Shared Database
                       |
                       H2
```

### Database Tables
1. **Company**: `company_id` (PK), `company_name`, `email` (Unique), `password`, `phone`, `status`, `created_at`
2. **Customer**: `customer_id` (PK), `name`, `email` (Unique), `phone`, `password`, `created_at`
3. **Product**: `product_id` (PK), `company_id` (FK), `product_name`, `category`, `model_number`
4. **Warranty**: `warranty_id` (PK), `warranty_period`, `warranty_unit`, `terms`
5. **Purchase**: `purchase_id` (PK), `customer_id` (FK), `product_id` (FK), `warranty_id` (FK), `purchase_date`, `invoice_number`
6. **Service_Request**: `request_id` (PK), `purchase_id` (FK), `issue_category`, `issue_description`, `photo_url`, `video_url`, `created_at`, `updated_at`
7. **Request_Status_History**: `history_id` (PK), `request_id` (FK), `status`, `remarks`, `changed_by`, `changed_at`

### Backend Stack
- **Java 17**, **Spring Boot 4.1.0**
- **Spring Web MVC** (REST APIs)
- **Spring Data JPA** (Database access)
- **Spring Security** (JWT Authentication, Roles, BCrypt)
- **H2 File Database** (Local persistent storage)
- **Maven**, **JUnit + Mockito**

---

## 3. Implementation: Backend

The backend is fully secured with JWT authentication and role-based access control (`ROLE_CUSTOMER`, `ROLE_COMPANY`). Customers and Companies are fully isolated and can only access their respective data.

### API Endpoints

#### Authentication
- **POST `/api/auth/login`**: Authenticates a company or customer and returns a JWT token.
  - *Req*: `{ "email": "...", "password": "..." }`
  - *Res*: `<JWT_TOKEN>` (Raw String)

#### Company Operations
- **POST `/api/companies`**: Registers a new company.
  - *Req*: `{ "companyName": "Acme Corp", "email": "acme@example.com", "password": "password123", "phone": "9999999999", "status": "ACTIVE" }`
  - *Res*: `{ "companyId": 1, "companyName": "Acme Corp", "email": "acme@example.com", "phone": "9999999999", "status": "ACTIVE" }`
- **GET `/api/company/service-requests`** *(Requires Company Token)*: Views all service requests for the company's products.

#### Customer Operations
- **POST `/api/customers`**: Registers a customer manually (usually done automatically via purchase bundle).
- **GET `/api/customers/me/products`** *(Requires Customer Token)*: Retrieves a list of products purchased by the authenticated customer.
  - *Res*: `[ { "purchaseId": 1, "productId": 1, "productName": "...", "companyName": "..." } ]`

#### Purchase Operations
- **POST `/api/purchases`** *(Requires Company Token)*: Registers a purchase bundle (Customer, Product, Warranty, Purchase). `companyId` is derived from the JWT.
  - *Req*: `{ "customerName": "...", "customerEmail": "...", "customerPhone": "...", "customerPassword": "...", "productName": "...", "productCategory": "...", "modelNumber": "...", "warrantyPeriod": 12, "warrantyUnit": "MONTHS", "warrantyTerms": "...", "purchaseDate": "2026-08-20", "invoiceNumber": "..." }`
  - *Res*: `{ "purchaseId": 1, "customerId": 1, "productId": 1, "warrantyId": 1 }`

#### Service Request Operations
- **POST `/api/service-requests`** *(Requires Customer Token)*: Creates a service request against a `purchaseId`.
  - *Req*: `{ "purchaseId": 1, "issueCategory": "Hardware", "issueDescription": "...", "photoUrl": "...", "videoUrl": "..." }`
  - *Res*: Created Service Request Object
- **GET `/api/customers/service-requests`** *(Requires Customer Token)*: Retrieves all service requests for the authenticated customer.
- **GET `/api/service-requests/{id}`** *(Requires Customer Token)*: Fetches details of a specific service request.
- **PUT `/api/service-requests/{id}/status`** *(Requires Company Token)*: Company updates the status.
  - *Req*: `{ "status": "IN_PROGRESS", "remarks": "Technician assigned" }`
  - *Res*: Updated Service Request Object
- **GET `/api/service-requests/{id}/history`** *(Requires Customer or Company Token)*: Shows the history of status updates.
  - *Res*: 
    ```json
    {
      "serviceRequest": {
        "requestId": 1,
        "issueCategory": "...",
        "photoUrl": "...",
        "currentStatus": "IN_PROGRESS"
      },
      "history": [
        {
          "historyId": 1,
          "status": "OPEN",
          "remarks": "Service request created",
          "changedBy": "customer@example.com",
          "changedAt": "..."
        },
        {
          "historyId": 2,
          "status": "IN_PROGRESS",
          "remarks": "Technician assigned",
          "changedBy": "company@example.com",
          "changedAt": "..."
        }
      ]
    }
    ```

---

## 4. API Workflow

To test the APIs seamlessly, follow this chronological order:

1. **Register a Company**: `POST /api/companies`. Create the company account.
2. **Login as Company**: `POST /api/auth/login`. Authenticate and save the raw string response as your `Company Token`.
3. **Register a Purchase Bundle**: `POST /api/purchases` (Requires Company Token). The company registers a purchase, effectively onboarding a customer and logging their product + warranty. The Company ID is safely extracted from the token context.
4. **Login as Customer**: `POST /api/auth/login`. Authenticate the customer (using the `customerEmail` and `customerPassword` provided in step 3) and save the raw string response as your `Customer Token`.
5. **View Customer's Products**: `GET /api/customers/me/products` (Requires Customer Token). Retrieves the list of products owned by the customer to find the `purchaseId`.
6. **Create a Service Request**: `POST /api/service-requests` (Requires Customer Token). The customer raises a service request using the `purchaseId` from step 5.
7. **View Company's Service Requests**: `GET /api/company/service-requests` (Requires Company Token). The company views all active service requests submitted across their products.
8. **Update Service Request Status**: `PUT /api/service-requests/{requestId}/status` (Requires Company Token). The company updates the status (e.g., to `IN_PROGRESS`). The `changedBy` field is securely inferred from the token.
9. **View Service Request History**: `GET /api/service-requests/{requestId}/history` (Requires Customer or Company Token). Shows the chronological history of status changes and remarks.
