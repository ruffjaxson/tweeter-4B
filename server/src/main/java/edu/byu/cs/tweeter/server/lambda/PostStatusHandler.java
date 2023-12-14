package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

// ROUTES CALLS THAT THE CLIENT MAKES TO API GATEWAY TO SERVER.SERVICE FUNCTIONS
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.server.dao.dynamodb.DDBAuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.dynamodb.DDBFeedDAO;
import edu.byu.cs.tweeter.server.dao.dynamodb.DDBFollowDAO;
import edu.byu.cs.tweeter.server.dao.dynamodb.DDBStatusDAO;
import edu.byu.cs.tweeter.server.lambda.interfaces.ServerHandler;
import edu.byu.cs.tweeter.server.service.StatusService;
import edu.byu.cs.tweeter.server.sqs.JsonSerializer;
import edu.byu.cs.tweeter.server.sqs.SqsClient;

/**
 * An AWS lambda function that posts a status to a user returns  the user object and an auth code for
 * a successful login.
 */
public class PostStatusHandler extends ServerHandler<PostStatusRequest> implements RequestHandler<PostStatusRequest, PostStatusResponse> {
    @Override
    public PostStatusResponse handleRequest(PostStatusRequest postStatusRequest, Context context) {
        validateRequestAndLogReceipt(postStatusRequest);
        SqsClient.addMessageToQueue(postStatusRequest, SqsClient.POSTS_Q);
//        StatusService statusService = new StatusService(new DDBStatusDAO(), new DDBAuthTokenDAO(), new DDBFollowDAO(), new DDBFeedDAO());
        return new PostStatusResponse();
    }

    @Override
    public void validateRequest(PostStatusRequest request) throws RuntimeException {
        if(request.getUserAlias() == null) {
            throw new RuntimeException("[Bad Request] Missing a alias/username");
        } else if(request.getStatus() == null){
            throw new RuntimeException("[Bad Request] Missing the status");
        } else if(request.getAuthToken() == null){
            throw new RuntimeException("[Bad Request] Missing auth token");
        }
    }
}
