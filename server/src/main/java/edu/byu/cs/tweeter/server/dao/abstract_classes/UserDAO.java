package edu.byu.cs.tweeter.server.dao.abstract_classes;

import edu.byu.cs.tweeter.model.domain.User;

public interface UserDAO {
    User login(String username, String rawPassword);
    User register(String username, String password, String firstName, String lastName, String imageByteString);
    User getUser(String userAlias);
    int getFollowingCount(User user);
    int getFollowersCount(User user);
    void changeFollowersCount(String userAlias, Integer amount);
    void changeFollowingCount(String userAlias, Integer amount);
}
