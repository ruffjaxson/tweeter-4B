package edu.byu.cs.tweeter.client.model.services.backgroundTask.handler;

import edu.byu.cs.tweeter.client.presenter.PagedObserver;
import edu.byu.cs.tweeter.model.domain.Follow;

public class GetFollowersHandler extends PagedBackgroundTaskHandler<PagedObserver, Follow>{
    public static String GET_FOLLOWERS_ACTION_KEY = "GET_FOLLOWERS_ACTION_KEY";
    public GetFollowersHandler(PagedObserver observer) {
        super(observer,"Failed to get followers because of exception: ", GET_FOLLOWERS_ACTION_KEY);
        setActionKey(GET_FOLLOWERS_ACTION_KEY);
    }

}
