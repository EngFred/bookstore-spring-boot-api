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

```http
GET /bookstore/books/search?query=fiction&type=genre&page=1&size=10&sort=title


**Query Parameters:**

- `query`: Search term
- `type`: `title` (default), `author`, `genre`
- `page`, `size`, `sort`: Optional pagination and sorting

---

## ⚙️ Configuration

All sensitive configuration values are managed using **environment variables** to keep credentials secure:

| Variable               | Purpose                          |
|------------------------|----------------------------------|
| `DB_URL`               | Database connection URL          |
| `DB_USERNAME`          | Database username                |
| `DB_PASSWORD`          | Database password                |
| `JWT_SECRET`           | JWT signing key                  |
| `JWT_EXPIRATION_TIME`  | Token validity duration (in hrs) |
| `CLOUDINARY_URL`       | Cloudinary access key            |

> ✅ Set these inside IntelliJ:  
> **Run > Edit Configurations > Environment Variables**

---

## 🖼️ Cover Image Upload

- Book cover images are stored in Cloudinary under a clean folder structure (`/book-cover-images/...`)
- Images are deleted when a book is deleted or updated with a new cover
- Multipart form requests supported (max file size: 5MB)

---

## 🧰 Error Handling

All exceptions return well-structured, meaningful JSON responses. Example:

```json
{
  "error": "Validation failed",
  "details": {
    "title": "Title must not be empty",
    "pages": "Must be greater than 0"
  }
}

