package edu.byu.cs.tweeter.server.dao.abstract_classes;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.models.DataPage;

public interface StatusDAO {
    DataPage<Status> getStory(User targetUser, int limit, Status lastStatus);
    void postStatus(Status status);
}
