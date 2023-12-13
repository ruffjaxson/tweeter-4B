package edu.byu.cs.tweeter.server.dao.dynamodb;


import static edu.byu.cs.tweeter.server.dao.dynamodb.tables.AuthTokens.TIME_TO_LIVE;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.server.dao.abstract_classes.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.dynamodb.tables.AuthTokens;

public class DDBAuthTokenDAO extends DynamoDAO<AuthTokens> implements AuthTokenDAO {
    private static final SecureRandom secureRandom = new SecureRandom(); //threadsafe
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder(); //th

    public DDBAuthTokenDAO() {
        super(AuthTokens.class, AuthTokens.TABLE_NAME, null);
    }

    @Override
    public AuthToken create() {
        System.out.println("In AuthToken create()");
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        String token = base64Encoder.encodeToString(randomBytes);
        AuthTokens ddbAuthTokens = new AuthTokens(token, Instant.now().getEpochSecond() + TIME_TO_LIVE);
        createOrOverwrite(ddbAuthTokens);
        return ddbAuthTokens.convert();
    }

    @Override
    public boolean verify(AuthToken authToken) {
        System.out.println("In AuthToken verify()");

        AuthTokens ddbAuthToken = find(authToken.getToken(), null);
        if (ddbAuthToken == null) return false;
        if (ddbAuthToken.isStillValid()) {
            renew(authToken);
            return true;
        }
        deleteToken(authToken);
        return false;
    }

    @Override
    public AuthToken renew(AuthToken authToken) {
        System.out.println("In AuthToken create()");
        AuthTokens token = new AuthTokens(authToken.getToken(), authToken.getTimestamp());
        // TODO: LOOK INTO WHY THE RENEWED AUTH TOKEN DOESN'T SEEM TO MAKE IT BACK TO THE CLIENT
        token.renewTimestamp();
        createOrOverwrite(token);
        return token.convert();
    }

    @Override
    public void deleteToken(AuthToken authToken) {
        System.out.println("In AuthToken delete()");
        delete(new AuthTokens(authToken.getToken(), authToken.getTimestamp()));
    }

    @Override
    protected void saveItem(AuthTokens entry) {

    }

    @Override
    public void changeRecordBeforeUpdate(AuthTokens existingEntry, Map<String, Integer> updateObject) {}
}
