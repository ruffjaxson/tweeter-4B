package edu.byu.cs.tweeter.server.sqs;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.dynamodb.DDBAuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.dynamodb.DDBFeedDAO;
import edu.byu.cs.tweeter.server.dao.dynamodb.DDBFollowDAO;
import edu.byu.cs.tweeter.server.dao.dynamodb.DDBStatusDAO;
import edu.byu.cs.tweeter.server.dao.dynamodb.DDBUserDAO;
import edu.byu.cs.tweeter.server.service.FollowService;
import edu.byu.cs.tweeter.server.service.StatusService;

public class JobHandler implements RequestHandler<SQSEvent, Void> {
    private StatusService service = new StatusService(new DDBStatusDAO(), new DDBAuthTokenDAO(), new DDBFollowDAO(), new DDBFeedDAO());


    @Override
    public Void handleRequest(SQSEvent event, Context context) {
        for (SQSEvent.SQSMessage msg : event.getRecords()) {
            UpdateFeedsRequest request = JsonSerializer.deserialize(msg.getBody(), UpdateFeedsRequest.class);
            System.out.println("JobHandler: " + request.getFollowers().size() + " - " + (request.getFollowers().size() > 0 ? request.getFollowers().get(request.getFollowers().size() - 1) : null));

            service.postStatusToFeeds(request.getStatus(), request.getFollowers());
        }
        return null;
    }

}