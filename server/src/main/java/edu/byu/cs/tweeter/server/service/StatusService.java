package edu.byu.cs.tweeter.server.service;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.GetStatusesResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.server.dao.abstract_classes.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.abstract_classes.FeedDAO;
import edu.byu.cs.tweeter.server.dao.abstract_classes.FollowDAO;
import edu.byu.cs.tweeter.server.dao.abstract_classes.StatusDAO;
import edu.byu.cs.tweeter.server.models.DataPage;

public class StatusService extends Service {

    private final StatusDAO statusDao;
    private final FollowDAO followDao;
    private final FeedDAO feedDao;

    public StatusService(StatusDAO dao, AuthTokenDAO authTokenDAO, FollowDAO followDao, FeedDAO feedDAO) {
        super(authTokenDAO);
        this.statusDao = dao;
        this.followDao = followDao;
        this.feedDao = feedDAO;
    }

    public GetStatusesResponse getStory(PagedRequest<Status> request) {
        if (isAuthTokenInvalid(request.getAuthToken())) { return new GetStatusesResponse(AUTHENTICATION_FAILED_MESSAGE);}

        DataPage<Status> page = statusDao.getStory(request.getTargetUser(), request.getLimit(), request.getlastItem());
        return new GetStatusesResponse(page.getValues(), page.getHasMorePages());
    }

    public GetStatusesResponse getFeed(PagedRequest<Status> request) {
        if (isAuthTokenInvalid(request.getAuthToken())) { return new GetStatusesResponse(AUTHENTICATION_FAILED_MESSAGE);}

        DataPage<Status> page = feedDao.getFeed(request.getTargetUser(), request.getLimit(), request.getlastItem());
        return new GetStatusesResponse(page.getValues(), page.getHasMorePages());
    }

    public PostStatusResponse postStatus(PostStatusRequest request) {
        if (isAuthTokenInvalid(request.getAuthToken())) { return new PostStatusResponse(AUTHENTICATION_FAILED_MESSAGE);}
        statusDao.postStatus(request.getStatus());
        List<String> followerAliases = followDao.getAllFollowerAliases(request.getUserAlias());
        feedDao.postStatusToAllFollowersFeeds(followerAliases, request.getStatus());

        return new PostStatusResponse();
    }

}
