package com.financialhub.identityservice.application.usecase;

import com.financialhub.identityservice.application.port.out.PasswordHasher;
import com.financialhub.identityservice.application.port.out.IdentityRegistrationStore;
import com.financialhub.identityservice.application.port.out.UserCredentialRepository;
import com.financialhub.identityservice.domain.UserCredential;
import java.time.Clock;
import java.util.Locale;
import java.util.Objects;

public final class RegisterUserUseCase {
    private final UserCredentialRepository users;
    private final IdentityRegistrationStore registrationStore;
    private final PasswordHasher passwordHasher;
    private final Clock clock;

    public RegisterUserUseCase(UserCredentialRepository users, IdentityRegistrationStore registrationStore, PasswordHasher passwordHasher, Clock clock) {
        this.users = Objects.requireNonNull(users); this.registrationStore = Objects.requireNonNull(registrationStore);
        this.passwordHasher = Objects.requireNonNull(passwordHasher); this.clock = Objects.requireNonNull(clock);
    }
    public UserCredential execute(String email, String rawPassword, String displayName) {
        if (email == null || email.isBlank()) throw new IllegalArgumentException("email is required");
        if (rawPassword == null || rawPassword.length() < 12) throw new IllegalArgumentException("password must contain at least 12 characters");
        if (displayName == null || displayName.isBlank()) throw new IllegalArgumentException("displayName is required");
        String normalizedEmail = email.trim().toLowerCase(Locale.ROOT);
        if (users.existsByEmail(normalizedEmail)) throw new IllegalStateException("Email is already registered");
        String passwordHash = passwordHasher.hash(rawPassword);
        return registrationStore.registerWithOutbox(UserCredential.register(normalizedEmail, passwordHash, clock.instant()), displayName.trim());
    }
}
