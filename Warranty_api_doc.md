# WarrantyHub API Documentation & Testing Workflow

This document provides a comprehensive overview of the WarrantyHub API endpoints and a step-by-step chronological guide on how to test the business workflow.

## Business Workflow Summary

The WarrantyHub platform handles the lifecycle of a product warranty and its related service requests.
The core workflow is isolated into two main roles: **Company** and **Customer**.

1. **Company Registration & Authentication**: A manufacturing/retail company signs up and logs in to get an access token.
2. **Purchase Registration (Company)**: The company registers a purchase on behalf of a customer. This single transactional endpoint creates the Customer (if they don't exist), Product, Warranty, and links them via a Purchase record.
3. **Customer Authentication**: The customer logs in using the credentials established during the purchase registration.
4. **Customer Product Retrieval**: The customer can fetch a list of all products they have purchased.
5. **Service Request Creation (Customer)**: The customer raises a service request for a specific product they own.
6. **Service Request Management (Company)**: The company can view all service requests across their products and update their statuses (e.g., OPEN -> IN_PROGRESS -> RESOLVED).
7. **Service Request Tracking (Customer)**: The customer can view the history and status updates of their service requests.

---

## Chronological Testing Guide

To test the APIs seamlessly, follow this chronological order:

### 1. Register a Company
*   **Endpoint**: `POST /api/companies`
*   **Description**: Creates a new company account.
*   **Payload Example**:
    ```json
    {
      "companyName": "Acme Corp",
      "email": "acme@example.com",
      "password": "password123",
      "phone": "9999999999",
      "status": "ACTIVE"
    }
    ```

### 2. Login as Company
*   **Endpoint**: `POST /api/auth/login`
*   **Description**: Authenticates the company and returns a JWT token.
*   **Payload Example**:
    ```json
    {
      "email": "acme@example.com",
      "password": "password123"
    }
    ```
*   *Note: Save the raw text response as your `Company Token` to use in the Authorization header (`Bearer <token>`).*

### 3. Register a Purchase Bundle (Requires Company Token)
*   **Endpoint**: `POST /api/purchases`
*   **Description**: Company registers a purchase, effectively onboarding a customer and logging their product + warranty.
*   **Payload Example**:
    ```json
    {
      "companyId": 1,
      "customerName": "Alice Smith",
      "customerEmail": "alice@example.com",
      "customerPhone": "1112223333",
      "customerPassword": "password123",
      "productName": "SuperLaptop Pro",
      "productCategory": "Electronics",
      "modelNumber": "SLP-2026",
      "warrantyPeriod": 2,
      "warrantyUnit": "YEARS",
      "warrantyTerms": "Comprehensive Hardware Coverage",
      "purchaseDate": "2026-08-20",
      "invoiceNumber": "INV-10001"
    }
    ```

### 4. Login as Customer
*   **Endpoint**: `POST /api/auth/login`
*   **Description**: Authenticates the customer (using credentials from step 3) and returns a JWT token.
*   **Payload Example**:
    ```json
    {
      "email": "alice@example.com",
      "password": "password123"
    }
    ```
*   *Note: Save the raw text response as your `Customer Token`.*

### 5. View Customer's Products (Requires Customer Token)
*   **Endpoint**: `GET /api/customers/me/products`
*   **Description**: Retrieves the list of products owned by the authenticated customer. Will return `SuperLaptop Pro` and its `purchaseId`.

### 6. Create a Service Request (Requires Customer Token)
*   **Endpoint**: `POST /api/service-requests`
*   **Description**: Customer raises a service request for a specific purchase.
*   **Payload Example**:
    ```json
    {
      "purchaseId": 1,
      "issueCategory": "Hardware",
      "issueDescription": "The screen flickers occasionally.",
      "priority": "HIGH"
    }
    ```

### 7. View Company's Service Requests (Requires Company Token)
*   **Endpoint**: `GET /api/company/service-requests`
*   **Description**: The company views all active service requests submitted for their products.

### 8. Update Service Request Status (Requires Company Token)
*   **Endpoint**: `PUT /api/service-requests/{requestId}/status`
*   **Description**: The company updates the status of the service request. The `changedBy` field is securely inferred from the company's authentication context.
*   **Payload Example**:
    ```json
    {
      "status": "IN_PROGRESS",
      "remarks": "Technician assigned to inspect the screen."
    }
    ```

### 9. View Service Request History (Requires Customer Token or Company Token)
*   **Endpoint**: `GET /api/service-requests/{requestId}/history`
*   **Description**: Shows the chronological history of status changes, remarks, and who made the change.

---

## Security & Isolation Rules Enforced

*   **Authentication**: Every protected endpoint requires a valid JWT Bearer token.
*   **Customer Isolation**: A customer cannot view another customer's products, nor can they create or view service requests for purchases that don't belong to them (Returns `403 Forbidden`).
*   **Company Isolation**: A company cannot view or update service requests for products manufactured/sold by a different company (Returns `403 Forbidden`).
*   **Audit Logging**: Status updates automatically resolve the user's email from the JWT context rather than relying on the client payload.
