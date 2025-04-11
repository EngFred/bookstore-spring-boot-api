package com.engfred.bookstore.service.impl;

import com.engfred.bookstore.data.entities.ImageType;
import com.engfred.bookstore.data.entities.User;
import com.engfred.bookstore.data.repository.UserRepository;
import com.engfred.bookstore.dto.models.UserDto;
import com.engfred.bookstore.dto.request.UpdateUserRequest;
import com.engfred.bookstore.dto.response.CloudinaryUploadResult;
import com.engfred.bookstore.dto.response.PagingResult;
import com.engfred.bookstore.mappers.UserMapper;
import com.engfred.bookstore.service.CloudinaryService;
import com.engfred.bookstore.service.UserService;
import com.engfred.bookstore.utils.SecurityUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final CloudinaryService cloudinaryService;

    @Override
    public PagingResult<UserDto> getAllUsers(int pageNumber, int pageSize, String sortField) throws DataAccessException, IllegalArgumentException {
        logger.info("Fetching all users with pageNumber: {}, pageSize: {}, sortField: {}", pageNumber, pageSize, sortField);
        final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by(sortField));
        final Page<User> entities = userRepository.findAll(pageable);
        final List<UserDto> entitiesDto = entities.stream().map(userMapper::toDto).toList();
        logger.info("Fetched {} users from database", entitiesDto.size());
        return new PagingResult<>(
                entitiesDto,
                entities.getTotalPages(),
                entities.getTotalElements(),
                entities.getSize(),
                entities.getNumber() + 1,
                entities.isEmpty()
        );
    }

    @Override
    public PagingResult<UserDto> searchAuthors(String keyword, int pageNumber, int pageSize, String sortField) {
        logger.info("Searching users with keyword: '{}', pageNumber: {}, pageSize: {}, sortField: {}",
                keyword, pageNumber, pageSize, sortField);
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by(sortField));

        Page<User> usersPage = userRepository.searchByKeyword(keyword, pageable);
        List<UserDto> usersDto = usersPage.stream().map(userMapper::toDto).toList();

        logger.info("Found {} users matching keyword '{}'", usersDto.size(), keyword);
        return new PagingResult<>(
                usersDto,
                usersPage.getTotalPages(),
                usersPage.getTotalElements(),
                usersPage.getSize(),
                usersPage.getNumber() + 1,
                usersPage.isEmpty()
        );
    }


    @Override
    public UserDto getUserById(UUID userId) {
        logger.info("Fetching user by ID: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.error("User with ID: {} not found", userId);
                    return new EntityNotFoundException("User not found!");
                });
        logger.info("User with ID: {} found", userId);
        return userMapper.toDto(user);
    }

    @Override
    public void deleteUser() {
        String currentUserEmail = SecurityUtils.getCurrentUserEmail();
        logger.info("Attempting to delete user with email: {}", currentUserEmail);
        User user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> {
                    logger.error("User with email: {} not found", currentUserEmail);
                    return new EntityNotFoundException("User not found!");
                });

        //Delete profile image from Cloudinary if it exists
        if (user.getProfileImagePublicId() != null && !user.getProfileImagePublicId().isEmpty()) {
            try {
                cloudinaryService.deleteImage(user.getProfileImagePublicId());
                logger.info("Deleted book profile image from Cloudinary: {}", user.getProfileImagePublicId());
            } catch (IOException e) {
                logger.warn("Failed to delete image from Cloudinary for user ID: {}. Reason: {}", user.getId(), e.getMessage());
            }
        }

        userRepository.deleteById(user.getId());
        logger.info("User with email: {} and ID: {} successfully deleted", currentUserEmail, user.getId());
    }

    @Override
    public UserDto updateUser(UpdateUserRequest request) throws IOException {
        String currentUserEmail = SecurityUtils.getCurrentUserEmail();
        logger.info("Attempting to update user with email: {}", currentUserEmail);
        User user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> {
                    logger.error("User with email: {} not found", currentUserEmail);
                    return new EntityNotFoundException("User not found!");
                });

        logger.info("Updating fields for user with email: {}", currentUserEmail);

        // Upload image if present
        CloudinaryUploadResult uploadResult = null;
        if (request.getProfileImage() != null && !request.getProfileImage().isEmpty()) {
            uploadResult = cloudinaryService.uploadImage(request.getProfileImage(), ImageType.USER_IMAGE);
        }

        user.setFirstname(request.getFirstname() != null && !request.getFirstname().trim().equals(user.getFirstname().trim()) ? request.getFirstname().trim() : user.getFirstname());
        user.setLastname(request.getLastname() != null && !request.getLastname().trim().equals(user.getLastname().trim()) ? request.getLastname().trim() : user.getLastname());
        user.setPhoneNumber(request.getPhoneNumber() != null && !request.getPhoneNumber().trim().equals(user.getPhoneNumber().trim()) ? request.getPhoneNumber().trim() : user.getPhoneNumber());
        user.setAddress(request.getAddress() != null && !request.getAddress().trim().equals(user.getAddress().trim()) ? request.getAddress().trim() : user.getAddress());
        user.setBio(request.getBio() != null && !request.getBio().trim().equals(user.getBio().trim()) ? request.getBio().trim() : user.getBio());
        user.setGender(request.getGender() != null && !request.getGender().toString().trim().equals(user.getGender().toString().trim()) ? request.getGender() : user.getGender());

        if (uploadResult != null) {
            user.setImageUrl(uploadResult.imageUrl());
            user.setProfileImagePublicId(uploadResult.publicId());
        }

        User updatedUser = userRepository.save(user);
        logger.info("User with email: {} successfully updated", currentUserEmail);
        return userMapper.toDto(updatedUser);
    }
}
