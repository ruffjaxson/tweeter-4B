package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.response.GetUsersResponse;
import edu.byu.cs.tweeter.model.net.response.PagedResponse;
import edu.byu.cs.tweeter.server.dao.dynamodb.DDBAuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.dynamodb.DDBFollowDAO;
import edu.byu.cs.tweeter.server.dao.dynamodb.DDBUserDAO;
import edu.byu.cs.tweeter.server.lambda.interfaces.PagedServerHandler;
import edu.byu.cs.tweeter.server.service.FollowService;

/**
 * An AWS lambda function that returns the users a user is following.
 */
public class GetFollowersHandler extends PagedServerHandler<User> implements RequestHandler<PagedRequest<User>, PagedResponse> {

    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followees returned and to return the next set of
     * followees after any that were returned in a previous request.
     *
     * @param request contains the data required to fulfill the request.
     * @param context the lambda context.
     * @return the followees.
     */
    @Override
    public GetUsersResponse handleRequest(PagedRequest<User> request, Context context) {
        validateRequestAndLogReceipt(request);
        FollowService service = new FollowService(new DDBFollowDAO(), new DDBUserDAO(), new DDBAuthTokenDAO());
        GetUsersResponse response = service.getFollowers(request);
        System.out.println("GetFollowersHandler.handleRequest returning response with followers: " + response.getItems());
        return response;
    }

}
