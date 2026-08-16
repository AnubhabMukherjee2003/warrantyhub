# WarrantyHub --- Project Plan

## Project Overview

**WarrantyHub** is a SaaS-based warranty and service-request platform
for consumer electronics companies in the CBG domain.

The company uses WarrantyHub to register customer purchase and warranty
information. When a customer faces a problem, the customer raises a
service request and tracks its status. The company executive updates the
request status from the company side.

## Core Business Flow

``` text
Company subscribes to WarrantyHub
        ↓
Company executive enters customer,
product, warranty and purchase details
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

The physical service work, technician activity, parts, and replacement
process are handled by the company outside WarrantyHub. WarrantyHub
mainly manages the request and its visible status.

## Main Actors

### Company Executive

-   Manage customer, product, warranty, and purchase information.
-   View service requests.
-   Change the service request status.

### Customer

-   Log in.
-   View their purchased products.
-   Raise a service request.
-   Track the service request and its status.

### System

-   Validate request-related purchase and warranty information.
-   Create service requests.
-   Maintain current status.
-   Maintain status history.
