package edu.byu.cs.tweeter.server.dao.dynamodb;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.abstract_classes.UserDAO;
import edu.byu.cs.tweeter.server.dao.dynamodb.tables.Users;
import edu.byu.cs.tweeter.util.FakeData;

public class DDBUserDAO extends DynamoDAO<Users> implements UserDAO {
    private final BCryptPasswordEncoder passwordEncoder;
    private final String UPDATE_FOLLOWER_COUNT_KEY = "UPD_FR_COUNT";
    private final String UPDATE_FOLLOWEE_COUNT_KEY = "UPD_FE_COUNT";

    public DDBUserDAO(){
        super(Users.class, Users.TABLE_NAME,  null);
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public User login(String username, String rawPassword) {
        System.out.println("In DDBUserDao.login");
        Users ddbUser = find(username, null);
        System.out.println("ddbUser:" + ddbUser);
        if (ddbUser == null) return null;
        System.out.println("Checking to see if '" + rawPassword +"' matches this hash: " + ddbUser.getPasswordHash());
        if (passwordEncoder.matches(rawPassword, ddbUser.getPasswordHash())){
            System.out.println("Passwords match!!!");
            return ddbUser.convertToUser();
        }
        System.out.println("Passwords didn't match");
        return null;
    }

    @Override
    public User register(String username, String password, String firstName, String lastName, String imageUrl) {
        System.out.println("in DDBUserDAO.register");
        Users users = new Users(username, firstName, lastName, passwordEncoder.encode(password), imageUrl, 0, 0 );
        createOrOverwrite(users);
        return users.convertToUser();
    }

    @Override
    public User getUser(String userAlias) {
        Users u = find(userAlias, null);
        return u == null ? null : u.convertToUser();
    }

    @Override
    public int getFollowingCount(User user) {
        Users u = find(user.getAlias(), null);
        return u == null ? 15 : u.getFollowingCount();
    }

    @Override
    public int getFollowersCount(User user) {
        Users u = find(user.getAlias(), null);
        return u == null ? 15 : u.getFollowersCount();
    }


    @Override
    public void changeFollowersCount(String userAlias, Integer amount) {
        Map<String, Integer> updateObject = new HashMap<>();
        updateObject.put(UPDATE_FOLLOWER_COUNT_KEY, amount);
        updateObject.put(UPDATE_FOLLOWEE_COUNT_KEY, 0);
        update(userAlias, null, updateObject);
    }

    @Override
    public void changeFollowingCount(String userAlias, Integer amount) {
        Map<String, Integer> updateObject = new HashMap<>();
        updateObject.put(UPDATE_FOLLOWEE_COUNT_KEY, amount);
        updateObject.put(UPDATE_FOLLOWER_COUNT_KEY, 0);
        update(userAlias, null, updateObject);
    }

    @Override
    public void addUserBatch(List<Users> userList) {
        createBatchesAndThenWrite(userList);
    }

    @Override
    protected void saveItem(Users entry) {

    }

    @Override
    public void changeRecordBeforeUpdate(Users existingEntry, Map<String, Integer> updateMap) {
        System.out.println("In changeRecordBeforeUpdate. Map: " + updateMap);
        int followerAmount = updateMap.get(UPDATE_FOLLOWER_COUNT_KEY);
        int followeeAmount = updateMap.get(UPDATE_FOLLOWEE_COUNT_KEY);
        System.out.println("Got the amounts: " + followerAmount +", " + followeeAmount);
        existingEntry.updateFollowerCountByAmount(followerAmount);
        existingEntry.updateFolloweeCountByAmount(followeeAmount);
    }
}
