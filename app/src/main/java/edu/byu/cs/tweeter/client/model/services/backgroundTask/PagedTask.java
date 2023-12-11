package edu.byu.cs.tweeter.client.model.services.backgroundTask;

import android.os.Bundle;
import android.os.Handler;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.response.PagedResponse;
import edu.byu.cs.tweeter.util.Pair;

public abstract class PagedTask<T> extends AuthenticatedTask {

    public static final String ITEMS_KEY = "items";
    public static final String MORE_PAGES_KEY = "more-pages";

    /**
     * The user whose items are being retrieved.
     * (This can be any user, not just the currently logged-in user.)
     */
    protected final User targetUser;

    /**
     * Maximum number of statuses to return (i.e., page size).
     */

    protected final int limit;

    /**
     * The last status returned in the previous page of results (can be null).
     * This allows the new page to begin where the previous page ended.
     */
    protected final T lastItem;

    /**
     * The items returned in the current page of results.
     */
    private List<T> items;

    /**
     * Indicates whether there are more pages of items that can be retrieved on subsequent calls.
     */
    private boolean hasMorePages;

    protected PagedTask(AuthToken authToken, User targetUser, int limit, T lastItem, Handler messageHandler, String urlPath) {
        super(authToken, messageHandler, urlPath);
        this.targetUser = targetUser;
        this.limit = limit;
        this.lastItem = lastItem;
    }

    protected User getTargetUser() {
        return targetUser;
    }

    protected int getLimit() {
        return limit;
    }

    protected T getLastItem() {
        return lastItem;
    }

    @Override
    protected final void runTask() throws IOException, TweeterRemoteException {
        PagedRequest<T> request = new PagedRequest(authToken, targetUser, limit, lastItem);
        PagedResponse<T> response = callMethodOnServer(request);

        if (response.isSuccess()) {
            this.items = response.getItems();
            this.hasMorePages = response.getHasMorePages();
            sendSuccessMessage();
        } else {
            sendFailedMessage(response.getMessage());
        }
    }

    protected abstract PagedResponse<T> callMethodOnServer(PagedRequest<T> request) throws TweeterRemoteException, IOException;

    @Override
    protected final void loadSuccessBundle(Bundle msgBundle) {
        msgBundle.putSerializable(ITEMS_KEY, (Serializable) items);
        msgBundle.putBoolean(MORE_PAGES_KEY, hasMorePages);
    }
}
