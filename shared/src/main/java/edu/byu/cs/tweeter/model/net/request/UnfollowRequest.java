package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class UnfollowRequest {
    private User follower;
    private User followee;
    private AuthToken authToken;

    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code.
     */
    private UnfollowRequest() {}

    public AuthToken getAuthToken() {
        return authToken;
    }

    /**
     * Creates an instance.
     *
     * @param follower the alias of the follower (user that is was following the followee)
     * @param followee the alias of the followee (user that is was followed by the follower)
     */
    public UnfollowRequest(AuthToken authToken, User follower, User followee) {
        this.follower = follower;
        this.followee = followee;
        this.authToken = authToken;
    }

    /**
     * Returns alias/username follower (user that was following the followee)
     *
     * @return the follower (user that was following the followee).
     */
    public User getFollower() {
        return follower;
    }

    /**
     * Sets the follower.
     *
     * @param follower the alias/username of the follower (user that was following the followee)
     */
    public void setFollower(User follower) {
        this.follower = follower;
    }

    /**
     * Returns followee (alias of the user that was followed by the follower)
     *
     * @return the alias of the followee (user that waw followed by the follower).
     */
    public User getFollowee() {
        return followee;
    }

    /**
     * Sets the followee alias.
     *
     * @param followee the alias of the followee (user that was followed by the follower
     */
    public void setFollowee(User followee) {
        this.followee = followee;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    @Override
    public String toString() {
        return "UnfollowRequest{" +
                "follower=" + follower.toString() +
                ", followee=" + followee.toString() +
                ", authToken=" + authToken.toString() +
                '}';
    }
}
