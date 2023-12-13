package edu.byu.cs.tweeter.server.dao.dynamodb.tables;

import java.util.Objects;

import edu.byu.cs.tweeter.model.domain.User;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

@DynamoDbBean
public class Follows extends Table {
    public static final String TABLE_NAME = "follows";
    public static final String INDEX_NAME = "follows_index";
    public static final String PARTITION_KEY = "followerHandle";
    public static final String SORT_KEY = "followeeHandle";

    private String followerHandle;
    private String followerFirstName;
    private String followerLastName;
    private String followerImageUrl;
    private String followeeHandle;
    private String followeeFirstName;
    private String followeeLastName;

    private String followeeImageUrl;


    public Follows() {}

    public Follows(String followerHandle, String followerFirstName, String followerLastName, String followerImageUrl, String followeeHandle, String followeeFirstName, String followeeLastName, String followeeImageUrl) {
        this.followerHandle = followerHandle;
        this.followerFirstName = followerFirstName;
        this.followerLastName = followerLastName;
        this.followerImageUrl = followerImageUrl;
        this.followeeHandle = followeeHandle;
        this.followeeFirstName = followeeFirstName;
        this.followeeLastName = followeeLastName;
        this.followeeImageUrl = followeeImageUrl;
    }

    public Follows(User follower, User followee) {
        this.followerHandle = follower.getAlias();
        this.followerFirstName = follower.getFirstName();
        this.followerLastName = follower.getLastName();
        this.followerImageUrl = follower.getImageUrl();
        this.followeeHandle = followee.getAlias();
        this.followeeFirstName = followee.getFirstName();
        this.followeeLastName = followee.getLastName();
        this.followeeImageUrl = followee.getImageUrl();
    }

    @DynamoDbPartitionKey
    @DynamoDbSecondarySortKey(indexNames = INDEX_NAME)
    public String getFollowerHandle() {
        return followerHandle;
    }

    @DynamoDbSortKey
    @DynamoDbSecondaryPartitionKey(indexNames = INDEX_NAME)
    public String getFolloweeHandle() {
        return followeeHandle;
    }


    public String getFollowerLastName() {
        return followerLastName;
    }

    public void setFollowerLastName(String followerLastName) {
        this.followerLastName = followerLastName;
    }

    public String getFollowerImageUrl() {
        return followerImageUrl;
    }

    public void setFollowerImageUrl(String followerImageUrl) {
        this.followerImageUrl = followerImageUrl;
    }

    public String getFolloweeLastName() {
        return followeeLastName;
    }

    public void setFolloweeLastName(String followeeLastName) {
        this.followeeLastName = followeeLastName;
    }

    public String getFolloweeImageUrl() {
        return followeeImageUrl;
    }

    public void setFolloweeImageUrl(String followeeImageUrl) {
        this.followeeImageUrl = followeeImageUrl;
    }

    public void setFollowerHandle(String follower_handle) {
        this.followerHandle = follower_handle;
    }

    public String getFollowerFirstName() {
        return followerFirstName;
    }

    public void setFollowerFirstName(String followerFirstName) {
        this.followerFirstName = followerFirstName;
    }



    public void setFolloweeHandle(String followeeHandle) {
        this.followeeHandle = followeeHandle;
    }

    public String getFolloweeFirstName() {
        return followeeFirstName;
    }

    public void setFolloweeFirstName(String followeeFirstName) {
        this.followeeFirstName = followeeFirstName;
    }

    @Override
    public String tableName() {
        return TABLE_NAME;
    }

    @Override
    public String partitionKey() {
        return PARTITION_KEY;
    }

    @Override
    public String sortKey() {
        return SORT_KEY;
    }

    @Override
    public String indexTableName() {
        return INDEX_NAME;
    }

    @Override
    public String partitionValue() {
        return getFollowerHandle();
    }

    @Override
    public String sortValue() {
        return getFolloweeHandle();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Follows)) return false;
        Follows follows = (Follows) o;
        return Objects.equals(getFollowerHandle(), follows.getFollowerHandle()) && Objects.equals(getFollowerFirstName(), follows.getFollowerFirstName()) && Objects.equals(getFollowerLastName(), follows.getFollowerLastName()) && Objects.equals(getFollowerImageUrl(), follows.getFollowerImageUrl()) && Objects.equals(getFolloweeHandle(), follows.getFolloweeHandle()) && Objects.equals(getFolloweeFirstName(), follows.getFolloweeFirstName()) && Objects.equals(getFolloweeLastName(), follows.getFolloweeLastName()) && Objects.equals(getFolloweeImageUrl(), follows.getFolloweeImageUrl());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFollowerHandle(), getFollowerFirstName(), getFollowerLastName(), getFollowerImageUrl(), getFolloweeHandle(), getFolloweeFirstName(), getFolloweeLastName(), getFolloweeImageUrl());
    }

    @Override
    public String toString() {
        return "Follows{" +
                "followerHandle='" + followerHandle + '\'' +
                ", followerFirstName='" + followerFirstName + '\'' +
                ", followerLastName='" + followerLastName + '\'' +
                ", followerImageUrl='" + followerImageUrl + '\'' +
                ", followeeHandle='" + followeeHandle + '\'' +
                ", followeeFirstName='" + followeeFirstName + '\'' +
                ", followeeLastName='" + followeeLastName + '\'' +
                ", followeeImageUrl='" + followeeImageUrl + '\'' +
                '}';
    }

    public User getFollower() {
        return new User(followerFirstName, followerLastName, followerHandle, followerImageUrl);
    }

    public User getFollowee() {
        return new User(followeeFirstName, followeeLastName, followeeHandle, followeeImageUrl);
    }
}