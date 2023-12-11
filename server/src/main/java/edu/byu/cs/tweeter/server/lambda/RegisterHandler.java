package edu.byu.cs.tweeter.server.lambda;
// ROUTES CALLS THAT THE CLIENT MAKES TO API GATEWAY TO SERVER.SERVICE FUNCTIONS
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;
import edu.byu.cs.tweeter.server.dao.dynamodb.DDBAuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.dynamodb.DDBUserDAO;
import edu.byu.cs.tweeter.server.lambda.interfaces.ServerHandler;
import edu.byu.cs.tweeter.server.service.UserService;

/**
 * An AWS lambda function that registers a new user and returns the user object and an auth code for
 * a successful register/login.
 */
public class RegisterHandler extends ServerHandler<RegisterRequest> implements RequestHandler<RegisterRequest, RegisterResponse> {
    @Override
    public RegisterResponse handleRequest(RegisterRequest registerRequest, Context context) {
        validateRequestAndLogReceipt(registerRequest);
        UserService userService = new UserService(new DDBUserDAO(), new DDBAuthTokenDAO());
        System.out.println("Created userService");
        return userService.register(registerRequest);
    }

    @Override
    public void validateRequest(RegisterRequest request) throws RuntimeException {
        if(request.getUsername() == null){
            throw new RuntimeException("[Bad Request] Missing a username");
        } else if(request.getPassword() == null) {
            throw new RuntimeException("[Bad Request] Missing a password");
        } else if(request.getFirstName() == null) {
            throw new RuntimeException("[Bad Request] Missing a first name");
        } else if(request.getLastName() == null) {
            throw new RuntimeException("[Bad Request] Missing a last name");
        } else if(request.getImage() == null) {
            throw new RuntimeException("[Bad Request] Missing a profile image");
        }
    }
}
