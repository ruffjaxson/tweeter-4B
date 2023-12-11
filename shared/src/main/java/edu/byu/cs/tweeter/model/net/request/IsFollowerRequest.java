package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class IsFollowerRequest {
    private AuthToken authToken;
    private User follower;
    private User followee;

    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code.
     */
    private IsFollowerRequest() {}

    /**
     * Creates an instance.
     *
     * @param follower the alias of the follower (user that is going to be following the followee)
     * @param followee the alias of the followee (user that is going to be followed by the follower)
     */
    public IsFollowerRequest(AuthToken authToken, User follower, User followee) {
        this.follower = follower;
        this.followee = followee;
        this.authToken = authToken;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    /**
     * Returns alias/username follower (user that is following the followee)
     *
     * @return the follower (user that is following the followee).
     */
    public User getFollower() {
        return follower;
    }

    /**
     * Sets the follower.
     *
     * @param follower the alias/username of the follower (user that is following the followee)
     */
    public void setFollower(User follower) {
        this.follower = follower;
    }

    /**
     * Returns followee (alias of the user that is being followed by the follower)
     *
     * @return the alias of the followee (user that is being followed by the follower).
     */
    public User getFollowee() {
        return followee;
    }

    /**
     * Sets the followee alias.
     *
     * @param followee the alias of the followee (user that is being followed by the follower
     */
    public void setFollowee(User followee) {
        this.followee = followee;
    }

    @Override
    public String toString() {
        return "IsFollowerRequest{" +
                "follower=" + (follower == null ? "null" : follower.toString()) +
                ", followee=" + (followee  == null ? "null" : followee.toString()) +
                '}';
    }
}
