package by.zemich.userms.service;

import by.zemich.userms.controller.request.RegisterRequest;
import by.zemich.userms.controller.request.UpdateUserRequest;
import by.zemich.userms.controller.response.UserFullResponse;
import by.zemich.userms.dao.entity.User;
import by.zemich.userms.dao.repository.UserRepository;
import by.zemich.userms.service.mapper.UserMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserRestService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User register(@Validated RegisterRequest registerRequest) {
        registerRequest.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        User rigisterUser = userMapper.mapToExistingEntity(registerRequest);
        return userRepository.save(rigisterUser);
    }


    public UserFullResponse deactivate(UUID userId) {
        return userRepository.findById(userId)
                .map(user -> {
                    user.setActive(false);
                    return userRepository.save(user);
                })
                .map(userMapper::mapToFullResponse)
                .orElseThrow(
                        () -> new EntityNotFoundException("User with id %s is nowhere to be found".formatted(userId))
                );
    }

    public UserFullResponse activate(UUID userId) {
        return userRepository.findById(userId)
                .map(user -> {
                    user.setActive(true);
                    return userRepository.save(user);
                })
                .map(userMapper::mapToFullResponse)
                .orElseThrow(
                        () -> new EntityNotFoundException("User with id %s is nowhere to be found".formatted(userId))
                );
    }

    public Page<UserFullResponse> findAll(Pageable pageable) {
        List<UserFullResponse> userFullResponses = userRepository.findAll(pageable).getContent().stream()
                .map(userMapper::mapToFullResponse)
                .toList();
        return new PageImpl<>(userFullResponses, pageable, userFullResponses.size());
    }

    public UserFullResponse findById(UUID userId) {
        return userRepository.findById(userId)
                .map(userMapper::mapToFullResponse)
                .orElseThrow(
                        () -> new EntityNotFoundException("User with id %s is nowhere to be found".formatted(userId))
                );
    }

    public void changePassword(String login, String newPassword) {
        userRepository.findByUsername(login)
                .map(user -> {
                    String encodedPassword = passwordEncoder.encode(newPassword);
                    user.setPassword(encodedPassword);
                    return userRepository.save(user);
                }).orElseThrow(
                        () -> new EntityNotFoundException("User %s not found".formatted(login))
                );
    }

    public UserFullResponse partialUpdate(UUID userId, UpdateUserRequest updateUserRequest) {
        return userRepository.findById(userId)
                .map(user -> {
                    userMapper.mapToExistingEntity(updateUserRequest, user);
                    return user;
                })
                .map(userRepository::save)
                .map(userMapper::mapToFullResponse)
                .orElseThrow(
                        () -> new EntityNotFoundException("User with id %s is nowhere to be found".formatted(userId))
                );
    }
}
