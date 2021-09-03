package com.donat.crypto.user.service.implementation;

import com.donat.crypto.user.service.AuthenticatorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class AuthenticationServiceImplTest {

    private static final String TEST_USER = "testUser";
    @Autowired
    AuthenticatorService service;

    @Test
    public void whenUserHasNoSession_thenNewSessionIsGiven() {
        final String session = service.giveSession(TEST_USER);
        assertThat(session).isNotEmpty();
    }

    @Test
    public void whenUserHasSession_thenSameSessionIsGiven() {
        final String session = service.giveSession(TEST_USER);
        final String sessionAgain = service.giveSession(TEST_USER);
        assertThat(session).isEqualTo(sessionAgain);
    }

    @Test
    public void whenUserHasSessionAndRemoveIt_thenItWillBeRemoved() {
        final String session = service.giveSession(TEST_USER);
        service.closeSession(session);
        final String sessionAgain = service.giveSession(TEST_USER);
        assertThat(session).isNotEqualTo(sessionAgain);
    }

    @Test
    public void whenUserHasNoSession_thenSessionValidationWillFail() {
        assertThat(service.validateSession("anything", TEST_USER)).isFalse();
    }

    @Test
    public void whenUserHasValidSession_thenSessionValidationWillPass() {
        final String session = service.giveSession(TEST_USER);
        assertThat(service.validateSession(session, TEST_USER)).isTrue();
    }

    @Test
    public void whenUserHasInvalidSession_thenSessionValidationWillFail() {
        final String session = service.giveSession(TEST_USER);
        assertThat(service.validateSession(session + "extra", TEST_USER)).isFalse();
    }

    @Test
    public void whenUserHasValidSessionButUserNameIsIncorrect_thenSessionValidationWillFail() {
        final String session = service.giveSession(TEST_USER);
        assertThat(service.validateSession(session, "Wrong User Name")).isFalse();
    }

}