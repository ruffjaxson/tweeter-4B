package edu.byu.cs.tweeter.client.model.services.backgroundTask;

import static edu.byu.cs.tweeter.client.model.services.backgroundTask.AuthenticateTask.AUTH_TOKEN_KEY;

import android.os.Bundle;
import android.os.Handler;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.GetUserRequest;
import edu.byu.cs.tweeter.model.net.response.GetUserResponse;

/**
 * Background task that returns the profile for a specified user.
 */
public class GetUserTask extends AuthenticatedTask {

    public static final String USER_KEY = "user";

    /**
     * Alias (or handle) for user whose profile is being retrieved.
     */
    private final String alias;

    private User user;
    private GetUserResponse response;

    public GetUserTask(AuthToken authToken, String alias, Handler messageHandler, String urlPath) {
        super(authToken, messageHandler, urlPath);
        this.alias = alias;
    }

    @Override
    protected void runTask() throws IOException, TweeterRemoteException {
        GetUserRequest request = new GetUserRequest(authToken, alias);
        GetUserResponse response = getServerFacade().getUser(request, urlPath);

        if (response.isSuccess()) {
            this.user = response.getUser();
            this.response = response;
            sendSuccessMessage();
        } else {
            sendFailedMessage(response.getMessage());
        }
    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        msgBundle.putSerializable(USER_KEY, user);
        msgBundle.putSerializable(AUTH_TOKEN_KEY, response.getAuthToken());
    }
}
