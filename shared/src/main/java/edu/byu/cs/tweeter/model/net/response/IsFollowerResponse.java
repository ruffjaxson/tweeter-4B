package edu.byu.cs.tweeter.model.net.response;

import java.util.Objects;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;

/**
 * A response for a {@link IsFollowerRequest}.
 */
public class IsFollowerResponse extends Response {

    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code.
     */
    private IsFollowerResponse() {
        super(false, null);
    }

    private boolean follower;
    private AuthToken authToken;

    /**
     * Creates a response indicating that the corresponding request was unsuccessful.
     *
     * @param message a message describing why the request was unsuccessful.
     */
    public IsFollowerResponse(String message) {
        super(false, message);
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    @Override
    public String toString() {
        return "IsFollowerResponse{" +
                "follower=" + follower +
                ", authToken=" + authToken +
                '}';
    }

    /**
     * Creates a response indicating that the corresponding request was successful.
     *
     */
    public IsFollowerResponse(Boolean isFollower, AuthToken authToken) {
        super(true, null);
        this.follower = isFollower;
        this.authToken = authToken;
    }

    public boolean isFollower() {
        return follower;
    }

    public void setFollower(boolean follower) {
        follower = follower;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IsFollowerResponse that = (IsFollowerResponse) o;
        return follower == that.follower;
    }

    @Override
    public int hashCode() {
        return Objects.hash(follower);
    }

}
