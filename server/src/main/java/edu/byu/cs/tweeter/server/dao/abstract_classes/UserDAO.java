package edu.byu.cs.tweeter.server.dao.abstract_classes;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.dynamodb.tables.Users;

public interface UserDAO {
    User login(String username, String rawPassword);
    User register(String username, String password, String firstName, String lastName, String imageByteString);
    User getUser(String userAlias);
    int getFollowingCount(User user);
    int getFollowersCount(User user);
    void changeFollowersCount(String userAlias, Integer amount);
    void changeFollowingCount(String userAlias, Integer amount);
    void addUserBatch(List<Users> userList);
    void delete(Users u);
}
