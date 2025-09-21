package com.papatoncio.taskapi.controllers;

import com.papatoncio.taskapi.dto.auth.TwoFAValidationRequest;
import com.papatoncio.taskapi.dto.common.ApiResponse;
import com.papatoncio.taskapi.services.TwoFAService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/2fa")
public class TwoFAController {
    private final TwoFAService twoFAService;

    public TwoFAController(
            TwoFAService twoFAService
    ) {
        this.twoFAService = twoFAService;
    }

    @GetMapping("/")
    public ApiResponse generateTwoFASecret() {
        return twoFAService.generateTwoFASecret();
    }

    @PostMapping("/validate")
    public ApiResponse validateTwoFACode(
            @RequestBody TwoFAValidationRequest req
    ) {
        return twoFAService.validateTwoFACode(req);
    }

    @PostMapping("/enable")
    public ApiResponse enableTwoFA(
            @RequestBody String code
    ) {
        return twoFAService.enableTwoFA(code);
    }
}
