package com.omar.backend.utils;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class UserUtils {

    public static String getAuthenticatedUserRole() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream().findFirst()
                .orElseThrow(() -> new RuntimeException("Role not found"))
                .getAuthority();
    }

    public static String getAuthenticatedUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        return principal.toString();
    }

    public static String getUserDepartment(String username) {
        if ("manager".equals(username)) {
            return "Sales";
        }
        return "DefaultDepartment";
    }
}
