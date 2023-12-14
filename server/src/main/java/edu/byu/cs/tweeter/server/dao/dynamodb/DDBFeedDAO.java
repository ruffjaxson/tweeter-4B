package edu.byu.cs.tweeter.server.dao.dynamodb;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.abstract_classes.FeedDAO;
import edu.byu.cs.tweeter.server.dao.dynamodb.tables.Feeds;
import edu.byu.cs.tweeter.server.models.DataPage;


public class DDBFeedDAO extends DynamoDAO<Feeds> implements FeedDAO {

    private final List<Status> statusList;
    private User targetUser;

    public DDBFeedDAO() {
        super(Feeds.class, Feeds.TABLE_NAME, null);
        statusList = new ArrayList<>();
    }

    public DataPage<Status> getFeed(User targetUser, int limit, Status lastStatus) {
        this.targetUser = targetUser;
        statusList.clear();
        boolean hasMoreItems = getItems(targetUser.getAlias(), lastStatus == null ? null : lastStatus.getSortHash(), limit, lastStatus == null ? null : new Feeds(targetUser.getAlias(), lastStatus), false, true, false);
        return new DataPage<>(statusList, hasMoreItems);
    }

    @Override
    public void postStatusToAllFollowersFeeds(List<User> followers, Status status) {
//        System.out.println("Posting status: " + status + " to followers: " + followers);
        List<Feeds> feeds = new ArrayList<>();
        for (User follower : followers) {
            feeds.add(new Feeds(follower.getAlias(), status));
        }
        createBatchesAndThenWrite(feeds);
    }


    public void postToFeed(String feedOwnerAlias, Status status) {
        if (Objects.equals(feedOwnerAlias, status.getUser().getAlias())) return;
        Feeds f = new Feeds(feedOwnerAlias, status);
        createOrOverwrite(f);
    }




    @Override
    protected void saveItem(Feeds entry) {
        statusList.add(entry.convert());
    }

    @Override
    public void changeRecordBeforeUpdate(Feeds existingEntry, Map<String, Integer> updateObject) {

    }
}
