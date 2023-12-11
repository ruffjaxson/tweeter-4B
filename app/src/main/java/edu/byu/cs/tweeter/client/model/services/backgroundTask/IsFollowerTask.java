package edu.byu.cs.tweeter.client.model.services.backgroundTask;

import static edu.byu.cs.tweeter.client.model.services.backgroundTask.AuthenticateTask.AUTH_TOKEN_KEY;

import android.os.Bundle;
import android.os.Handler;

import java.io.IOException;
import java.util.Random;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;

/**
 * Background task that determines if one user is following another.
 */
public class IsFollowerTask extends AuthenticatedTask {

    public static final String IS_FOLLOWER_KEY = "is-follower";

    /**
     * The alleged follower.
     */
    private final User follower;

    /**
     * The alleged followee.
     */
    private final User followee;

    private boolean isFollower;
    private IsFollowerResponse response;

    public IsFollowerTask(AuthToken authToken, User follower, User followee, Handler messageHandler, String urlPath) {
        super(authToken, messageHandler, urlPath);
        this.follower = follower;
        this.followee = followee;
    }

    @Override
    protected final void runTask() throws IOException, TweeterRemoteException {
        IsFollowerRequest request = new IsFollowerRequest(authToken, follower, followee);
        IsFollowerResponse response = getServerFacade().isFollower(request, urlPath);
        if (response.isSuccess()) {
            isFollower = response.isFollower();
            this.response = response;
            sendSuccessMessage();
        } else {
            sendFailedMessage(response.getMessage());
        }
    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        msgBundle.putBoolean(IS_FOLLOWER_KEY, isFollower);
        msgBundle.putSerializable(AUTH_TOKEN_KEY, response.getAuthToken());

    }

}
