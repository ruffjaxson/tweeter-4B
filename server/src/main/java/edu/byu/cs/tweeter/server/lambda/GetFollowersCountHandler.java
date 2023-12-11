package edu.byu.cs.tweeter.server.lambda;
// ROUTES CALLS THAT THE CLIENT MAKES TO API GATEWAY TO SERVER.SERVICE FUNCTIONS
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.GetFollowersCountRequest;
import edu.byu.cs.tweeter.model.net.response.GetCountResponse;
import edu.byu.cs.tweeter.server.dao.dynamodb.DDBAuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.dynamodb.DDBUserDAO;
import edu.byu.cs.tweeter.server.lambda.interfaces.ServerHandler;
import edu.byu.cs.tweeter.server.service.UserService;

/**
 * An AWS lambda function that returns the users a user is following.
 */
public class GetFollowersCountHandler extends ServerHandler<GetFollowersCountRequest> implements RequestHandler<GetFollowersCountRequest, GetCountResponse> {

    /**
     * Returns the number of followers of the given follower (users that are being followed by
     * the user given in the request)
     *
     * @param request contains the data required to fulfill the request.
     * @param context the lambda context.
     * @return the number of followers.
     */
    @Override
    public GetCountResponse handleRequest(GetFollowersCountRequest request, Context context) {
        validateRequestAndLogReceipt(request);
        UserService service = new UserService(new DDBUserDAO(), new DDBAuthTokenDAO());
        GetCountResponse response = service.getFollowersCount(request);
        System.out.println("GetFollowingCountHandler.handleRequest returning response: " + response.toString());
        return response;
    }

    @Override
    public void validateRequest(GetFollowersCountRequest request) throws RuntimeException {
        if(request.getFollowee() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a followee");
        } else if(request.getAuthToken() == null){
            throw new RuntimeException("[Bad Request] Missing auth token");
        }
    }
}
