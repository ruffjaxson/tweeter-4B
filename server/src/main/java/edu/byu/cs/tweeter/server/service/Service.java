package edu.byu.cs.tweeter.server.service;


import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.server.dao.abstract_classes.AuthTokenDAO;

public abstract class Service {
    private final int TTL = 720;
    private final AuthTokenDAO authTokenDAO;
    protected AuthToken authToken;


    protected final String AUTHENTICATION_FAILED_MESSAGE = "AuthToken verification failed --> You must be logged in to perform this action.";

    Service (AuthTokenDAO dao) {
        this.authTokenDAO = dao;
    }

    public AuthToken createAuthToken() {
        AuthToken token = this.authTokenDAO.create();
        this.authToken = token;
        return token;
    }

    public void deleteAuthToken(AuthToken token) {
        this.authTokenDAO.deleteToken(token);
    }

    public boolean isAuthTokenInvalid(AuthToken token) {
        this.authToken = token;
        return !this.authTokenDAO.verify(token);
    }

    public AuthToken renewedAuthToken() {
        if (this.authToken == null) {
            return createAuthToken();
        }
        return this.authTokenDAO.renew(authToken);
    }
}
