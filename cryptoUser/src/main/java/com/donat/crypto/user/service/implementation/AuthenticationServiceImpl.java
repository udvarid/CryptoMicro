package com.donat.crypto.user.service.implementation;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.donat.crypto.user.service.AuthenticatorService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticatorService {

    public static final int SESSION_EXPIRED_MIN = 240;

    private final Map<String, LoggedInUser> sessionMap = new ConcurrentHashMap();

    @Override
    public String giveSession(final String userId) {
        clearOldSessions();
        String session = getSession(userId);
        if (session == null) {
            session = generateNewSession();
            sessionMap.put(session, new LoggedInUser(userId));
        }
        return session;
    }

    @Override
    public void closeSession(final String sessionId) {
        sessionMap.remove(sessionId);
    }

    @Override
    public boolean validateSession(final String sessionId, final String userId) {
        clearOldSessions();
        return sessionMap.containsKey(sessionId) &&
                sessionMap.get(sessionId).getName().equals(userId);
    }

    private String getSession(final String userId) {
        return sessionMap.entrySet().stream()
                .filter(e -> e.getValue().getName().equals(userId))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }

    private String generateNewSession() {
        boolean uniqeSession = false;
        String generatedString = "";
        while (!uniqeSession) {
            generatedString = RandomStringUtils.random(20, true, true);
            uniqeSession = !sessionMap.containsKey(generatedString);
        }
        return generatedString;
    }

    private void clearOldSessions() {
        sessionMap.entrySet().removeIf(session ->
                ChronoUnit.MINUTES
                        .between(session.getValue().getTime(), LocalDateTime.now()) > SESSION_EXPIRED_MIN);
    }

    private static final class LoggedInUser {
        private final String name;
        private final LocalDateTime time;

        public LoggedInUser(String name) {
            this.name = name;
            this.time = LocalDateTime.now();
        }

        public String getName() {
            return name;
        }

        public LocalDateTime getTime() {
            return time;
        }
    }
}
