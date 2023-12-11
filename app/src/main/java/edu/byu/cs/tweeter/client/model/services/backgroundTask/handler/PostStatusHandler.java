package edu.byu.cs.tweeter.client.model.services.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.services.FollowService;
import edu.byu.cs.tweeter.client.model.services.StatusService;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.model.services.observer.ServiceObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;

public class PostStatusHandler extends BackgroundTaskHandler {
    public static String POST_STATUS_ACTION_KEY = "POST_STATUS_ACTION_KEY";
    public PostStatusHandler(StatusService.PostStatusObserver observer) {
        super(observer,"Failed to post status because of exception: ");
        setActionKey(POST_STATUS_ACTION_KEY);
    }

    @Override
    protected void handleSuccessMessage(ServiceObserver observer, Bundle data) {
        StatusService.PostStatusObserver myObserver = (StatusService.PostStatusObserver)observer;
        myObserver.postStatusSucceeded();
        AuthToken authToken = (AuthToken) data.getSerializable(LoginTask.AUTH_TOKEN_KEY);
        if (authToken != null){
            Cache.getInstance().setCurrUserAuthToken(authToken);
        }
    }
}
