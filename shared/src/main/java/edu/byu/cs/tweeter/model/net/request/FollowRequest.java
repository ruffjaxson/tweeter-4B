package edu.byu.cs.tweeter.model.net.request;

import java.util.Objects;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowRequest {
    private User follower;
    private User followee;
    private AuthToken authToken;

    private FollowRequest() {}

    public FollowRequest(AuthToken authToken, User follower, User followee) {
        this.follower = follower;
        this.followee = followee;
        this.authToken = authToken;
    }

    public User getFollower() {
        return follower;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setFollower(User follower) {
        this.follower = follower;
    }


    public User getFollowee() {
        return followee;
    }

    public void setFollowee(User followee) {
        this.followee = followee;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    @Override
    public String toString() {
        return "FollowRequest{" +
                "authToken=" + authToken.toString() +
                "follower=" + follower.toString() +
                ", followee=" + followee.toString() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FollowRequest)) return false;
        FollowRequest that = (FollowRequest) o;
        return getFollower().equals(that.getFollower()) && getFollowee().equals(that.getFollowee()) && getAuthToken().equals(that.getAuthToken());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFollower(), getFollowee(), getAuthToken());
    }
}
