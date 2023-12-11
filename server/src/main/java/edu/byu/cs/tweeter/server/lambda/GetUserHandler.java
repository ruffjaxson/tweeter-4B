package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.GetUserRequest;
import edu.byu.cs.tweeter.model.net.response.GetUserResponse;
import edu.byu.cs.tweeter.server.dao.dynamodb.DDBAuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.dynamodb.DDBUserDAO;
import edu.byu.cs.tweeter.server.lambda.interfaces.ServerHandler;
import edu.byu.cs.tweeter.server.service.UserService;

/**
 * An AWS lambda function that finds a user and returns information about that user
 */
public class GetUserHandler extends ServerHandler<GetUserRequest> implements RequestHandler<GetUserRequest, GetUserResponse> {
    @Override
    public GetUserResponse handleRequest(GetUserRequest getUserRequest, Context context) {
        validateRequestAndLogReceipt(getUserRequest);
        UserService userService = new UserService(new DDBUserDAO(), new DDBAuthTokenDAO());
        GetUserResponse response = userService.getUser(getUserRequest);
        System.out.println("GetUserHandler.handleRequest is returning a response: " + response.toString());
        return response;
    }

    @Override
    public void validateRequest(GetUserRequest request) throws RuntimeException {
        if(request.getUserAlias() == null){
            throw new RuntimeException("[Bad Request] Missing a username");
        } else if(request.getAuthToken() == null){
            throw new RuntimeException("[Bad Request] Missing auth token");
        }
    }
}