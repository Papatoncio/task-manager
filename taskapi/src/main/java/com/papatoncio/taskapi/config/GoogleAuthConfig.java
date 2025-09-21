package com.papatoncio.taskapi.config;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorConfig;
import com.warrenstrange.googleauth.ICredentialRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GoogleAuthConfig {
    @Bean
    public GoogleAuthenticator googleAuthenticator(ICredentialRepository credentialRepository) {
        GoogleAuthenticatorConfig config = new GoogleAuthenticatorConfig.GoogleAuthenticatorConfigBuilder()
                .setCodeDigits(6)     // dígitos del código
                .setTimeStepSizeInMillis(30000) // 30s
                .build();

        GoogleAuthenticator gAuth = new GoogleAuthenticator(config);
        gAuth.setCredentialRepository(credentialRepository);

        return gAuth;
    }
}
