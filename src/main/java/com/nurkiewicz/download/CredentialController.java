package com.nurkiewicz.download;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * REST controller for managing credentials.
 */
@RestController
@RequestMapping("/credentials")
public class CredentialController {

    private static final Logger log = LoggerFactory.getLogger(CredentialController.class);
    
    private final CredentialStorage credentialStorage;
    
    @Autowired
    public CredentialController(CredentialStorage credentialStorage) {
        this.credentialStorage = credentialStorage;
    }
    
    /**
     * Store credentials for a user.
     * 
     * @param request the credential request containing username and password
     * @return ResponseEntity with status and message
     */
    @PostMapping("/store")
    public ResponseEntity<Map<String, String>> storeCredentials(@RequestBody CredentialRequest request) {
        log.debug("Received request to store credentials for user: {}", request.getUsername());
        
        Map<String, String> response = new HashMap<>();
        
        if (request.getUsername() == null || request.getPassword() == null || 
            request.getUsername().isEmpty() || request.getPassword().isEmpty()) {
            response.put("message", "Username and password are required");
            return ResponseEntity.badRequest().body(response);
        }
        
        boolean stored = credentialStorage.storeCredentials(request.getUsername(), request.getPassword());
        
        if (stored) {
            response.put("message", "Credentials stored successfully");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Failed to store credentials");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Validate credentials for a user.
     * 
     * @param request the credential request containing username and password
     * @return ResponseEntity with status and message
     */
    @PostMapping("/validate")
    public ResponseEntity<Map<String, String>> validateCredentials(@RequestBody CredentialRequest request) {
        log.debug("Received request to validate credentials for user: {}", request.getUsername());
        
        Map<String, String> response = new HashMap<>();
        
        if (request.getUsername() == null || request.getPassword() == null) {
            response.put("message", "Username and password are required");
            return ResponseEntity.badRequest().body(response);
        }
        
        boolean valid = credentialStorage.validateCredentials(request.getUsername(), request.getPassword());
        
        if (valid) {
            response.put("message", "Credentials are valid");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Invalid credentials");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
    
    /**
     * Delete credentials for a user.
     * 
     * @param username the username
     * @return ResponseEntity with status and message
     */
    @DeleteMapping("/{username}")
    public ResponseEntity<Map<String, String>> deleteCredentials(@PathVariable String username) {
        log.debug("Received request to delete credentials for user: {}", username);
        
        Map<String, String> response = new HashMap<>();
        
        if (username == null || username.isEmpty()) {
            response.put("message", "Username is required");
            return ResponseEntity.badRequest().body(response);
        }
        
        boolean deleted = credentialStorage.deleteCredentials(username);
        
        if (deleted) {
            response.put("message", "Credentials deleted successfully");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "No credentials found for the specified user");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
    
    /**
     * Check if credentials exist for a user.
     * 
     * @param username the username
     * @return ResponseEntity with status and message
     */
    @GetMapping("/{username}")
    public ResponseEntity<Map<String, String>> checkCredentials(@PathVariable String username) {
        log.debug("Received request to check credentials for user: {}", username);
        
        Map<String, String> response = new HashMap<>();
        
        if (username == null || username.isEmpty()) {
            response.put("message", "Username is required");
            return ResponseEntity.badRequest().body(response);
        }
        
        boolean exists = credentialStorage.getCredentials(username).isPresent();
        
        if (exists) {
            response.put("message", "Credentials exist for the specified user");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "No credentials found for the specified user");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
    
    /**
     * Request object for credential operations.
     */
    public static class CredentialRequest {
        private String username;
        private String password;
        
        public String getUsername() {
            return username;
        }
        
        public void setUsername(String username) {
            this.username = username;
        }
        
        public String getPassword() {
            return password;
        }
        
        public void setPassword(String password) {
            this.password = password;
        }
    }
}