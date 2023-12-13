package edu.byu.cs.tweeter.server.sqs;


import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.abstract_classes.FollowDAO;
import edu.byu.cs.tweeter.server.dao.abstract_classes.UserDAO;
import edu.byu.cs.tweeter.server.dao.dynamodb.DDBFollowDAO;
import edu.byu.cs.tweeter.server.dao.dynamodb.DDBUserDAO;
import edu.byu.cs.tweeter.server.dao.dynamodb.tables.Follows;
import edu.byu.cs.tweeter.server.dao.dynamodb.tables.Users;

public class Filler {
    static UserDAO userDAO = new DDBUserDAO();
    static FollowDAO followDAO = new DDBFollowDAO();

    // How many follower users to add
    // We recommend you test this with a smaller number first, to make sure it works for you
    private final static int NUM_USERS = 10000;

    // The alias of the user to be followed by each user created
    // This example code does not add the target user, that user must be added separately.
    private final static String FOLLOW_TARGET = "@emma";

    public static void fillDatabase() {

        // Get instance of DAOs by way of the Abstract Factory Pattern
        List<Follows> follows = new ArrayList<>();
        List<Users> userList = new ArrayList<>();

        User targetUser = new User();
        targetUser.setAlias(FOLLOW_TARGET);

        // Iterate over the number of users you will create
        for (int i = 0; i <= NUM_USERS; i++) {

            String name = "Guy " + i;
            String alias = "@guy" + i;

            // Note that in this example, a UserDTO only has a name and an alias.
            // The url for the profile image can be derived from the alias in this example
            Users follower = new Users();
            follower.setAlias(alias);
            follower.setFirstName(name);

            userList.add(follower);

            // Note that in this example, to represent a follows relationship, only the aliases
            // of the two users are needed
            follows.add(new Follows(follower.convertToUser(), targetUser));
        }

        try {
            // Call the DAOs for the database logic
            if (userList.size() > 0) {
                userDAO.addUserBatch(userList);
            }
            if (follows.size() > 0) {
                followDAO.addFollowsBatch(follows);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void emptyDatabases() {
        User targetUser = new User();
        targetUser.setAlias(FOLLOW_TARGET);

        for (int i = 1000; i <= NUM_USERS; i++) {

            String name = "Guy " + i;
            String alias = "@guy" + i;
            Users follower = new Users();
            follower.setAlias(alias);
            follower.setFirstName(name);

            System.out.println("Removing user and follows: " + follower.getAlias());
            userDAO.delete(follower);
            followDAO.unfollow(follower.convertToUser(), targetUser);
        }
    }

    public static void find() {
        userDAO.getUser("@guy2");
        followDAO.getFollowers(userDAO.getUser("@emma"), 10, null);
//        followDAO.isFollower(new User("", "", "@guy1", ""), new User("", "", "@emma", ""));
    }
}