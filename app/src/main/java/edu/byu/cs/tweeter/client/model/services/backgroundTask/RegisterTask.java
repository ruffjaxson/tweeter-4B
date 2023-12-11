package edu.byu.cs.tweeter.client.model.services.backgroundTask;

import android.os.Bundle;
import android.os.Handler;

import java.io.IOException;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.services.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that creates a new user account and logs in the new user (i.e., starts a session).
 */
public class RegisterTask extends AuthenticateTask {

    /**
     * The user's first name.
     */
    private final String firstName;
    
    /**
     * The user's last name.
     */
    private final String lastName;

    /**
     * The base-64 encoded bytes of the user's profile image.
     */
    private final String image;
    private RegisterResponse successResponse;

    public RegisterTask(String firstName, String lastName, String username, String password,
                        String image, Handler messageHandler, String registerPath) {
        super(messageHandler, username, password, registerPath);
        this.firstName = firstName;
        this.lastName = lastName;
        this.image = image;
    }

    @Override
    protected void runTask() throws IOException, TweeterRemoteException {
        RegisterRequest request = new RegisterRequest(username, password, firstName, lastName, image);
        RegisterResponse response = getServerFacade().register(request, urlPath);

        if (response.isSuccess()) {
            successResponse = response;
            sendSuccessMessage();
        } else {
            sendFailedMessage(response.getMessage());
        }
    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        msgBundle.putSerializable(USER_KEY, successResponse.getUser());
        msgBundle.putSerializable(AUTH_TOKEN_KEY, successResponse.getAuthToken());
    }
}
