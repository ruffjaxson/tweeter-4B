package edu.byu.cs.tweeter.client.model.services.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.services.StatusService;
import edu.byu.cs.tweeter.client.model.services.UserService;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.model.services.observer.ServiceObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class LoginHandler extends BackgroundTaskHandler {
    public static String LOGIN_ACTION_KEY = "LOGIN_ACTION_KEY";
    public LoginHandler(UserService.LoginObserver observer) {
        super(observer,"Failed to login because of exception: ");
        setActionKey(LOGIN_ACTION_KEY);
    }

    @Override
    protected void handleSuccessMessage(ServiceObserver observer, Bundle data) {
        UserService.LoginObserver myObserver = (UserService.LoginObserver)observer;

        User loggedInUser = (User) data.getSerializable(LoginTask.USER_KEY);
        AuthToken authToken = (AuthToken) data.getSerializable(LoginTask.AUTH_TOKEN_KEY);

        Cache.getInstance().setCurrUser(loggedInUser);
        Cache.getInstance().setCurrUserAuthToken(authToken);

        myObserver.loginSucceeded(authToken, loggedInUser);
    }
}
