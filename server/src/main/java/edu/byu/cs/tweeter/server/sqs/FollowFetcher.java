package edu.byu.cs.tweeter.server.sqs;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.PagedResponse;
import edu.byu.cs.tweeter.server.dao.dynamodb.DDBAuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.dynamodb.DDBFollowDAO;
import edu.byu.cs.tweeter.server.dao.dynamodb.DDBUserDAO;
import edu.byu.cs.tweeter.server.service.FollowService;

public class FollowFetcher implements RequestHandler<SQSEvent, Void> {
    private FollowService service = new FollowService(new DDBFollowDAO(), new DDBUserDAO(), new DDBAuthTokenDAO());

    @Override
    public Void handleRequest(SQSEvent event, Context context) {
        for (SQSEvent.SQSMessage msg : event.getRecords()) {
            PostStatusRequest postStatusRequest = JsonSerializer.deserialize(msg.getBody(), PostStatusRequest.class);
            System.out.println("---- FollowFetcher received request: " + postStatusRequest.toString());
            assert postStatusRequest.getStatus() != null;

            User user = postStatusRequest.getStatus().getUser();
            AuthToken authToken = postStatusRequest.getAuthToken();

            boolean hasMorePages = true;
            User lastUser = null;
            PagedRequest<User> followersRequest;
            PagedResponse<User> response;
            List<User> users;
            UpdateFeedsRequest updateFeedsRequest;
            int count = 0;

            while (hasMorePages) {
                System.out.println("Iteration in while statement: " + count);
                followersRequest = new PagedRequest<>(authToken, user, 25, lastUser);
                response = service.getFollowers(followersRequest);
                users = response.getItems();
                lastUser = (users.size() > 0) ? users.get(users.size() - 1) : null;

                if (users.size() > 0) {
                    System.out.println("We've got " + users.size() + " followers to work on. Adding req to JobsQ");
                    updateFeedsRequest = new UpdateFeedsRequest(users, postStatusRequest.getStatus());
                    SqsClient.addMessageToQueue(updateFeedsRequest, SqsClient.JOBS_Q);
                } else {
                    System.out.println("Didn't find any followers for " + user.getAlias());
                }

                hasMorePages = response.getHasMorePages();
                count++;
            }
        }
        return null;
    }

}