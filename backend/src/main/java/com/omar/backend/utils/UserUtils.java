package com.omar.backend.utils;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class UserUtils {


    // SecurityContextHolder: This class is used to retrieve authentication information stored in the security context of the current thread. It’s commonly used in Spring Security to handle user authentication and authorization.
    //Authentication: Represents the authentication details of the currently authenticated user.
    //UserDetails: A Spring Security interface for storing user-related information, such as username, password, and granted authorities (roles).

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
