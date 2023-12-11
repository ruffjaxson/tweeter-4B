package edu.byu.cs.tweeter.server.dao.dynamodb.tables;

import java.time.Instant;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
public class Feeds extends Table {
    public static final String TABLE_NAME = "feeds";
    public static final String PARTITION_KEY = "feedOwnerAlias";
    public static final String SORT_KEY = "sortingHash";

    public Feeds() {}

    private String feedOwnerAlias; // primary partition key
    private String postText;
    private Long timestamp; // primary sort key
    private String timestampString;
    private List<String> urls;
    private List<String> mentionedAliases;
    private String postUserAlias;
    private String postUserFirstName;
    private String postUserLastName;
    private String postUserImageURL;
    private String sortingHash;



    public Feeds(String feedOwnerAlias, Status s) {
        User poster = s.getUser();
        this.feedOwnerAlias = feedOwnerAlias;


        this.postText = s.getPost();
        this.timestamp = s.getTimestamp();
        this.timestampString = Instant.ofEpochSecond(s.getTimestamp()).toString();
        this.urls = s.getUrls();
        this.mentionedAliases = s.getMentions();
        this.sortingHash = timestamp.toString() + postUserAlias + ": " + postText;
        this.postUserAlias = poster.getAlias();
        this.postUserFirstName = poster.getFirstName();
        this.postUserLastName = poster.getLastName();
        this.postUserImageURL = poster.getImageUrl();
    }



    @DynamoDbPartitionKey
    public String getFeedOwnerAlias() {
        return feedOwnerAlias;
    }

    public void setFeedOwnerAlias(String feedOwnerAlias) {
        this.feedOwnerAlias = feedOwnerAlias;
    }

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }

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

    public String getPostUserAlias() {
        return postUserAlias;
    }

    public void setPostUserAlias(String postUserAlias) {
        this.postUserAlias = postUserAlias;
    }

    public String getPostUserFirstName() {
        return postUserFirstName;
    }

    public void setPostUserFirstName(String postUserFirstName) {
        this.postUserFirstName = postUserFirstName;
    }

    public String getPostUserLastName() {
        return postUserLastName;
    }

    public void setPostUserLastName(String postUserLastName) {
        this.postUserLastName = postUserLastName;
    }

    public String getPostUserImageURL() {
        return postUserImageURL;
    }

    public void setPostUserImageURL(String postUserImageURL) {
        this.postUserImageURL = postUserImageURL;
    }

    @DynamoDbSortKey
    public String getSortingHash() {
        return sortingHash;
    }

    public void setSortingHash(String sortingHash) {
        this.sortingHash = sortingHash;
    }

    public Status convert(){
        User u = new User(getPostUserFirstName(), getPostUserLastName(), getPostUserAlias(), getPostUserImageURL());
        return new Status( getPostText(), u, getTimestamp(), getUrls(), getMentionedAliases() );
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
        return getFeedOwnerAlias();
    }

    @Override
    public Object sortValue() {
        return getSortingHash();
    }
}
