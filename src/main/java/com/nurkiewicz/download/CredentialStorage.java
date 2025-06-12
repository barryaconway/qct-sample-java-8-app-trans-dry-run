package com.nurkiewicz.download;

import java.util.Optional;

/**
 * Interface for storing and retrieving credentials.
 */
public interface CredentialStorage {
    
    /**
     * Store credentials for a specific user.
     * 
     * @param username the username
     * @param password the password
     * @return true if credentials were stored successfully, false otherwise
     */
    boolean storeCredentials(String username, String password);
    
    /**
     * Retrieve credentials for a specific user.
     * 
     * @param username the username
     * @return an Optional containing the password if found, empty otherwise
     */
    Optional<String> getCredentials(String username);
    
    /**
     * Validate if the provided credentials match stored credentials.
     * 
     * @param username the username
     * @param password the password to validate
     * @return true if credentials are valid, false otherwise
     */
    boolean validateCredentials(String username, String password);
    
    /**
     * Delete stored credentials for a specific user.
     * 
     * @param username the username
     * @return true if credentials were deleted successfully, false otherwise
     */
    boolean deleteCredentials(String username);
}