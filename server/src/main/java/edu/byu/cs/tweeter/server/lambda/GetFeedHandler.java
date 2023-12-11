package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.response.GetStatusesResponse;
import edu.byu.cs.tweeter.model.net.response.PagedResponse;
import edu.byu.cs.tweeter.server.dao.dynamodb.DDBAuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.dynamodb.DDBFeedDAO;
import edu.byu.cs.tweeter.server.dao.dynamodb.DDBFollowDAO;
import edu.byu.cs.tweeter.server.dao.dynamodb.DDBStatusDAO;
import edu.byu.cs.tweeter.server.lambda.interfaces.PagedServerHandler;
import edu.byu.cs.tweeter.server.service.StatusService;

/**
 * An AWS lambda function that returns the feed for a user specified in the request. The feed is
 *  comprised of the statuses of the followees of the given user.
 */
public class GetFeedHandler extends PagedServerHandler<Status> implements RequestHandler<PagedRequest<Status>, PagedResponse> {
    @Override
    public GetStatusesResponse handleRequest(PagedRequest<Status> pagedRequest, Context context) {
        validateRequestAndLogReceipt(pagedRequest);
        StatusService statusService = new StatusService(new DDBStatusDAO(), new DDBAuthTokenDAO(), new DDBFollowDAO(), new DDBFeedDAO());
        GetStatusesResponse response = statusService.getFeed(pagedRequest);
        System.out.println("GetFeedHandler.handleRequest is returning a response: " + response.toString());
        return response;
    }

}