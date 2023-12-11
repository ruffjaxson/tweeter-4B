package edu.byu.cs.tweeter.server.dao.abstract_classes;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public interface AuthTokenDAO {

    AuthToken create();
    boolean verify(AuthToken authToken);
    AuthToken renew(AuthToken authToken);
    void deleteToken(AuthToken authToken);
}
