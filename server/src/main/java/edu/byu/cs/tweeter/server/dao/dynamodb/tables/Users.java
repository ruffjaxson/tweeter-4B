package edu.byu.cs.tweeter.server.dao.dynamodb.tables;

import edu.byu.cs.tweeter.model.domain.User;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class Users extends Table {

    public Users() {}

    public static final String TABLE_NAME = "users";
    public static final String PARTITION_KEY = "alias";

    private String alias; // primary partition key
    private String firstName;
    private String lastName;
    private String passwordHash;
    private String imageURL;
    private int followersCount;
    private int followingCount;

    @DynamoDbPartitionKey
    public String getAlias() {
        return alias;
    }

    public Users(String alias, String firstName, String lastName, String passwordHash, String imageURL, int followersCount, int followingCount) {
        this.alias = alias;
        this.firstName = firstName;
        this.lastName = lastName;
        this.passwordHash = passwordHash;
        this.imageURL = imageURL;
        this.followersCount = followersCount;
        this.followingCount = followingCount;
    }

    public void updateFollowerCountByAmount(int amount){
        this.followersCount += amount;
        if (this.followersCount < 0) {
            this.followersCount = 0;
        }
        System.out.println("new follower count: " + this.followersCount);
    }

    public void updateFolloweeCountByAmount(int amount){
        this.followingCount += amount;
        if (this.followingCount < 0) {
            this.followingCount = 0;
        }
        System.out.println("new followee count: " + this.followingCount);
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
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
        return null;
    }

    @Override
    public String indexTableName() {
        return null;
    }

    @Override
    public String partitionValue() {
        return getAlias();
    }

    @Override
    public String sortValue() {
        return null;
    }

    public User convertToUser(){
        return new User(getFirstName(), getLastName(), getAlias(), getImageURL());
    }
}
