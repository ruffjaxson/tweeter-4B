package edu.byu.cs.tweeter.client.model.services.backgroundTask;

import android.os.Handler;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.response.PagedResponse;

/**
 * Background task that retrieves a page of other users being followed by a specified user.
 */
public class GetFollowingTask extends PagedTask<User> {

    public GetFollowingTask(AuthToken authToken, User targetUser, int limit, User lastFollowee,
                            Handler messageHandler, String urlPath) {
        super(authToken, targetUser, limit, lastFollowee, messageHandler, urlPath);
    }

    @Override
    protected PagedResponse<User> callMethodOnServer(PagedRequest<User> request) throws TweeterRemoteException, IOException {
        return getServerFacade().getFollowing(request, urlPath);
    }


}
