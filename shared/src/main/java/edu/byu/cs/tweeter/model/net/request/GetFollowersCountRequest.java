package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;


/**
 * Contains all the information needed to make a request to have the server return the number of
 * followees for a specified follower.
 */
public class GetFollowersCountRequest {

    private AuthToken authToken;
    private User followee;

    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code.
     */
    private GetFollowersCountRequest() {}

    /**
     * Creates an instance.
     *
     * @param followee the alias of the user whose followers are being counted.
     */
    public GetFollowersCountRequest(AuthToken authToken, User followee) {
        this.authToken = authToken;
        this.followee = followee;
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
     * Returns the followee whose followers are to be counted by this request.
     *
     * @return the followee.
     */
    public User getFollowee() {
        return followee;
    }

    /**
     * Sets the followee.
     *
     * @param followee the followee.
     */
    public void setFollowee(User followee) {
        this.followee = followee;
    }

    @Override
    public String toString() {
        return "GetFollowingCountRequest{" +
                "authToken=" + authToken.toString() +
                ", followee='" + followee.toString() + '\'' +
                '}';
    }
}
