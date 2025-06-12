package com.nurkiewicz.download;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * File-based implementation of CredentialStorage that stores credentials in a file.
 * Passwords are hashed using SHA-256 before storage.
 */
@Component
public class FileCredentialStorage implements CredentialStorage {

    private static final Logger log = LoggerFactory.getLogger(FileCredentialStorage.class);
    
    @Value("${credential.storage.file:credentials.dat}")
    private String credentialFile;
    
    private final Map<String, String> credentials = new HashMap<>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    
    @PostConstruct
    public void init() {
        loadCredentials();
    }
    
    @Override
    public boolean storeCredentials(String username, String password) {
        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            return false;
        }
        
        try {
            lock.writeLock().lock();
            credentials.put(username, hashPassword(password));
            saveCredentials();
            log.info("Credentials stored for user: {}", username);
            return true;
        } catch (Exception e) {
            log.error("Failed to store credentials for user: {}", username, e);
            return false;
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    @Override
    public Optional<String> getCredentials(String username) {
        try {
            lock.readLock().lock();
            return Optional.ofNullable(credentials.get(username));
        } finally {
            lock.readLock().unlock();
        }
    }
    
    @Override
    public boolean validateCredentials(String username, String password) {
        if (username == null || password == null) {
            return false;
        }
        
        try {
            lock.readLock().lock();
            String storedHash = credentials.get(username);
            if (storedHash == null) {
                return false;
            }
            
            String inputHash = hashPassword(password);
            return storedHash.equals(inputHash);
        } catch (Exception e) {
            log.error("Error validating credentials for user: {}", username, e);
            return false;
        } finally {
            lock.readLock().unlock();
        }
    }
    
    @Override
    public boolean deleteCredentials(String username) {
        if (username == null) {
            return false;
        }
        
        try {
            lock.writeLock().lock();
            if (credentials.containsKey(username)) {
                credentials.remove(username);
                saveCredentials();
                log.info("Credentials deleted for user: {}", username);
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("Failed to delete credentials for user: {}", username, e);
            return false;
        } finally {
            lock.writeLock().unlock();
        }
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
    
    private void saveCredentials() {
        Path path = Paths.get(credentialFile);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path.toFile()))) {
            oos.writeObject(new HashMap<>(credentials));
            log.debug("Credentials saved to file: {}", path.toAbsolutePath());
        } catch (IOException e) {
            log.error("Error saving credentials to file: {}", path.toAbsolutePath(), e);
            throw new RuntimeException("Error saving credentials", e);
        }
    }
    
    @SuppressWarnings("unchecked")
    private void loadCredentials() {
        Path path = Paths.get(credentialFile);
        if (!Files.exists(path)) {
            log.info("Credentials file does not exist. Creating new file: {}", path.toAbsolutePath());
            return;
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path.toFile()))) {
            Map<String, String> loadedCredentials = (Map<String, String>) ois.readObject();
            credentials.clear();
            credentials.putAll(loadedCredentials);
            log.info("Loaded {} credentials from file: {}", credentials.size(), path.toAbsolutePath());
        } catch (IOException | ClassNotFoundException e) {
            log.error("Error loading credentials from file: {}", path.toAbsolutePath(), e);
            // Continue with empty credentials map
        }
    }
}