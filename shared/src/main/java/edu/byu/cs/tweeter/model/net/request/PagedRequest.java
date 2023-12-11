package edu.byu.cs.tweeter.model.net.request;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Contains all the information needed to make a request to have the server return the next page of
 * followees for a specified follower.
 */
public class PagedRequest<T> {

    private AuthToken authToken;
    private User targetUser;
    private int limit;
    private T lastItem;

    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code.
     */
    private PagedRequest() {}

    /**
     * Creates an instance.
     *
     * @param targetUser the alias of the user whose followers are to be returned.
     * @param limit the maximum number of followers to return.
     * @param lastItem the alias of the last follower that was returned in the previous request (null if
     *                     there was no previous request or if no followers were returned in the
     *                     previous request).
     */
    public PagedRequest(AuthToken authToken, User targetUser, int limit, T lastItem) {
        this.authToken = authToken;
        this.targetUser = targetUser;
        this.limit = limit;
        this.lastItem = lastItem;
    }

    /**
     * Returns the auth token of the user who is making the request.
     *
     * @return the auth token.
     */
    public AuthToken getAuthToken() {
        return authToken;
    }

    /**
     * Sets the auth token.
     *
     * @param authToken the auth token.
     */
    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    /**
     * Returns the follower whose followees are to be returned by this request.
     *
     * @return the follower.
     */
    public User getTargetUser() {
        return targetUser;
    }

    /**
     * Sets the follower.
     *
     * @param targetUser the follower.
     */
    public void settargetUser(User targetUser) {
        this.targetUser = targetUser;
    }

    /**
     * Returns the number representing the maximum number of followees to be returned by this request.
     *
     * @return the limit.
     */
    public int getLimit() {
        return limit;
    }

    /**
     * Sets the limit.
     *
     * @param limit the limit.
     */
    public void setLimit(int limit) {
        this.limit = limit;
    }

    /**
     * Returns the last follower that was returned in the previous request or null if there was no
     * previous request or if no followers were returned in the previous request.
     *
     * @return the last follower.
     */
    public T getlastItem() {
        return lastItem;
    }

    /**
     * Sets the last follower.
     *
     * @param lastItem the last follower.
     */
    public void setlastItem(T lastItem) {
        this.lastItem = lastItem;
    }

    @Override
    public String toString() {
        return "PagedFollowersOrFollowingRequest{" +
                "authToken=" + authToken.toString() +
                ", targetUser='" + targetUser.toString() + '\'' +
                ", limit=" + limit +
                ", lastItem='" + lastItem + '\'' +
                '}';
    }
}
