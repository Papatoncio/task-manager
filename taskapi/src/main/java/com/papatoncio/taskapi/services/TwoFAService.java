package com.papatoncio.taskapi.services;

import com.papatoncio.taskapi.dto.auth.TwoFAValidationRequest;
import com.papatoncio.taskapi.dto.common.ApiResponse;
import com.papatoncio.taskapi.dto.user.UserResponse;
import com.papatoncio.taskapi.entities.User;
import com.papatoncio.taskapi.mappers.UserMapper;
import com.papatoncio.taskapi.repositories.UserRepository;
import com.papatoncio.taskapi.security.SecurityUtil;
import com.papatoncio.taskapi.utils.ResponseFactory;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TwoFAService {
    @Value("${two.fa.issuer}")
    private String issuer;

    @Autowired
    GoogleAuthenticator googleAuthenticator;

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public TwoFAService(
            UserRepository userRepository,
            UserMapper userMapper
    ) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public ApiResponse generateTwoFASecret() {
        Long userId = SecurityUtil.getUserId();

        User user = userRepository.findById(userId).orElse(null);

        if (user == null)
            return ResponseFactory.error("El usuario no existe.");

        GoogleAuthenticatorKey key = googleAuthenticator.createCredentials(user.getEmail());

        user.setSecret2FA(key.getKey());
        userRepository.save(user);

        String otpAuthURL = GoogleAuthenticatorQRGenerator.getOtpAuthURL(
                issuer,
                user.getEmail(),
                key
        );

        return ResponseFactory.success(otpAuthURL, key.getKey());
    }

    public ApiResponse validateTwoFACode(TwoFAValidationRequest req) {
        User user = userRepository.findById(req.userId()).orElse(null);

        if (user == null)
            return ResponseFactory.error("El usuario no existe.");

        boolean isCodeValid = googleAuthenticator.authorizeUser(
                user.getEmail(),
                Integer.parseInt(req.code())
        );

        if (!isCodeValid)
            return ResponseFactory.error("Código invalido");

        return ResponseFactory.success("Código valido");
    }

    public ApiResponse enableTwoFA(String code) {
        Long userId = SecurityUtil.getUserId();

        User user = userRepository.findById(userId).orElse(null);

        if (user == null)
            return ResponseFactory.error("El usuario no existe.");

        boolean isCodeValid = googleAuthenticator.authorizeUser(
                user.getEmail(),
                Integer.parseInt(code)
        );

        if (!isCodeValid)
            return ResponseFactory.error("Código invalido");

        user.setTwoFactorEnabled(true);
        user = userRepository.save(user);

        UserResponse res = userMapper.toResponse(user);

        return ResponseFactory.success(res, "2FA activado correctamente.");
    }
}

