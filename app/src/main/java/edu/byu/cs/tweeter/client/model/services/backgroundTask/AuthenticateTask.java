package edu.byu.cs.tweeter.client.model.services.backgroundTask;

import android.os.Handler;

public abstract class AuthenticateTask extends BackgroundTask {

    public static final String USER_KEY = "user";
    public static final String AUTH_TOKEN_KEY = "auth-token";


    /**
     * The user's username (or "alias" or "handle"). E.g., "@susan".
     */
    protected final String username;

    /**
     * The user's password.
     */
    protected final String password;

    protected AuthenticateTask(Handler messageHandler, String username, String password, String urlPath) {
        super(messageHandler, urlPath);
        this.username = username;
        this.password = password;
    }

}
