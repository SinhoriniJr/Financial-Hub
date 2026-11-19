package com.financialhub.identityservice.application.port.out;

import com.financialhub.identityservice.domain.UserCredential;

/** Commits credentials and a UserRegistered outbox event in the same local transaction. */
public interface IdentityRegistrationStore {
    UserCredential registerWithOutbox(UserCredential credential, String displayName);
}
