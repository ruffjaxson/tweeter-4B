package edu.byu.cs.tweeter.server.dao.dynamodb;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.abstract_classes.StatusDAO;
import edu.byu.cs.tweeter.server.dao.dynamodb.tables.Stories;
import edu.byu.cs.tweeter.server.models.DataPage;


public class DDBStatusDAO extends DynamoDAO<Stories> implements StatusDAO {

    private final List<Status> statusList;
    private User targetUser;

    public DDBStatusDAO() {
        super(Stories.class, Stories.TABLE_NAME, null);
        statusList = new ArrayList<>();
    }

    @Override
    public DataPage<Status> getStory(User targetUser, int limit, Status lastStatus) {
        this.targetUser = targetUser;
        statusList.clear();
        boolean hasMoreItems = getItems(targetUser.getAlias(), null, limit, lastStatus == null ? null : new Stories(lastStatus), false, true, false);
        return new DataPage<>(statusList, hasMoreItems);
    }

    public void postStatus(Status status) {
        Stories s = new Stories(status);
        createOrOverwrite(s);
        // TODO: UPDATE ALL FOLLOWERS' FEEDS
    }


    @Override
    protected void saveItem(Stories entry) {
        statusList.add(entry.convert(targetUser));
    }

    @Override
    public void changeRecordBeforeUpdate(Stories existingEntry, Map<String, Integer> updateObject){}

}
