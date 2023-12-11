package edu.byu.cs.tweeter.client.model.services.backgroundTask.handler;

import edu.byu.cs.tweeter.client.presenter.PagedObserver;
import edu.byu.cs.tweeter.model.domain.Status;

public class GetFeedHandler extends PagedBackgroundTaskHandler<PagedObserver, Status>{
    public static String GET_FEED_ACTION_KEY = "GET_FEED_ACTION_KEY";
    public GetFeedHandler(PagedObserver observer) {
        super(observer,"Failed to get feed because of exception: ", GET_FEED_ACTION_KEY);
        setActionKey(GET_FEED_ACTION_KEY);
    }
}
