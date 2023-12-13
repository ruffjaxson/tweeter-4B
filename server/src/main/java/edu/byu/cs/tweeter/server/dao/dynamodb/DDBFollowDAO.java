package edu.byu.cs.tweeter.server.dao.dynamodb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.Follow;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.abstract_classes.FollowDAO;
import edu.byu.cs.tweeter.server.dao.dynamodb.tables.Follows;
import edu.byu.cs.tweeter.server.models.DataPage;
import edu.byu.cs.tweeter.util.FakeData;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbIndex;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class DDBFollowDAO extends DynamoDAO<Follows> implements FollowDAO {

    private List<String> stringList;
    private List<User> followersList;
    private List<User> followeesList;
    private final DynamoDbEnhancedClient enhancedClient = TweeterDynamoDBClient.getClient();


    public DDBFollowDAO() {
        super(Follows.class, Follows.TABLE_NAME, Follows.INDEX_NAME);
        stringList = new ArrayList<>();
        followeesList = new ArrayList<>();
        followersList = new ArrayList<>();
    }

    public boolean isFollower(User follower, User followee) {
        Follows f = find(follower.getAlias(), followee.getAlias());
        if (f == null) {
            return false;
        }
        return true;
    }

    public DataPage<User> getFollowing(User follower, int limit, User lastFollowee) {
        followeesList.clear();
        boolean hasMorePages = getItems(follower.getAlias(), null, limit, lastFollowee == null ? null : new Follows(follower, lastFollowee), false, false, false);
        return new DataPage<>(followeesList, hasMorePages);
    }

    public DataPage<User> getFollowers(User followee, int limit, User lastFollower) {
        followersList.clear();
        System.out.println("Retrieving followers with lastFollower:" + lastFollower);
//        boolean hasMorePages = getItems(followee.getAlias(), null, limit, lastFollower == null ? null : new Follows(lastFollower, followee), true, false, true);
//        return new DataPage<>(followersList, hasMorePages);
        DataPage<User> result = getFollowers(followee.getAlias(), limit, lastFollower == null ? null : lastFollower.getAlias());
        System.out.println("returning followers:" + result.getValues());
        return result;
    }

//    @Override
//    public List<String> getAllFollowerAliases(String userAlias) {
//        System.out.println("In getAllFollowerAliases");
//        stringList.clear();
//        lastItem = null;
//        boolean hasMoreItems = true;
//        while(hasMoreItems) {
//            hasMoreItems = getItems(userAlias, null, 10, lastItem, true);
//        }
//        return stringList;
//    }

    @Override
    public void addFollowsBatch(List<Follows> follows) {
        createBatchesAndThenWrite(follows);
    }

    public void follow(User follower, User followee) {
        Follows f = new Follows(follower, followee);
        createOrOverwrite(f);
    }

    public void unfollow(User follower, User followee) {
        Follows f = new Follows(follower, followee);
        delete(f);
    }

    @Override
    protected void saveItem(Follows entry) {
//        System.out.println("Found a follow record:" + entry);
        stringList.add(entry.getFollowerHandle());
        followersList.add(entry.getFollower());
        followeesList.add(entry.getFollowee());
//        System.out.println(entry.getFollowerHandle() + "IS A FOLLOWER, found while looking for all followers");
    }

    @Override
    public void changeRecordBeforeUpdate(Follows existingEntry, Map<String, Integer> updateObject) {}

    private DataPage<User>  getFollowers(String followee_handle, int pageSize, String last_follower) {

        DynamoDbIndex<Follows> index = enhancedClient.table(Follows.TABLE_NAME, TableSchema.fromBean(Follows.class)).index(Follows.INDEX_NAME);
        Key key = Key.builder()
                .partitionValue(followee_handle)
                .build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .scanIndexForward(true)
                .queryConditional(QueryConditional.keyEqualTo(key))
                .limit(pageSize);

        if(isNonEmptyString(last_follower)) {
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(Follows.SORT_KEY, AttributeValue.builder().s(followee_handle).build());
            startKey.put(Follows.PARTITION_KEY, AttributeValue.builder().s(last_follower).build());

            requestBuilder.exclusiveStartKey(startKey);
        }

        QueryEnhancedRequest request = requestBuilder.build();

        DataPage<User> result = new DataPage<>();

        SdkIterable<Page<Follows>> sdkIterable = index.query(request);
        PageIterable<Follows> pages = PageIterable.create(sdkIterable);
        pages.stream()
                .limit(1)
                .forEach((Page<Follows> page) -> {
                    result.setHasMorePages(page.lastEvaluatedKey() != null);
                    page.items().forEach(visit -> result.getValues().add(visit.getFollower()));
                });

        return result;
    }
}
