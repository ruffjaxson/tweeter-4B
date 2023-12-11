package edu.byu.cs.tweeter.client.model.services.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.services.FollowService;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.model.services.observer.ServiceObserver;

public class IsFollowerHandler extends BackgroundTaskHandler {
    public static String IS_FOLLOWER_ACTION_KEY = "IS_FOLLOWER_ACTION_KEY";
    public IsFollowerHandler(FollowService.IsFollowerObserver observer) {
        super(observer,"Failed to get is follower because of exception: ");
        setActionKey(IS_FOLLOWER_ACTION_KEY);
    }

    @Override
    protected void handleSuccessMessage(ServiceObserver observer, Bundle data) {
        FollowService.IsFollowerObserver myObserver = (FollowService.IsFollowerObserver)observer;

        boolean isFollower = data.getBoolean(IsFollowerTask.IS_FOLLOWER_KEY);
        myObserver.isFollowerSucceeded(isFollower);
    }
}
