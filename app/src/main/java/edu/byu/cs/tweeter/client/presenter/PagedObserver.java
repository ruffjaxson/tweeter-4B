package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.services.observer.ServiceObserver;

public interface PagedObserver<I> extends AuthenticatedObserver, ServiceObserver {
     void getItemsSucceeded(List<I> items, boolean hasMore);

}
