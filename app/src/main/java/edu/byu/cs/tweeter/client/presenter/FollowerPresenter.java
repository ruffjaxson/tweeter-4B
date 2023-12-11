package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.services.FollowService;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowerPresenter extends PagedPresenter<User, PagedPresenter.PagedView<User>> implements AuthenticatedObserver {
    private static final int PAGE_SIZE = 10;

    public FollowerPresenter(PagedPresenter.PagedView<User> view, User user) {
        super(view, user);
        setObserver(this);
    }

    protected void callServiceClassToGetItems(){
        var followService = new FollowService();
        followService.getFollowers(Cache.getInstance().getCurrUserAuthToken(), user,
                PAGE_SIZE, lastItem, this);
    }

}
