package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.server.dao.dynamodb.DDBAuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.dynamodb.DDBFollowDAO;
import edu.byu.cs.tweeter.server.dao.dynamodb.DDBUserDAO;
import edu.byu.cs.tweeter.server.lambda.interfaces.ServerHandler;
import edu.byu.cs.tweeter.server.service.FollowService;

public class IsFollowerHandler extends ServerHandler<IsFollowerRequest> implements RequestHandler<IsFollowerRequest, IsFollowerResponse> {
    @Override
    public IsFollowerResponse handleRequest(IsFollowerRequest isFollowerRequest, Context context) {
        validateRequestAndLogReceipt(isFollowerRequest);
        FollowService followService = new FollowService(new DDBFollowDAO(), new DDBUserDAO(), new DDBAuthTokenDAO());
        System.out.println("Right before calling followService.isFollower");

        IsFollowerResponse response = followService.isFollower(isFollowerRequest);
        System.out.println("IsFollowerHandler.handleRequest is returning a response: " + response.toString());
        return response;
    }

    @Override
    public void validateRequest(IsFollowerRequest request) throws RuntimeException {
        if(request.getFollower() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower");
        } else if(request.getFollowee() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a followee");
        } else if(request.getAuthToken() == null){
            throw new RuntimeException("[Bad Request] Missing auth token");
        }

    }
}
