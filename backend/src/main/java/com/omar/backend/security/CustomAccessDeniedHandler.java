package com.omar.backend.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;



// Purpose: This class handles access denied exceptions (403 Forbidden errors).

@Profile("!test")
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Value("${messages.error.access.denied}")
    private String accessDeniedMessage;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
            throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().write(accessDeniedMessage);
    }
}
