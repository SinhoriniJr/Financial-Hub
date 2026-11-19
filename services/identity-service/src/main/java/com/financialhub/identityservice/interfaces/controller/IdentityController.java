package com.financialhub.identityservice.interfaces.controller;

import com.financialhub.identityservice.application.usecase.RegisterUserUseCase;
import com.financialhub.identityservice.interfaces.dto.request.RegisterRequest;
import com.financialhub.identityservice.interfaces.dto.response.RegistrationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/identity")
public class IdentityController {
    private final RegisterUserUseCase registerUser;
    public IdentityController(RegisterUserUseCase registerUser) { this.registerUser = registerUser; }

    @PostMapping("/registrations")
    public ResponseEntity<RegistrationResponse> register(@Valid @RequestBody RegisterRequest request) {
        var user = registerUser.execute(request.email(), request.password(), request.displayName());
        var response = new RegistrationResponse(user.getId(), user.getEmail(), user.getStatus().name(), user.getCreatedAt());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
