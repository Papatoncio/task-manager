package com.papatoncio.taskapi.services;

import com.papatoncio.taskapi.entities.User;
import com.papatoncio.taskapi.repositories.UserRepository;
import com.warrenstrange.googleauth.ICredentialRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserCredentialService implements ICredentialRepository {
    private final UserRepository userRepository;

    public UserCredentialService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public String getSecretKey(String email) {
        return userRepository.findByEmail(email)
                .map(User::getSecret2FA)
                .orElse(null);
    }

    @Override
    public void saveUserCredentials(String email, String secretKey, int validationCode, List<Integer> scratchCodes) {
        userRepository.findByEmail(email).ifPresent(user -> {
            user.setSecret2FA(secretKey);
            userRepository.save(user);
        });
    }
}
