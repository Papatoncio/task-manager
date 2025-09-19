package com.papatoncio.taskapi.mappers;

import com.papatoncio.taskapi.dto.user.UserRequest;
import com.papatoncio.taskapi.dto.user.UserResponse;
import com.papatoncio.taskapi.entities.User;
import com.papatoncio.taskapi.utils.MapperUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    // DTO → Entidad
    public User toEntity(UserRequest request) {
        return User.builder()
                .username(request.username())
                .email(request.email())
                .password(request.password())
                .build();
    }

    // Entidad → DTO
    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getActive(),
                user.getCreatedAt(),
                MapperUtils.rolesToString(user.getRoles())
        );
    }

    // Entidad → UserDetails
    public UserDetails toUserDetails(User user) {
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                        .toList()
        );
    }
}
