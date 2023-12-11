package edu.byu.cs.tweeter.model.net.response;

import java.io.Serializable;
import java.util.Objects;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.net.request.GetFollowersCountRequest;

/**
 * A paged response for a {@link GetFollowersCountRequest}.
 */
public class GetCountResponse extends Response {

    private int count;
    private AuthToken authToken;

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    /**
     * Creates a response indicating that the corresponding request was unsuccessful. Sets the
     * success and more pages indicators to false.
     *
     * @param message a message describing why the request was unsuccessful.
     */
    public GetCountResponse(String message) {
        super(false, message);
    }

    /**
     * Creates a response indicating that the corresponding request was successful.
     *
     * @param count    the number of followees (users that the specified user/follower is following).
     */
    public GetCountResponse(int count, AuthToken authToken) {
        super(true);
        this.count = count;
        this.authToken = authToken;

        System.out.println("new GetCountResponse is being constructed with count: " + this.count);

    }
    
    /**
     * Returns the number of followers or followees for user specified by the request.
     *
     * @return the number of followers or followees.
     */
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GetCountResponse that = (GetCountResponse) o;
        return count == that.count;
    }

    @Override
    public int hashCode() {
        return Objects.hash(count);
    }

    @Override
    public String toString() {
        return "GetCountResponse{" +
                "count=" + count +
                ", authToken=" + authToken +
                '}';
    }

    public AuthToken getAuthToken() {
        return authToken;
    }
}
