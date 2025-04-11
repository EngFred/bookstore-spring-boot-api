package com.engfred.bookstore.data.repository;

import com.engfred.bookstore.data.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

//this is where all database operations related to User will happen.
//Tells Spring to automatically implement common CRUD operations (like save(), findById(), findAll(), delete(), etc.) for the User entity.

@Repository //Marks this interface as a Spring-managed bean (so it can be injected with @Autowired or constructor injection).
public interface UserRepository extends JpaRepository<User, UUID> {
    //This is a custom finder method — Spring Data JPA will automatically implement it using the method name.
    Optional<User> findByEmail(String email);
}
