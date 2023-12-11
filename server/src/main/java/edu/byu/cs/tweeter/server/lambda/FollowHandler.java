package edu.byu.cs.tweeter.server.lambda;
// ROUTES CALLS THAT THE CLIENT MAKES TO API GATEWAY TO SERVER.SERVICE FUNCTIONS
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;
import edu.byu.cs.tweeter.server.dao.dynamodb.DDBAuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.dynamodb.DDBFollowDAO;
import edu.byu.cs.tweeter.server.dao.dynamodb.DDBUserDAO;
import edu.byu.cs.tweeter.server.lambda.interfaces.ServerHandler;
import edu.byu.cs.tweeter.server.service.FollowService;

/**
 * An AWS lambda function that logs a user in and returns the user object and an auth code for
 * a successful login.
 */
public class FollowHandler extends ServerHandler<FollowRequest> implements RequestHandler<FollowRequest, FollowResponse> {
    @Override
    public FollowResponse handleRequest(FollowRequest followRequest, Context context) {
        validateRequestAndLogReceipt(followRequest);
        FollowService followService = new FollowService(new DDBFollowDAO(), new DDBUserDAO(), new DDBAuthTokenDAO());
        return followService.follow(followRequest);
    }

    @Override
    public void validateRequest(FollowRequest request) throws RuntimeException {
        if(request.getFollower() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower");
        } else if(request.getFollowee() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a followee");
        } else if(request.getAuthToken() == null){
            throw new RuntimeException("[Bad Request] Missing auth token");
        }
    }
}
