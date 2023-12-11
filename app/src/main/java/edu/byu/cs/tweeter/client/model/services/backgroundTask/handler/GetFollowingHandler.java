package edu.byu.cs.tweeter.client.model.services.backgroundTask.handler;

import edu.byu.cs.tweeter.client.presenter.PagedObserver;
import edu.byu.cs.tweeter.model.domain.Follow;

public class GetFollowingHandler extends PagedBackgroundTaskHandler<PagedObserver, Follow>{
    public static String GET_FOLLOWING_ACTION_KEY = "GET_FOLLOWING_ACTION_KEY";
    public GetFollowingHandler(PagedObserver observer) {
        super(observer,"Failed to get following because of exception: ", GET_FOLLOWING_ACTION_KEY);
        setActionKey(GET_FOLLOWING_ACTION_KEY);
    }

}
