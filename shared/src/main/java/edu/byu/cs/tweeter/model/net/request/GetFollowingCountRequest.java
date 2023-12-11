package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Contains all the information needed to make a request to have the server return the number of
 * followees for a specified follower.
 */
public class GetFollowingCountRequest {

    private AuthToken authToken;
    private User follower;

    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code.
     */
    private GetFollowingCountRequest() {}

    /**
     * Creates an instance.
     *
     * @param follower the alias of the user whose followees are being counted.
     */
    public GetFollowingCountRequest(AuthToken authToken, User follower) {
        this.authToken = authToken;
        this.follower = follower;
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
     * Returns the follower whose followees are to be counted by this request.
     *
     * @return the follower.
     */
    public User getFollower() {
        return follower;
    }

    /**
     * Sets the follower.
     *
     * @param follower the follower.
     */
    public void setFollower(User follower) {
        this.follower = follower;
    }

    @Override
    public String toString() {
        return "GetFollowingCountRequest{" +
                "authToken=" + authToken.toString() +
                ", follower='" + follower.toString() + '\'' +
                '}';
    }
}
