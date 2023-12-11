package edu.byu.cs.tweeter.client.model.services.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.services.UserService;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.services.observer.ServiceObserver;
import edu.byu.cs.tweeter.client.presenter.AuthenticatedObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class GetUserHandler extends BackgroundTaskHandler {
    public static String GET_USER_ACTION_KEY = "GET_USER_ACTION_KEY";
    public GetUserHandler(AuthenticatedObserver observer) {
        super(observer,"Failed to get user because of exception: ");
        setActionKey(GET_USER_ACTION_KEY);
    }

    @Override
    protected void handleSuccessMessage(ServiceObserver observer, Bundle data) {
        AuthenticatedObserver myObserver = (AuthenticatedObserver)observer;

        User user = (User) data.getSerializable(GetUserTask.USER_KEY);
        myObserver.getUserSucceeded(user);
    }
}
