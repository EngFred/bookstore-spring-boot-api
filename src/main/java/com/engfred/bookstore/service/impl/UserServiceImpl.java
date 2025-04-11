package com.engfred.bookstore.service.impl;

import com.engfred.bookstore.data.entities.User;
import com.engfred.bookstore.data.repository.UserRepository;
import com.engfred.bookstore.dto.models.UserDto;
import com.engfred.bookstore.dto.response.PagingResult;
import com.engfred.bookstore.mappers.UserMapper;
import com.engfred.bookstore.service.UserService;
import com.engfred.bookstore.utils.SecurityUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public PagingResult<UserDto> getAllUsers(int pageNumber, int pageSize, String sortField) throws DataAccessException, IllegalArgumentException {
        final Pageable pageable = PageRequest.of(pageNumber-1, pageSize, Sort.by(sortField));
        final Page<User> entities = userRepository.findAll(pageable);
        final List<UserDto> entitiesDto = entities.stream().map(userMapper::toDto).toList();
        return new PagingResult<>(
                entitiesDto,
                entities.getTotalPages(),
                entities.getTotalElements(),
                entities.getSize(),
                entities.getNumber()+1,
                entities.isEmpty()
        );
    }

    @Override
    public UserDto getUserById(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found!"));
        return userMapper.toDto(user);
    }

    @Override
    public void deleteUser(UUID userId) throws AccessDeniedException {
        User user = userRepository.findById(userId)
                        .orElseThrow(() -> new EntityNotFoundException("User not found!"));

        if( user.getEmail().equals(SecurityUtils.getCurrentUserEmail()) && user.getId().equals(userId) ) {
            userRepository.deleteById(userId);
        } else {
            throw new AccessDeniedException("You are not allowed to delete this user!");
        }
    }

    @Override
    public UserDto updateUser(UUID userId, UserDto userDto) throws AccessDeniedException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found!"));

        if (user.getEmail().equals(SecurityUtils.getCurrentUserEmail()) && user.getId().equals(userId) ) {
            if (userDto.getFirstname() != null && !userDto.getFirstname().trim().equals(user.getFirstname().trim())) {
                user.setFirstname(userDto.getFirstname().trim());
            }
            if (userDto.getLastname() != null && !userDto.getLastname().trim().equals(user.getLastname().trim())) {
                user.setLastname(userDto.getLastname().trim());
            }
            if (userDto.getPhoneNumber() != null && !userDto.getPhoneNumber().trim().equals(user.getPhoneNumber().trim())) {
                user.setPhoneNumber(userDto.getPhoneNumber().trim());
            }
            if (userDto.getAddress() != null && !userDto.getAddress().trim().equals(user.getAddress().trim())) {
                user.setAddress(userDto.getAddress().trim());
            }
            if (userDto.getBio() != null && !userDto.getBio().trim().equals(user.getBio().trim())) {
                user.setBio(userDto.getBio().trim());
            }
            if (userDto.getImageUrl() != null && !userDto.getImageUrl().trim().equals(user.getImageUrl().trim())) {
                user.setImageUrl(userDto.getImageUrl().trim());
            }
            if (userDto.getGender() != null && !userDto.getGender().toString().trim().equals(user.getGender().toString().trim())) {
                user.setGender(userDto.getGender());
            }
            User updatedUser = userRepository.save(user);
            return userMapper.toDto(updatedUser);
        } else {
            throw new AccessDeniedException("You are not allowed to update this user!");
        }
    }
}
