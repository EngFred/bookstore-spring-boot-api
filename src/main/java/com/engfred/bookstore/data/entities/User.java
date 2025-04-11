package com.engfred.bookstore.data.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Table(name ="users")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String firstname;

    @Column(length = 100)
    private String lastname;

    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false, length = 200)
    private String address;

    @Column(length = 500)
    private String bio;

    @Column(nullable = false)
    private String password;

    private String imageUrl;

    @Column(name = "profile_image_public_id")
    private String profileImagePublicId;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore // Prevent circular references in JSON serialization
    private List<Book> books = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getUsername() {
        return this.email; // Spring uses this to identify the user
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
