package edu.byu.cs.tweeter.server.dao.dynamodb;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.abstract_classes.FollowDAO;
import edu.byu.cs.tweeter.server.dao.dynamodb.tables.Follows;
import edu.byu.cs.tweeter.server.models.DataPage;
import edu.byu.cs.tweeter.util.FakeData;

public class DDBFollowDAO extends DynamoDAO<Follows> implements FollowDAO {

    private Follows lastItem;
    private List<String> stringList;
    private List<User> followersList;
    private List<User> followeesList;

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
        boolean hasMorePages = getItems(follower.getAlias(), null, limit, lastFollowee == null ? null : new Follows(follower, lastFollowee), false);
        return new DataPage<>(followeesList, hasMorePages);
    }

    public DataPage<User> getFollowers(User followee, int limit, User lastFollower) {
        followersList.clear();
        boolean hasMorePages = getItems(followee.getAlias(), null, limit, lastFollower == null ? null : new Follows(followee, lastFollower), true);
        return new DataPage<>(followersList, hasMorePages);
    }

    @Override
    public List<String> getAllFollowerAliases(String userAlias) {
        System.out.println("In getAllFollowerAliases");
        stringList.clear();
        lastItem = null;
        boolean hasMoreItems = true;
        while(hasMoreItems) {
            hasMoreItems = getItems(userAlias, null, 10, lastItem, true);
        }
        return stringList;
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
        System.out.println("Found a follow record:" + entry);
        lastItem = entry;
        stringList.add(entry.getFollowerHandle());
        followersList.add(entry.getFollower());
        followeesList.add(entry.getFollowee());
        System.out.println(entry.getFollowerHandle() + "IS A FOLLOWER, found while looking for all followers");
    }

    @Override
    public void changeRecordBeforeUpdate(Follows existingEntry, Map<String, Integer> updateObject) {}
}
