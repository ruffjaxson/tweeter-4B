package edu.byu.cs.tweeter.client.model.services.backgroundTask;

import android.os.Handler;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public abstract class AuthenticatedTask extends BackgroundTask {


    protected AuthenticatedTask(AuthToken authToken, Handler messageHandler, String urlPath) {
        super(messageHandler, urlPath);
        this.authToken = authToken;
    }

    /**
     * Auth token for logged-in user.
     * This user is the "follower" in the relationship.
     */
    protected final AuthToken authToken;

    public AuthToken getAuthToken() {
        return authToken;
    }


}
