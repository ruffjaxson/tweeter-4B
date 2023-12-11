package edu.byu.cs.tweeter.client.model.services.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.services.FollowService;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.model.services.observer.ServiceObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;

public class FollowHandler extends BackgroundTaskHandler {
    public static String FOLLOW_ACTION_KEY = "FOLLOW_ACTION_KEY";
    public FollowHandler(FollowService.FollowObserver observer) {
        super(observer,"Failed to follow because of exception: ");
        setActionKey(FOLLOW_ACTION_KEY);
    }

    @Override
    protected void handleSuccessMessage(ServiceObserver observer, Bundle data) {
        FollowService.FollowObserver myObserver = (FollowService.FollowObserver)observer;
        myObserver.handleFollowSuccessMessage();
        AuthToken authToken = (AuthToken) data.getSerializable(LoginTask.AUTH_TOKEN_KEY);
        if (authToken != null){
            Cache.getInstance().setCurrUserAuthToken(authToken);
        }
    }
}