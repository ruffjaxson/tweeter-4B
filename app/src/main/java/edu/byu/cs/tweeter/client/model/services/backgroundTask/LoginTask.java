package edu.byu.cs.tweeter.client.model.services.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.services.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that logs in a user (i.e., starts a session).
 */
public class LoginTask extends AuthenticateTask {

    private LoginResponse successResponse;

    public LoginTask(String username, String password, Handler messageHandler, String loginPath) {
        super(messageHandler, username, password, loginPath);
    }

    @Override
    protected final void runTask() throws IOException, TweeterRemoteException {
        LoginRequest request = new LoginRequest(username, password);
        LoginResponse response = getServerFacade().login(request, urlPath);
        System.out.println("Response:" + response.toString());
        if (response.isSuccess()) {
            this.successResponse = response;
            Cache.getInstance().setCurrUserAuthToken(response.getAuthToken());
            sendSuccessMessage();
        } else {
            sendFailedMessage(response.getMessage());
        }
//        User loggedInUser = getFakeData().getFirstUser();
//        AuthToken authToken = getFakeData().getAuthToken();
//        return new Pair<>(loggedInUser, authToken);
    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        msgBundle.putSerializable(USER_KEY, successResponse.getUser());
        msgBundle.putSerializable(AUTH_TOKEN_KEY, successResponse.getAuthToken());
    }
}
