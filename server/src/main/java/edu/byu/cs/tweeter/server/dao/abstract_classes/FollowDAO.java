package edu.byu.cs.tweeter.server.dao.abstract_classes;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.models.DataPage;

public interface FollowDAO {
    void follow(User follower, User followee);
    void unfollow(User follower, User followee);
    boolean isFollower(User follower, User followee);
    DataPage <User> getFollowing(User follower, int limit, User lastFollowee);
    DataPage<User> getFollowers(User followee, int limit, User lastFollower);
    List<String> getAllFollowerAliases(String userAlias);

}
