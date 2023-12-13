package edu.byu.cs.tweeter.server.sqs;

import java.util.List;
import java.util.Objects;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class UpdateFeedsRequest {
    private List<User> followers;
    private Status status;

    public UpdateFeedsRequest() {}


    public UpdateFeedsRequest(List<User> followers, Status status) {
        this.followers = followers;
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UpdateFeedsRequest)) return false;
        UpdateFeedsRequest that = (UpdateFeedsRequest) o;
        return getFollowers().equals(that.getFollowers()) && getStatus().equals(that.getStatus());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFollowers(), getStatus());
    }

    public List<User> getFollowers() {
        return followers;
    }

    public void setFollowers(List<User> followers) {
        this.followers = followers;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }


    @Override
    public String toString() {
        return "UpdateFeedsRequest{" +
                "followers=" + followers +
                ", status=" + status +
                '}';
    }
}
