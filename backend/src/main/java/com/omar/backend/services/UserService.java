package com.omar.backend.services;

import com.omar.backend.models.UserEntity;
import com.omar.backend.repositories.UserRepository;
import com.omar.backend.utils.AuthenticationFacade;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationFacade authenticationFacade;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, AuthenticationFacade  authenticationFacade ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationFacade = authenticationFacade;
    }



    public UserEntity getCurrentUser() {
        String username = authenticationFacade.getAuthenticatedUsername();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
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
