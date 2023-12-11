package edu.byu.cs.tweeter.client.model.services.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.services.FollowService;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.model.services.observer.ServiceObserver;

public class GetFollowingCountHandler extends BackgroundTaskHandler {
    public static String GET_FOLLOWING_COUNT_ACTION_KEY = "GET_FOLLOWING_COUNT_ACTION_KEY";
    public GetFollowingCountHandler(FollowService.GetFollowingCountObserver observer) {
        super(observer,"Failed to get following count because of exception: ");
        setActionKey(GET_FOLLOWING_COUNT_ACTION_KEY);
    }

    @Override
    protected void handleSuccessMessage(ServiceObserver observer, Bundle data) {
        FollowService.GetFollowingCountObserver myObserver = (FollowService.GetFollowingCountObserver)observer;

        int count = data.getInt(GetFollowersCountTask.COUNT_KEY);
        myObserver.getFollowingCountSucceeded(count);
    }
}
