package com.papatoncio.taskapi.controllers;

import com.papatoncio.taskapi.dto.user.UserRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class testController {
    @PostMapping("/test")
    public ResponseEntity<?> register(@RequestBody UserRequest req) {
//        if (userRepository.findByEmail(req.email()).isPresent()) return ResponseEntity.status(409).build();
//        User user = new User();
//        user.setUsername(req.username());
//        user.setPassword(passwordEncoder.encode(req.password()));
//        user.getRoles().add(UserRole.USER);
//        userRepository.save(user);
        return ResponseEntity.ok().build();
    }
}
