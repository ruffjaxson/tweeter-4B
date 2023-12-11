package edu.byu.cs.tweeter.client.model.services.backgroundTask.handler;

import android.os.Bundle;

import java.util.List;

import edu.byu.cs.tweeter.client.model.services.backgroundTask.GetFollowingTask;
import edu.byu.cs.tweeter.client.model.services.observer.ServiceObserver;
import edu.byu.cs.tweeter.client.presenter.PagedObserver;

public class PagedBackgroundTaskHandler<T extends ServiceObserver, I> extends BackgroundTaskHandler{

    public PagedBackgroundTaskHandler(PagedObserver observer, String exceptionMessageToPrepend, String actionKey) {
        super(observer,exceptionMessageToPrepend);
        setActionKey(actionKey);
    }

    @Override
    protected void handleSuccessMessage(ServiceObserver observer, Bundle data) {
        PagedObserver<I> myObserver = (PagedObserver)observer;

        List<I> items = (List<I>) data.getSerializable(GetFollowingTask.ITEMS_KEY);
        boolean hasMorePages = data.getBoolean(GetFollowingTask.MORE_PAGES_KEY);

        myObserver.getItemsSucceeded(items, hasMorePages);
    }
}
