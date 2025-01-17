package com.omar.backend.config;

import com.omar.backend.models.UserEntity;
import com.omar.backend.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByUsername("admin").isEmpty()) {
            UserEntity admin = UserEntity.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .roles(Set.of("ADMIN"))
                    .build();

            userRepository.save(admin);
        }

        if (userRepository.findByUsername("manager").isEmpty()) {
            UserEntity manager = UserEntity.builder()
                    .username("manager")
                    .password(passwordEncoder.encode("manager123"))
                    .roles(Set.of("MANAGER"))
                    .build();

            userRepository.save(manager);
        }

        if (userRepository.findByUsername("hr").isEmpty()) {
            UserEntity hr = UserEntity.builder()
                    .username("hr")
                    .password(passwordEncoder.encode("hr123"))
                    .roles(Set.of("HR"))
                    .build();

            userRepository.save(hr);
        }
    }
}
