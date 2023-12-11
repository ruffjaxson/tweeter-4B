package edu.byu.cs.tweeter.server.dao.abstract_classes;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.models.DataPage;

public interface FeedDAO {
    DataPage<Status> getFeed(User targetUser, int limit, Status lastStatus);

    void postStatusToAllFollowersFeeds(List<String> followerAliases, Status status);
}
