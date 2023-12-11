package edu.byu.cs.tweeter.client.model.services.backgroundTask.handler;

import edu.byu.cs.tweeter.client.presenter.PagedObserver;
import edu.byu.cs.tweeter.model.domain.Status;

public class GetStoryHandler extends PagedBackgroundTaskHandler<PagedObserver, Status>{
    public static String GET_STORY_ACTION_KEY = "GET_STORY_ACTION_KEY";
    public GetStoryHandler(PagedObserver observer) {
        super(observer,"Failed to get story because of exception: ", GET_STORY_ACTION_KEY);
        setActionKey(GET_STORY_ACTION_KEY);
    }
}

