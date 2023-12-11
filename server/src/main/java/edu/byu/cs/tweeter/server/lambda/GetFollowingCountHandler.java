package edu.byu.cs.tweeter.server.lambda;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.GetFollowingCountRequest;
import edu.byu.cs.tweeter.model.net.response.GetCountResponse;
import edu.byu.cs.tweeter.server.dao.dynamodb.DDBAuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.dynamodb.DDBUserDAO;
import edu.byu.cs.tweeter.server.lambda.interfaces.ServerHandler;
import edu.byu.cs.tweeter.server.service.UserService;

/**
 * An AWS lambda function that returns the users a user is following.
 */
public class GetFollowingCountHandler extends ServerHandler<GetFollowingCountRequest> implements RequestHandler<GetFollowingCountRequest, GetCountResponse> {

    /**
     * Returns the number of followees of the given follower (users that are being followed by
     * the user given in the request)
     *
     * @param request contains the data required to fulfill the request.
     * @param context the lambda context.
     * @return the number of followees.
     */
    @Override
    public GetCountResponse handleRequest(GetFollowingCountRequest request, Context context) {
        validateRequestAndLogReceipt(request);
        UserService service = new UserService(new DDBUserDAO(), new DDBAuthTokenDAO());
        GetCountResponse response = service.getFollowingCount(request);
        System.out.println("GetFollowingCountHandler.handleRequest returning response: " + response.toString());
        return response;
    }

    @Override
    public void validateRequest(GetFollowingCountRequest request) throws RuntimeException {
        if(request.getFollower() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower");
        } else if(request.getAuthToken() == null){
            throw new RuntimeException("[Bad Request] Missing auth token");
        }
    }
}
