package com.engfred.bookstore.dto.request;

import com.engfred.bookstore.data.entities.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Getter
@Setter
@Builder
public class UpdateUserRequest {
    @Size(max = 100, message = "Firstname cannot exceed 100 characters")
    private String firstname;

    @Size(max = 100, message = "Lastname cannot exceed 100 characters")
    private String lastname;

    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    private String email;

    @Pattern(regexp = "\\+?[0-9]{9,15}", message = "Invalid phone number")
    private String phoneNumber;

    @Size(max = 200, message = "Address cannot exceed 200 characters")
    private String address;

    @Size(max = 500, message = "Bio must be less than 500 characters")
    private String bio;

    private MultipartFile profileImage;

    private Gender gender;
}
