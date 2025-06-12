package com.nurkiewicz.download;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * In-memory implementation of CredentialStorage for testing purposes.
 */
@Component
@Profile("test")
public class MemoryCredentialStorage implements CredentialStorage {

    private static final Logger log = LoggerFactory.getLogger(MemoryCredentialStorage.class);
    
    private final Map<String, String> credentials = new HashMap<>();
    
    @Override
    public boolean storeCredentials(String username, String password) {
        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            return false;
        }
        
        try {
            credentials.put(username, hashPassword(password));
            log.info("Credentials stored for user: {}", username);
            return true;
        } catch (Exception e) {
            log.error("Failed to store credentials for user: {}", username, e);
            return false;
        }
    }
    
    @Override
    public Optional<String> getCredentials(String username) {
        return Optional.ofNullable(credentials.get(username));
    }
    
    @Override
    public boolean validateCredentials(String username, String password) {
        if (username == null || password == null) {
            return false;
        }
        
        try {
            String storedHash = credentials.get(username);
            if (storedHash == null) {
                return false;
            }
            
            String inputHash = hashPassword(password);
            return storedHash.equals(inputHash);
        } catch (Exception e) {
            log.error("Error validating credentials for user: {}", username, e);
            return false;
        }
    }
    
    @Override
    public boolean deleteCredentials(String username) {
        if (username == null) {
            return false;
        }
        
        if (credentials.containsKey(username)) {
            credentials.remove(username);
            log.info("Credentials deleted for user: {}", username);
            return true;
        }
        return false;
    }
    
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            log.error("Error hashing password", e);
            throw new RuntimeException("Error hashing password", e);
        }
    }
}