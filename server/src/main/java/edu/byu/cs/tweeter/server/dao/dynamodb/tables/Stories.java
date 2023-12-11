package edu.byu.cs.tweeter.server.dao.dynamodb.tables;

import java.time.Instant;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
public class Stories extends Table {
    public static final String TABLE_NAME = "stories";
    public static final String PARTITION_KEY = "userAlias";
    public static final String SORT_KEY = "timestamp";

    public Stories() {}

    public Stories(Status s) {
        this.userAlias = s.getUser().getAlias();
        this.postText = s.getPost();
        this.timestamp = s.getTimestamp();
        this.timestampString = Instant.ofEpochSecond(s.getTimestamp()).toString();
        this.urls = s.getUrls();
        this.mentionedAliases = s.getMentions();
    }

    String userAlias; // primary partition key
    public String postText;
    public Long timestamp; // primary sort key
    public String timestampString;
    public List<String> urls;
    public List<String> mentionedAliases;

    @DynamoDbPartitionKey
    public String getUserAlias() {
        return userAlias;
    }

    public void setUserAlias(String userAlias) {
        this.userAlias = userAlias;
    }

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }

    @DynamoDbSortKey
    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getTimestampString() {
        return timestampString;
    }

    public void setTimestampString(String timestampString) {
        this.timestampString = timestampString;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public List<String> getMentionedAliases() {
        return mentionedAliases;
    }

    public void setMentionedAliases(List<String> mentionedAliases) {
        this.mentionedAliases = mentionedAliases;
    }

    public Status convert(User storyUser){
        return new Status( getPostText(), storyUser, getTimestamp().longValue(), getUrls(), getMentionedAliases() );
    }

    @Override
    public String tableName() {
        return TABLE_NAME;
    }

    @Override
    public String indexTableName() {
        return null;
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
    public String partitionValue() {
        return getUserAlias();
    }

    @Override
    public Object sortValue() {
        return getTimestamp();
    }
}
