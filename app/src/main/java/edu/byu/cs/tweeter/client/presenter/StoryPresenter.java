package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.services.StatusService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryPresenter  extends PagedPresenter<Status, PagedPresenter.PagedView<Status>> implements AuthenticatedObserver {

    private static final int PAGE_SIZE = 10;
    public static Class<? extends PagedView<Status>> View;

    public StoryPresenter(PagedView<Status> view, User user) {
        super(view, user);
        setObserver(this);
    }

    protected void callServiceClassToGetItems(){
        var statusService = new StatusService();
        statusService.getStory(
                Cache.getInstance().getCurrUserAuthToken(),
                user,
                PAGE_SIZE,
                lastItem,
                this
        );
    }

}
