package edu.byu.cs.tweeter.client.model.services.backgroundTask;

import static edu.byu.cs.tweeter.client.model.services.backgroundTask.AuthenticateTask.AUTH_TOKEN_KEY;

import android.os.Bundle;
import android.os.Handler;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;

/**
 * Background task that logs out a user (i.e., ends a session).
 */
public class LogoutTask extends AuthenticatedTask {

    private LogoutResponse response;

    public LogoutTask(AuthToken authToken, Handler messageHandler, String urlPath) {
        super(authToken, messageHandler, urlPath);
    }

    @Override
    protected final void runTask() throws IOException, TweeterRemoteException {
        LogoutRequest request = new LogoutRequest(authToken);
        LogoutResponse response = getServerFacade().logout(request, urlPath);
        if (response.isSuccess()) {
            this.response = response;
            sendSuccessMessage();
        } else {
            sendFailedMessage(response.getMessage());
        }
    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {}
}
