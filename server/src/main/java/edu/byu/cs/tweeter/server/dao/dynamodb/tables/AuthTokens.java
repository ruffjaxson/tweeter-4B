package edu.byu.cs.tweeter.server.dao.dynamodb.tables;

import java.time.Instant;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class AuthTokens extends Table {
    private static final int MINUTES_TO_LIVE = 1;
    public static final int TIME_TO_LIVE = MINUTES_TO_LIVE * 60; // thirty minutes
    public static final int RENEW_TIME = 1800;

    public AuthTokens(String token, Long expiresTimestamp) {
        this.token = token;
        this.timestamp = expiresTimestamp;
    }

    public AuthTokens() {}

    public static final String TABLE_NAME = "authtokens";
    public static final String PARTITION_KEY = "token";

    private String token; // primary partition key
    private Long timestamp;

    @DynamoDbPartitionKey
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    public Long getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Long expiresTimestamp) {
        this.timestamp = expiresTimestamp;
    }
    public boolean isStillValid(){
        return Instant.now().getEpochSecond() < timestamp;
    }
    public void renewTimestamp(){
        this.timestamp += RENEW_TIME;
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
        return getToken();
    }

    @Override
    public String sortValue() {
        return null;
    }

    public AuthToken convert(){
        return new AuthToken(getToken(), getTimestamp());
    }

}
