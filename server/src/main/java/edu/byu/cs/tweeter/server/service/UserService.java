package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.GetFollowersCountRequest;
import edu.byu.cs.tweeter.model.net.request.GetFollowingCountRequest;
import edu.byu.cs.tweeter.model.net.request.GetUserRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.GetCountResponse;
import edu.byu.cs.tweeter.model.net.response.GetUserResponse;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;
import edu.byu.cs.tweeter.server.dao.S3DAO;
import edu.byu.cs.tweeter.server.dao.abstract_classes.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.abstract_classes.UserDAO;

public class UserService extends Service {

    private final UserDAO userDao;

    public UserService (UserDAO dao, AuthTokenDAO authDao) {
        super(authDao);
        this.userDao = dao;
    }

    public LoginResponse login(LoginRequest request) {
        User user = userDao.login(request.getUsername(), request.getPassword());
        if (user != null) {
            System.out.println("Creating auth token for user: " + user);
            return new LoginResponse(user, renewedAuthToken());
        }
        System.out.println("Creating failure LoginResponse");
        LoginResponse response = new LoginResponse("Unable to find a user with those credentials");
        System.out.println("Created response:" + response);
        return response;
    }

    public RegisterResponse register(RegisterRequest request) {
        System.out.println("in UserService.register");
        String imageUrl = S3DAO.saveImage(request.getImage(), request.getUsername());

        User user = userDao.register((request.getUsername()), request.getPassword(), request.getFirstName(), request.getLastName(), imageUrl);
        System.out.println("after userDao.register");

        AuthToken authToken = createAuthToken();
        return new RegisterResponse(user, authToken);
    }


    public LogoutResponse logout(LogoutRequest request) {
        if (isAuthTokenInvalid(request.getAuthToken())) { return new LogoutResponse(AUTHENTICATION_FAILED_MESSAGE);}

        deleteAuthToken(request.getAuthToken());
        return new LogoutResponse();
    }

    public GetUserResponse getUser(GetUserRequest request) {
        if (isAuthTokenInvalid(request.getAuthToken())) { return new GetUserResponse(AUTHENTICATION_FAILED_MESSAGE);}

        User user = userDao.getUser(request.getUserAlias());
        if (user == null) {
            throw new RuntimeException("[Server Error], could not find user with alias: " + request.getUserAlias());
        }
        return new GetUserResponse(user, renewedAuthToken());
    }

    public GetCountResponse getFollowingCount(GetFollowingCountRequest request) {
        if (isAuthTokenInvalid(request.getAuthToken())) { return new GetCountResponse(AUTHENTICATION_FAILED_MESSAGE);}


        int followeeCount = userDao.getFollowingCount(request.getFollower());
        return new GetCountResponse(followeeCount, renewedAuthToken());
    }


    public GetCountResponse getFollowersCount(GetFollowersCountRequest request) {
        if (isAuthTokenInvalid(request.getAuthToken())) { return new GetCountResponse(AUTHENTICATION_FAILED_MESSAGE);}

        int followerCount = userDao.getFollowersCount(request.getFollowee());
        return new GetCountResponse(followerCount, renewedAuthToken());
    }

}
