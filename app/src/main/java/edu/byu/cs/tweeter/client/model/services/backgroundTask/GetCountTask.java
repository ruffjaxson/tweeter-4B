package edu.byu.cs.tweeter.client.model.services.backgroundTask;

import static edu.byu.cs.tweeter.client.model.services.backgroundTask.AuthenticateTask.AUTH_TOKEN_KEY;

import android.os.Bundle;
import android.os.Handler;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.response.GetCountResponse;

public abstract class GetCountTask extends AuthenticatedTask {

    public static final String COUNT_KEY = "count";

    /**
     * The user whose count is being retrieved.
     * (This can be any user, not just the currently logged-in user.)
     */
    private final User targetUser;

    private int count;
    private GetCountResponse response;

    protected GetCountTask(AuthToken authToken, User targetUser, Handler messageHandler, String urlPath) {
        super(authToken, messageHandler, urlPath);
        this.targetUser = targetUser;
    }

    protected User getTargetUser() {
        return targetUser;
    }

    @Override
    protected void runTask() throws IOException, TweeterRemoteException {
        GetCountResponse response = runCountTask();
        if (response.isSuccess()) {
            this.count = response.getCount();
            this.response = response;
            sendSuccessMessage();
        } else {
            sendFailedMessage(response.getMessage());
        }
    }

    protected abstract GetCountResponse runCountTask() throws IOException, TweeterRemoteException;

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        msgBundle.putInt(COUNT_KEY, count);
        msgBundle.putSerializable(AUTH_TOKEN_KEY, response.getAuthToken());
    }
}
