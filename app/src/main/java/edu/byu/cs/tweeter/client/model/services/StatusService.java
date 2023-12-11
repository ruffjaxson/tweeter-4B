package edu.byu.cs.tweeter.client.model.services;


import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.*;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.handler.*;
import edu.byu.cs.tweeter.client.model.services.observer.ServiceObserver;
import edu.byu.cs.tweeter.client.presenter.PagedObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StatusService {

    public static final String FEED_URL_PATH = "/status/feed";
    public static final String POST_STATUS_URL_PATH = "/status";
    public static final String STORY_URL_PATH = "/status/story";


    public void postStatus(Status newStatus, PostStatusObserver observer) {
        PostStatusTask statusTask = new PostStatusTask(
                Cache.getInstance().getCurrUserAuthToken(),
                Cache.getInstance().getCurrUser().getAlias(), newStatus,
                new PostStatusHandler(observer), POST_STATUS_URL_PATH
        );
        BackgroundTaskUtils.runTask(statusTask);
    }

    public interface PostStatusObserver extends ServiceObserver {
        void postStatusSucceeded();
    }

    public void getFeed(AuthToken authToken, User user, int pageSize, Status lastStatus, PagedObserver observer){
        GetFeedTask getFeedTask = new GetFeedTask(authToken, user, pageSize, lastStatus,
                new GetFeedHandler(observer), FEED_URL_PATH);
        BackgroundTaskUtils.runTask(getFeedTask);
    }


    public void getStory(AuthToken authToken, User user, int pageSize, Status lastStatus, PagedObserver observer){
        GetStoryTask getStoryTask = new GetStoryTask(authToken, user, pageSize, lastStatus,
                new GetStoryHandler(observer), STORY_URL_PATH);
        BackgroundTaskUtils.runTask(getStoryTask);
    }



}
