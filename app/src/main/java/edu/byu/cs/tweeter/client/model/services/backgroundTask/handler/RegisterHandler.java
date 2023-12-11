package edu.byu.cs.tweeter.client.model.services.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.services.UserService;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.RegisterTask;
import edu.byu.cs.tweeter.client.model.services.observer.ServiceObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class RegisterHandler extends BackgroundTaskHandler {
    public static String REGISTER_ACTION_KEY = "REGISTER_ACTION_KEY";
    public RegisterHandler(UserService.RegisterObserver observer) {
        super(observer,"Failed to register because of exception: ");
        setActionKey(REGISTER_ACTION_KEY);
    }

    @Override
    protected void handleSuccessMessage(ServiceObserver observer, Bundle data) {
        UserService.RegisterObserver myObserver = (UserService.RegisterObserver)observer;

        User registeredUser = (User) data.getSerializable(RegisterTask.USER_KEY);
        AuthToken authToken = (AuthToken) data.getSerializable(RegisterTask.AUTH_TOKEN_KEY);

        Cache.getInstance().setCurrUser(registeredUser);
        Cache.getInstance().setCurrUserAuthToken(authToken);

        myObserver.registerSucceeded(authToken, registeredUser);
    }
}
