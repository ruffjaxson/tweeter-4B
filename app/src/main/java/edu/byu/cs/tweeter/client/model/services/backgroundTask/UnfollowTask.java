package edu.byu.cs.tweeter.client.model.services.backgroundTask;

import android.os.Handler;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.services.FollowService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;
import edu.byu.cs.tweeter.model.net.response.UnfollowResponse;

/**
 * Background task that establishes a following relationship between two users.
 */
public class UnfollowTask extends AuthenticatedTask {
    /**
     * The user that is being followed.
     */
    private final User followee;
    private final User follower;

    public UnfollowTask(AuthToken authToken, User follower, User followee, Handler messageHandler, String urlPath) {
        super(authToken, messageHandler, urlPath);
        this.followee = followee;
        this.follower = follower;
    }

    @Override
    protected void runTask() throws IOException, TweeterRemoteException {
        UnfollowRequest request = new UnfollowRequest(authToken, follower, followee);
        UnfollowResponse response = getServerFacade().unfollow(request, urlPath);

        if (response.isSuccess()) {
            sendSuccessMessage();
        } else {
            sendFailedMessage(response.getMessage());
        }
    }

}
