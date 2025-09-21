package com.papatoncio.taskapi.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    private SecurityUtil() {
    }

    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static CustomUserDetails getUserDetails() {
        Authentication auth = getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof CustomUserDetails userDetails) {
            return userDetails;
        }
        return null;
    }

    public static Long getUserId() {
        CustomUserDetails userDetails = getUserDetails();
        return userDetails != null ? userDetails.getId() : 0;
    }

    public static String getUsername() {
        CustomUserDetails userDetails = getUserDetails();
        return userDetails != null ? userDetails.getUsername() : null;
    }
}
