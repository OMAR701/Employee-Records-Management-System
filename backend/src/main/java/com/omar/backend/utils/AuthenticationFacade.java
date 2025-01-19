package com.omar.backend.utils;


import org.springframework.security.core.Authentication;

public interface AuthenticationFacade {
    Authentication getAuthentication();

    String getAuthenticatedUsername();
}
