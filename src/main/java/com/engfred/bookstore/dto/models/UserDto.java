package com.engfred.bookstore.dto.models;

import com.engfred.bookstore.data.entities.Gender;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private UUID id;
    private String firstname;
    private String lastname;
    private String email;
    private String phoneNumber;
    private String address;
    private String bio;
    private String imageUrl;
    private Gender gender;
    private Date createdAt;
}
