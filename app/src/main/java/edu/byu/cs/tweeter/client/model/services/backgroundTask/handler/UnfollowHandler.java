package edu.byu.cs.tweeter.client.model.services.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.services.FollowService;
import edu.byu.cs.tweeter.client.model.services.observer.ServiceObserver;

public class UnfollowHandler extends BackgroundTaskHandler{
    public static String UNFOLLOW_ACTION_KEY = "UNFOLLOW_ACTION_KEY";
    public UnfollowHandler(FollowService.UnfollowObserver observer) {
        super(observer,"Failed to unfollow because of exception: ");
        setActionKey(UNFOLLOW_ACTION_KEY);
    }

    @Override
    protected void handleSuccessMessage(ServiceObserver observer, Bundle data) {
        FollowService.UnfollowObserver myObserver = (FollowService.UnfollowObserver)observer;
        myObserver.handleUnfollowSuccessMessage();
    }
}
