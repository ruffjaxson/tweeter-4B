package edu.byu.cs.tweeter.client.model.services.backgroundTask;

import android.os.Handler;


import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.response.PagedResponse;

/**
 * Background task that retrieves a page of followers.
 */
public class GetFollowersTask extends PagedTask<User> {

    public GetFollowersTask(AuthToken authToken, User targetUser, int limit, User lastFollower,
                            Handler messageHandler, String getFollowersPath) {
        super(authToken, targetUser, limit, lastFollower, messageHandler, getFollowersPath);
    }

    @Override
    protected PagedResponse<User> callMethodOnServer(PagedRequest<User> request) throws TweeterRemoteException, IOException {
        return getServerFacade().getFollowers(request, urlPath);
    }

}
