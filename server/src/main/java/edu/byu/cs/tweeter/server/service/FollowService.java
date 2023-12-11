package edu.byu.cs.tweeter.server.service;


import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;
import edu.byu.cs.tweeter.model.net.response.GetUsersResponse;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.model.net.response.UnfollowResponse;
import edu.byu.cs.tweeter.server.dao.abstract_classes.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.abstract_classes.FollowDAO;
import edu.byu.cs.tweeter.server.dao.abstract_classes.UserDAO;
import edu.byu.cs.tweeter.server.models.DataPage;

public class FollowService extends Service {

    private final FollowDAO followDao;
    private final UserDAO userDao;

    public FollowService (FollowDAO followDao, UserDAO userDao, AuthTokenDAO authTokenDAO) {
        super(authTokenDAO);
        this.followDao = followDao;
        this.userDao = userDao;
    }

    public GetUsersResponse getFollowing(PagedRequest<User> request) {
        if (isAuthTokenInvalid(request.getAuthToken())) { return new GetUsersResponse(AUTHENTICATION_FAILED_MESSAGE);}
        DataPage<User> page = followDao.getFollowing(
                request.getTargetUser(),
                request.getLimit(),
                request.getlastItem() == null ? null : request.getlastItem()
        );
        return new GetUsersResponse(page.getValues(), page.getHasMorePages());
    }

    public GetUsersResponse getFollowers(PagedRequest<User> request) {
        if (isAuthTokenInvalid(request.getAuthToken())) { return new GetUsersResponse(AUTHENTICATION_FAILED_MESSAGE);}

        DataPage<User> page = followDao.getFollowers(
                request.getTargetUser(),
                request.getLimit(),
                request.getlastItem() == null ? null : request.getlastItem()
        );
        return new GetUsersResponse(page.getValues(), page.getHasMorePages());
    }

    public IsFollowerResponse isFollower(IsFollowerRequest request) {
        System.out.println("in FollowService.isFollower");
        if (isAuthTokenInvalid(request.getAuthToken())) { return new IsFollowerResponse(AUTHENTICATION_FAILED_MESSAGE);}

        System.out.println("Validated auth token");
        Boolean isFollower = followDao.isFollower(request.getFollower(), request.getFollowee());
        System.out.println("Result from isFollower: " + isFollower);

        return new IsFollowerResponse(isFollower, renewedAuthToken());
    }

    public FollowResponse follow(FollowRequest request) {
        if (isAuthTokenInvalid(request.getAuthToken())) { return new FollowResponse(AUTHENTICATION_FAILED_MESSAGE);}

        followDao.follow(request.getFollower(), request.getFollowee());
        userDao.changeFollowersCount(request.getFollowee().getAlias(), 1);
        userDao.changeFollowingCount(request.getFollower().getAlias(), 1);

        System.out.println("FollowService.follow is returning successfully");
        return new FollowResponse();
    }

    public UnfollowResponse unfollow(UnfollowRequest request) {
        if (isAuthTokenInvalid(request.getAuthToken())) { return new UnfollowResponse(AUTHENTICATION_FAILED_MESSAGE);}

        followDao.unfollow(request.getFollower(), request.getFollowee());
        userDao.changeFollowersCount(request.getFollowee().getAlias(), -1);
        userDao.changeFollowingCount(request.getFollower().getAlias(), -1);

        System.out.println("FollowService.unfollow is returning successfully");
        return new UnfollowResponse();
    }
}
