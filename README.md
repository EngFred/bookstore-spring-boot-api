# 📚 Bookstore REST API

A robust and secure RESTful API for managing books and authors in a bookstore system. Built with **Spring Boot**, **Spring Security**, **JWT Authentication**, **PostgreSQL**, and **Cloudinary** for image uploads.

---

## 🚀 Features

- ✅ **User Authentication**
  - Register and login with email & password
  - Secure JWT-based authentication
  - Passwords encrypted with BCrypt
  - Access control for protected endpoints

- 📚 **Book Management**
  - Create, read, update, delete books
  - Upload cover images via **Cloudinary**
  - Only authors can edit or delete their books
  - Pagination & sorting supported
  - Search by title, genre, author's first or last name

- ✍️ **Author Management**
  - CRUD operations for authors
  - View books created by a specific author

- 🔐 **Security**
  - Spring Security 6+
  - JWT token validation
  - Global exception handling with custom error responses
  - Method-level access control (e.g., only the owner can update/delete)

---

## 🛠️ Tech Stack

| Technology       | Purpose                          |
|------------------|----------------------------------|
| Java 17          | Programming Language             |
| Spring Boot 3    | Backend Framework                |
| Spring Security  | Authentication & Authorization   |
| PostgreSQL       | Relational Database              |
| Hibernate/JPA    | ORM for database interaction     |
| Cloudinary       | Image upload and storage         |
| JWT              | Stateless Authentication         |
| Maven            | Build Tool                       |
| IntelliJ IDEA    | IDE for development              |

---

## 📦 API Endpoints

### 🔐 Authentication

| Method | Endpoint         | Description         |
|--------|------------------|---------------------|
| POST   | `/bookstore/auth/register` | Register a new user |
| POST   | `/bookstore/auth/login`    | Login and receive a JWT |

---

### 👤 Authors

| Method | Endpoint                  | Description                    |
|--------|---------------------------|--------------------------------|
| POST   | `/bookstore/authors`            | Create new author              |
| GET    | `/bookstore/authors`            | Get all authors (paginated)    |
| GET    | `/bookstore/authors/{id}`       | Get author by ID               |
| PUT    | `/bookstore/authors/{id}`       | Update author                  |
| DELETE | `/bookstore/authors/{id}`       | Delete author                  |
| GET    | `/bookstore/authors/{id}/books` | Get books by author (paginated)|

---

### 📚 Books

| Method | Endpoint              | Description                          |
|--------|-----------------------|--------------------------------------|
| POST   | `/bookstore/books`          | Create a new book (with image)       |
| GET    | `/bookstore/books`          | Get all books (paginated, sortable)  |
| GET    | `/bookstore/books/search`   | Search books by title/author/genre   |
| GET    | `/bookstore/books/{id}`     | Get book by ID                       |
| PUT    | `/bookstore/books/{id}`     | Update book (author only)           |
| DELETE | `/bookstore/books/{id}`     | Delete book (author only)           |

---

## 🧪 Search Example

**Search Books:**
- GET /bookstore/books/search?query=fiction&type=genre&page=1&size=10&sort=title


