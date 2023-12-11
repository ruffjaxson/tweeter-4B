package edu.byu.cs.tweeter.client.model.services.backgroundTask.handler;

import android.os.Bundle;


import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.services.FollowService;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.model.services.observer.ServiceObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;

public class GetFollowersCountHandler extends BackgroundTaskHandler {
    public static String GET_FOLLOWERS_COUNT_ACTION_KEY = "GET_FOLLOWERS_COUNT_ACTION_KEY";
    public GetFollowersCountHandler(FollowService.GetFollowersCountObserver observer) {
        super(observer,"Failed to get followers count because of exception: ");
        setActionKey(GET_FOLLOWERS_COUNT_ACTION_KEY);
    }

    @Override
    protected void handleSuccessMessage(ServiceObserver observer, Bundle data) {
        FollowService.GetFollowersCountObserver myObserver = (FollowService.GetFollowersCountObserver)observer;

        int count = data.getInt(GetFollowersCountTask.COUNT_KEY);
        AuthToken authToken = (AuthToken) data.getSerializable(LoginTask.AUTH_TOKEN_KEY);
        if (authToken != null){
            Cache.getInstance().setCurrUserAuthToken(authToken);
        }

        myObserver.getFollowersCountSucceeded(count);
    }
}
