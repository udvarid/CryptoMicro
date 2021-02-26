package com.donat.crypto.user.service;

public interface AuthenticatorService {

    String giveSession(String userId);

    void closeSession(String sessionId);

    boolean validateSession(String sessionId, String userId);

}
