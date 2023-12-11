package edu.byu.cs.tweeter.client.model.services;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.*;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.handler.*;
import edu.byu.cs.tweeter.client.model.services.observer.ServiceObserver;
import edu.byu.cs.tweeter.client.presenter.PagedObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowService {
    public static final String USER_URL_PATH = "/user";
    public static final String FOLLOW_URL_PATH = "/follow";
    public static final String UNFOLLOW_URL_PATH = "/unfollow";
    public static final String FOLLOWERS_URL_PATH = "/followers";
    public static final String FOLLOWING_URL_PATH = "/following";
    public static final String COUNT_URL_PATH = "/count";
    public static final String IS_FOLLOWER_URL_PATH = "/is-follower";


    public interface GetFollowersCountObserver extends ServiceObserver{
        void getFollowersCountSucceeded(int count);
    }

    public void getFollowersCount(User selectedUser, GetFollowersCountObserver observer) {
        // Get count of most recently selected user's followers.
        GetFollowersCountTask followersCountTask = new GetFollowersCountTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new GetFollowersCountHandler(observer), USER_URL_PATH+ "/" + selectedUser.getAlias() + FOLLOWERS_URL_PATH + COUNT_URL_PATH);
        BackgroundTaskUtils.runTask(followersCountTask);
    }


    public interface GetFollowingCountObserver extends ServiceObserver {
        void getFollowingCountSucceeded(int count);
    }

    public void getFollowingCount(User selectedUser, GetFollowingCountObserver observer) {
        // Get count of most recently selected user's followees (who they are following)
        GetFollowingCountTask followingCountTask = new GetFollowingCountTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new GetFollowingCountHandler(observer), USER_URL_PATH+ "/" + selectedUser.getAlias() + FOLLOWING_URL_PATH + COUNT_URL_PATH);
        BackgroundTaskUtils.runTask(followingCountTask);
    }


    public interface UnfollowObserver extends ServiceObserver {
        void handleUnfollowSuccessMessage();
    }

    public void unfollow(User selectedUser, UnfollowObserver observer) {
        UnfollowTask unfollowTask = new UnfollowTask(Cache.getInstance().getCurrUserAuthToken(), Cache.getInstance().getCurrUser(),
                selectedUser, new UnfollowHandler(observer), USER_URL_PATH + "/" + Cache.getInstance().getCurrUser().getAlias() + UNFOLLOW_URL_PATH + "/" + selectedUser.getAlias());
        BackgroundTaskUtils.runTask(unfollowTask);
    }


    public interface FollowObserver extends ServiceObserver {
        void handleFollowSuccessMessage();
    }

    public void follow(User selectedUser, FollowObserver observer) {
        FollowTask followTask = new FollowTask(Cache.getInstance().getCurrUserAuthToken(), Cache.getInstance().getCurrUser(),
                selectedUser, new FollowHandler(observer), USER_URL_PATH + "/" + Cache.getInstance().getCurrUser().getAlias() + FOLLOW_URL_PATH + "/" + selectedUser.getAlias());
        BackgroundTaskUtils.runTask(followTask);
    }



    public void getFollowers(AuthToken authToken, User user, int pageSize, User lastFollower, PagedObserver observer){
        GetFollowersTask getFollowersTask = new GetFollowersTask(authToken, user, pageSize, lastFollower, new GetFollowersHandler(observer), USER_URL_PATH + "/" + user.getAlias() + FOLLOWERS_URL_PATH);
        BackgroundTaskUtils.runTask(getFollowersTask);
    }


    public void getFollowing(AuthToken authToken, User user, int pageSize,
                             User lastFollowee, PagedObserver observer){
        String path = USER_URL_PATH+ "/" + user.getAlias() + FOLLOWING_URL_PATH;
        GetFollowingTask getFollowingTask = new GetFollowingTask(authToken,
                user, pageSize, lastFollowee, new GetFollowingHandler(observer), path);
        BackgroundTaskUtils.runTask(getFollowingTask);
    }


    public interface IsFollowerObserver extends ServiceObserver{
        void isFollowerSucceeded(boolean isFollower);
    }


    public void isFollower(User selectedUser, IsFollowerObserver observer){
        IsFollowerTask isFollowerTask = new IsFollowerTask(Cache.getInstance().getCurrUserAuthToken(),
                Cache.getInstance().getCurrUser(), selectedUser, new IsFollowerHandler(observer), IS_FOLLOWER_URL_PATH);
        BackgroundTaskUtils.runTask(isFollowerTask);
    }

}
