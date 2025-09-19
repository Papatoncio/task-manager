package com.papatoncio.taskapi.services;

import com.papatoncio.taskapi.common.UserRole;
import com.papatoncio.taskapi.dto.auth.AuthRequest;
import com.papatoncio.taskapi.dto.common.ApiResponse;
import com.papatoncio.taskapi.dto.user.UserRequest;
import com.papatoncio.taskapi.entities.Role;
import com.papatoncio.taskapi.entities.User;
import com.papatoncio.taskapi.mappers.UserMapper;
import com.papatoncio.taskapi.repositories.RoleRepository;
import com.papatoncio.taskapi.repositories.UserRepository;
import com.papatoncio.taskapi.security.JwtUtil;
import com.papatoncio.taskapi.utils.ResponseFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final JwtUtil jwtUtil;
    private final UserMapper userMapper;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            RoleRepository roleRepository,
            JwtUtil jwtUtil,
            UserMapper userMapper
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.jwtUtil = jwtUtil;
        this.userMapper = userMapper;
    }

    public ApiResponse register(UserRequest req) {
        if (userRepository.findByEmail(req.email()).isPresent())
            return ResponseFactory.error("El email ya existe.");

        User user = new User();
        user.setUsername(req.username());
        user.setEmail(req.email());
        user.setPassword(passwordEncoder.encode(req.password()));

        Role role = roleRepository.findByName(UserRole.ROLE_USER).orElse(null);

        if (role == null)
            return ResponseFactory.error("No se ha encontrado el rol.");

        user.setRoles(Set.of(role));
        userRepository.save(user);

        return ResponseFactory.created(req, "Usuario registrado correctamente.");
    }

    public ApiResponse login(AuthRequest req) {
        User user = userRepository.findByEmail(req.email()).orElse(null);
        if (user == null)
            return ResponseFactory.error("Credenciales Incorrectas.");

        if (!passwordEncoder.matches(req.password(), user.getPassword()))
            return ResponseFactory.error("Credenciales Incorrectas.");

        return ResponseFactory.success(jwtUtil.generateToken(user), "Inicio de sesión éxitoso.");
    }
}
