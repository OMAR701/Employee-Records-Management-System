package com.omar.backend.utils;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacadeImpl implements AuthenticationFacade {

    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public String getAuthenticatedUsername() {
        Authentication authentication = getAuthentication();
        return (authentication != null) ? authentication.getName() : null;
    }
}
