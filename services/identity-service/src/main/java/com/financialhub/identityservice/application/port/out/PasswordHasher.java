package com.financialhub.identityservice.application.port.out;

public interface PasswordHasher {
    String hash(String rawPassword);
}
