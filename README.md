# Food Order Management System

A backend REST API project built using Java, Spring Boot, Hibernate, and MySQL for managing a food ordering platform.  
The system supports customer management, restaurant management, menu handling, order processing, payment tracking, and order item operations.

---

# Features

## Customer Management
- Add customer
- Update customer
- Delete customer
- Fetch customer by ID
- Fetch all customers

## Restaurant Management
- Add restaurant
- Update restaurant
- Delete restaurant
- Fetch restaurant details

## Menu Management
- Add menu items
- Update menu items
- Delete menu items
- Fetch menu items by restaurant

## Order Management
- Place order
- Cancel order
- Update order status
- Fetch orders by:
  - Customer
  - Status
  - Date
  - Price range
  - Restaurant

## Order Item Management
- Add items to existing order
- Update item quantity
- Remove order item
- Calculate subtotal and total amount automatically

## Payment Management
- Payment status validation
- Payment method handling
- Refund support for cancelled orders
- Prevent invalid payment transitions

---

# Technologies Used

- Java
- Spring Boot
- Spring Data JPA
- Hibernate
- MySQL
- Maven
- Postman

---

# Project Structure

``` id="6x1l5s"
src/main/java/com/foodorder
│
├── controller
├── service
├── dao
├── repository
├── entity
├── dto
├── exception
└── config