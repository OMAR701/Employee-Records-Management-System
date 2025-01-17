package com.omar.backend.services;

import com.omar.backend.models.UserEntity;
import com.omar.backend.repositories.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserEntity registerUser(String username, String plainPassword, String... roles) {
        String encryptedPassword = passwordEncoder.encode(plainPassword);

        UserEntity newUser = UserEntity.builder()
                .username(username)
                .password(encryptedPassword)
                .roles(Set.of(roles))
                .build();

        return userRepository.save(newUser);
    }
}
