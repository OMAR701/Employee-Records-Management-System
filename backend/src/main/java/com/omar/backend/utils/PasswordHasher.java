package com.omar.backend.utils;

import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.Key;

public class PasswordHasher {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println("Admin Password: " + encoder.encode("admin123"));
        System.out.println("HR Password: " + encoder.encode("hr123"));
        System.out.println("Manager Password: " + encoder.encode("manager123"));
        Key key = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS512);
        String encodedKey = java.util.Base64.getEncoder().encodeToString(key.getEncoded());
        System.out.println("Generated Key: " + encodedKey);
    }

}

