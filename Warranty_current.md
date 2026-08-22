WarrantyHub — Phase 6 & 7 Conversion Plan

1. What Has Been Done

Phase 6 — Business Flow

Company Creation

API: POST /api/companies

Creates a company/executive entry.

Company Data Entry

API: POST /api/purchases

One transactional API creates:

Customer

Product

Warranty

Purchase

The Product is linked to the Company, and the Purchase links Customer, Product, and Warranty.

Customer Service Request

API: POST /api/service-requests

Creates a Service Request against an existing Purchase.

Initial status is created and recorded in RequestStatusHistory.

Service Request Tracking

API: GET /api/service-requests/{id}

Returns current Service Request information/status.

API: GET /api/service-requests/{id}/history

Returns status history.

Company Status Change

API: PUT /api/service-requests/{id}/status

Company changes the current status. A new status-history entry is created.

Phase 7 — Security

Login

API: POST /api/auth/login

Customer and Company authenticate using email/password.

Password Hashing

Passwords are stored using BCrypt rather than plaintext.

JWT Authentication

Successful login returns a JWT.

Protected requests use:

Authorization: Bearer <JWT>

JWT is validated through the JWT authentication filter.

Role Authorization

ROLE_CUSTOMER
ROLE_COMPANY

Customer:

Create Service Request

View own Service Request

View own history

Company:

Create Purchase

Change Service Request status

Resource-Level Authorization

Customer ownership checks have been started so a Customer cannot access another Customer's Service Request.

2. What Still Needs To Be Done

A. Customer — My Products

API

GET /api/customers/me/products

Use the JWT to identify the authenticated Customer and return all Products purchased by that Customer.

JWT
 ↓
Customer
 ↓
Purchases
 ↓
Products

The client must NOT provide a customer ID.

B. Company — Service Requests

API

GET /api/company/service-requests

Return all Service Requests belonging to the authenticated Company.

Company JWT
 ↓
Company
 ↓
Products
 ↓
Purchases
 ↓
Service Requests

A Company must only see requests related to its own products/purchases.

C. Finish changedBy Security

The client must not send changedBy.

Request:

{
  "status": "IN_PROGRESS",
  "remarks": "Request accepted"
}

The server gets the authenticated identity from:

SecurityContextHolder
 ↓
Authenticated Company
 ↓
changedBy

D. Finish Customer Ownership Protection

A Customer should only be able to:

View their own Service Requests.

View their own Service Request history.

View their own purchased Products.

Customer A requesting Customer B's resource must receive:

403 Forbidden

E. Finish Company Isolation

A Company should only be able to:

View its own Service Requests.

Change the status of its own Service Requests.

Company A accessing Company B's request must receive:

403 Forbidden

Relationship:

Company
 ↓
Product
 ↓
Purchase
 ↓
ServiceRequest

3. Final Business Flow

COMPANY
  ↓
POST /api/companies
  ↓
COMPANY LOGIN → JWT
  ↓
POST /api/purchases
  ↓
Customer + Product + Warranty + Purchase


CUSTOMER
  ↓
CUSTOMER LOGIN → JWT
  ↓
GET /api/customers/me/products
  ↓
Customer's Products

CUSTOMER
  ↓
POST /api/service-requests
  ↓
ServiceRequest = OPEN
  ↓
RequestStatusHistory


COMPANY
  ↓
GET /api/company/service-requests
  ↓
Company's Requests
  ↓
PUT /api/service-requests/{id}/status
  ↓
New Status + Status History


CUSTOMER
  ↓
GET /api/service-requests/{id}
GET /api/service-requests/{id}/history
  ↓
Track Request

4. Final Target APIs

Authentication

POST /api/auth/login

Company

POST /api/companies
GET  /api/company/service-requests

Purchase / Data Entry

POST /api/purchases

Customer

POST /api/customers
GET  /api/customers/me/products

Service Request

POST /api/service-requests
GET  /api/service-requests/{id}
PUT  /api/service-requests/{id}/status
GET  /api/service-requests/{id}/history

5. Phase 7 Completion Checklist

Customer authentication

Company authentication

Password hashing

JWT generation

JWT validation

Customer/Company roles

Remove client-controlled changedBy

Complete Customer ownership checks

Add Customer → My Products API

Add Company → My Service Requests API

Complete Company isolation

Verify all security rules through Postman

When these are complete, Phase 7 is finished.

Next Phase

Phase 8 — Testing & Hardening

test the api whole workflow