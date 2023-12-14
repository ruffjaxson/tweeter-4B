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
import edu.byu.cs.tweeter.server.dao.dynamodb.DDBFeedDAO;
import edu.byu.cs.tweeter.server.dao.dynamodb.DDBFollowDAO;
import edu.byu.cs.tweeter.server.dao.dynamodb.DDBStatusDAO;
import edu.byu.cs.tweeter.server.dao.dynamodb.DDBUserDAO;
import edu.byu.cs.tweeter.server.service.FollowService;
import edu.byu.cs.tweeter.server.service.StatusService;

public class FollowFetcher implements RequestHandler<SQSEvent, Void> {
    private FollowService service = new FollowService(new DDBFollowDAO(), new DDBUserDAO(), new DDBAuthTokenDAO());
    private StatusService statusService = new StatusService(new DDBStatusDAO(), new DDBAuthTokenDAO(), new DDBFollowDAO(), new DDBFeedDAO());


    @Override
    public Void handleRequest(SQSEvent event, Context context) {
        for (SQSEvent.SQSMessage msg : event.getRecords()) {
            PostStatusRequest postStatusRequest = JsonSerializer.deserialize(msg.getBody(), PostStatusRequest.class);
            System.out.println("---- FollowFetcher received request: " + postStatusRequest.toString());
            assert postStatusRequest.getStatus() != null;
            statusService.postStatus(postStatusRequest);

            User user = postStatusRequest.getStatus().getUser();
            AuthToken authToken = postStatusRequest.getAuthToken();

            boolean hasMorePages = true;

            int followersCount = 0;
            int counter = 0;
            User lastFollower = null;

            while (hasMorePages) {
                PagedRequest<User> followersRequest = new PagedRequest<>(authToken, user, 200, lastFollower);
                PagedResponse<User> response = service.getFollowers(followersRequest);
                List<User> followers = response.getItems();
                lastFollower = (followers.size() > 0) ? followers.get(followers.size() - 1) : null;

                if (followers.size() > 0) {
                    followersCount += followers.size();
                    System.out.println("FollowFetcher: " + followersCount + " - " + lastFollower.getAlias());
                    UpdateFeedsRequest updateFeedsRequest = new UpdateFeedsRequest(followers, postStatusRequest.getStatus());
                    SqsClient.addMessageToQueue(updateFeedsRequest, SqsClient.JOBS_Q);
                } else {
                    System.out.println("Didn't find any followers for " + user.getAlias());
                }

                hasMorePages = response.getHasMorePages();
            }
        }
        return null;
    }

}