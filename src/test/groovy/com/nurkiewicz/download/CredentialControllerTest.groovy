package com.nurkiewicz.download

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebAppConfiguration
@ContextConfiguration(classes = [MainApplication])
@ActiveProfiles("test")
class CredentialControllerTest extends Specification {

    private MockMvc mockMvc

    @Autowired
    private CredentialStorage credentialStorage

    @Autowired
    public void setWebApplicationContext(WebApplicationContext wac) {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build()
    }

    def 'should store credentials successfully'() {
        given:
            def requestJson = '{"username":"testuser","password":"testpassword"}'

        expect:
            mockMvc.perform(
                    post('/credentials/store')
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath('$.message').value('Credentials stored successfully'))
    }

    def 'should fail to store credentials with empty username'() {
        given:
            def requestJson = '{"username":"","password":"testpassword"}'

        expect:
            mockMvc.perform(
                    post('/credentials/store')
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath('$.message').value('Username and password are required'))
    }

    def 'should validate credentials successfully'() {
        given:
            credentialStorage.storeCredentials('validuser', 'validpassword')
            def requestJson = '{"username":"validuser","password":"validpassword"}'

        expect:
            mockMvc.perform(
                    post('/credentials/validate')
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath('$.message').value('Credentials are valid'))
    }

    def 'should fail to validate incorrect credentials'() {
        given:
            credentialStorage.storeCredentials('validuser', 'validpassword')
            def requestJson = '{"username":"validuser","password":"wrongpassword"}'

        expect:
            mockMvc.perform(
                    post('/credentials/validate')
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath('$.message').value('Invalid credentials'))
    }

    def 'should delete credentials successfully'() {
        given:
            credentialStorage.storeCredentials('deleteuser', 'password')

        expect:
            mockMvc.perform(
                    delete('/credentials/deleteuser'))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath('$.message').value('Credentials deleted successfully'))
    }

    def 'should return not found when deleting non-existent credentials'() {
        expect:
            mockMvc.perform(
                    delete('/credentials/nonexistentuser'))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath('$.message').value('No credentials found for the specified user'))
    }

    def 'should check if credentials exist'() {
        given:
            credentialStorage.storeCredentials('existinguser', 'password')

        expect:
            mockMvc.perform(
                    get('/credentials/existinguser'))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath('$.message').value('Credentials exist for the specified user'))
    }

    def 'should return not found when checking non-existent credentials'() {
        expect:
            mockMvc.perform(
                    get('/credentials/nonexistentuser'))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath('$.message').value('No credentials found for the specified user'))
    }
}