package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.services.observer.ServiceObserver;
import edu.byu.cs.tweeter.model.domain.User;

public interface AuthenticatedObserver extends ServiceObserver {
    void getUserSucceeded(User user);
}
