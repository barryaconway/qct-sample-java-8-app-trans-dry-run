package com.nurkiewicz.download;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.Assert.*;

public class FileCredentialStorageTest {

    private FileCredentialStorage storage;
    private String testCredentialFile = "test-credentials.dat";
    
    @Before
    public void setUp() {
        storage = new FileCredentialStorage();
        ReflectionTestUtils.setField(storage, "credentialFile", testCredentialFile);
        storage.init();
    }
    
    @After
    public void tearDown() throws Exception {
        Path path = Paths.get(testCredentialFile);
        Files.deleteIfExists(path);
    }
    
    @Test
    public void shouldStoreAndRetrieveCredentials() {
        // given
        String username = "testuser";
        String password = "testpassword";
        
        // when
        boolean stored = storage.storeCredentials(username, password);
        Optional<String> retrieved = storage.getCredentials(username);
        
        // then
        assertTrue(stored);
        assertTrue(retrieved.isPresent());
    }
    
    @Test
    public void shouldValidateCorrectCredentials() {
        // given
        String username = "validuser";
        String password = "validpassword";
        storage.storeCredentials(username, password);
        
        // when
        boolean valid = storage.validateCredentials(username, password);
        
        // then
        assertTrue(valid);
    }
    
    @Test
    public void shouldNotValidateIncorrectCredentials() {
        // given
        String username = "validuser";
        String password = "validpassword";
        storage.storeCredentials(username, password);
        
        // when
        boolean valid = storage.validateCredentials(username, "wrongpassword");
        
        // then
        assertFalse(valid);
    }
    
    @Test
    public void shouldDeleteCredentials() {
        // given
        String username = "deleteuser";
        String password = "password";
        storage.storeCredentials(username, password);
        
        // when
        boolean deleted = storage.deleteCredentials(username);
        Optional<String> retrieved = storage.getCredentials(username);
        
        // then
        assertTrue(deleted);
        assertFalse(retrieved.isPresent());
    }
    
    @Test
    public void shouldReturnFalseWhenDeletingNonExistentCredentials() {
        // when
        boolean deleted = storage.deleteCredentials("nonexistentuser");
        
        // then
        assertFalse(deleted);
    }
    
    @Test
    public void shouldPersistCredentialsToFile() throws Exception {
        // given
        String username = "persistuser";
        String password = "persistpassword";
        
        // when
        storage.storeCredentials(username, password);
        
        // then
        File file = new File(testCredentialFile);
        assertTrue(file.exists());
        assertTrue(file.length() > 0);
        
        // Create a new storage instance to verify loading from file
        FileCredentialStorage newStorage = new FileCredentialStorage();
        ReflectionTestUtils.setField(newStorage, "credentialFile", testCredentialFile);
        newStorage.init();
        
        // Verify credentials were loaded
        Optional<String> retrieved = newStorage.getCredentials(username);
        assertTrue(retrieved.isPresent());
        assertTrue(newStorage.validateCredentials(username, password));
    }
    
    @Test
    public void shouldNotStoreEmptyCredentials() {
        // when
        boolean storedEmptyUsername = storage.storeCredentials("", "password");
        boolean storedEmptyPassword = storage.storeCredentials("username", "");
        boolean storedNullUsername = storage.storeCredentials(null, "password");
        boolean storedNullPassword = storage.storeCredentials("username", null);
        
        // then
        assertFalse(storedEmptyUsername);
        assertFalse(storedEmptyPassword);
        assertFalse(storedNullUsername);
        assertFalse(storedNullPassword);
    }
}