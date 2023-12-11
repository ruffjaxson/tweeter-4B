package edu.byu.cs.tweeter.client.model.services.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.services.UserService;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.model.services.observer.ServiceObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class LogoutHandler extends BackgroundTaskHandler {
    public static String LOGIN_ACTION_KEY = "LOGIN_ACTION_KEY";
    public LogoutHandler(UserService.LogoutObserver observer) {
        super(observer,"Failed to logout because of exception: ");
        setActionKey(LOGIN_ACTION_KEY);
    }

    @Override
    protected void handleSuccessMessage(ServiceObserver observer, Bundle data) {
        UserService.LogoutObserver myObserver = (UserService.LogoutObserver)observer;

        myObserver.logoutSucceeded();
    }
}
