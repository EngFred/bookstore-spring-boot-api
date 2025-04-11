package com.engfred.bookstore.dto.request;

import com.engfred.bookstore.data.entities.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "First name is required")
    private String firstname;

    private String lastname;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "\\+?[0-9]{9,15}", message = "Invalid phone number")
    private String phoneNumber;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    private Gender gender;

    @Size(max = 500, message = "Bio must be less than 500 characters")
    private String bio;

    private String imageUrl;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

}
