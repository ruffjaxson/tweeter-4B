package edu.byu.cs.tweeter.client.presenter;


import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.services.StatusService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedPresenter extends PagedPresenter<Status, PagedPresenter.PagedView<Status>> implements AuthenticatedObserver {

    private static final String LOG_TAG = "FeedPresenter";
    private static final int PAGE_SIZE = 10;

    public FeedPresenter(PagedView<Status> view, User user) {
        super(view, user);
        this.user = user;
        setObserver(this);
    }

    protected void callServiceClassToGetItems(){
        var statusService = new StatusService();
        statusService.getFeed(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastItem, this);
    }
}
