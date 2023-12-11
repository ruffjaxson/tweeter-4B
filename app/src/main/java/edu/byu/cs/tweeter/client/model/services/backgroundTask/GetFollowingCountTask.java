package edu.byu.cs.tweeter.client.model.services.backgroundTask;

import android.os.Handler;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.GetFollowersCountRequest;
import edu.byu.cs.tweeter.model.net.request.GetFollowingCountRequest;
import edu.byu.cs.tweeter.model.net.response.GetCountResponse;

/**
 * Background task that queries how many other users a specified user is following.
 */
public class GetFollowingCountTask extends GetCountTask {

    public GetFollowingCountTask(AuthToken authToken, User targetUser, Handler messageHandler, String urlPath) {
        super(authToken, targetUser, messageHandler, urlPath);
    }

    @Override
    protected GetCountResponse runCountTask() throws IOException, TweeterRemoteException {
        GetFollowingCountRequest request = new GetFollowingCountRequest(authToken, getTargetUser());
        return getServerFacade().getFollowingCount(request, urlPath);
    }
}
