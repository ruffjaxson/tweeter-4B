package edu.byu.cs.tweeter.client;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.GetFollowingCountRequest;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.GetCountResponse;
import edu.byu.cs.tweeter.model.net.response.GetUsersResponse;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;


public class ServerFacadeTest {
    private ServerFacade facade;

    @BeforeEach
    public void setup() {
        facade = new ServerFacade();
    }

    @Test
    public void testRegister() {
        try {

            String username = "@username";
            String firstName = "firstName";
            String lastName = "lastName";
            String image = "image";


            RegisterRequest request = new RegisterRequest(
                    username,
                    "password",
                    firstName,
                    lastName,
                    image
            );

            RegisterResponse response = facade.register(request ,"/register");
            Assertions.assertNotNull(response.getAuthToken());
            Assertions.assertNotNull(response.getUser());
            Assertions.assertTrue(response.isSuccess());
            User user = response.getUser();
            Assertions.assertEquals("@allen", user.getAlias());
            Assertions.assertEquals("Allen", user.getFirstName());
            Assertions.assertEquals("Anderson", user.getLastName());
            Assertions.assertEquals("https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png", user.getImageUrl());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testGetFollowers() {
        User testUser = new User("name", "lastName", "imageUrl");
        try {
            PagedRequest<User> request = new PagedRequest(new AuthToken("auth"), testUser, 5, null);

            GetUsersResponse response = facade.getFollowers(request ,"/user/@allen/followers/");
            Assertions.assertTrue(response.isSuccess());

            List<User> followers = response.getItems();
            Assertions.assertEquals(5, followers.size());
            Assertions.assertTrue(response.getHasMorePages());
            Assertions.assertEquals(followers.get(0).getAlias(), "@allen");
            Assertions.assertEquals(followers.get(4).getAlias(), "@chris");
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testGetFollowingCount() {
        try {
            User user = new User("first", "last", "url");
            GetFollowingCountRequest request = new GetFollowingCountRequest(new AuthToken("auth"), user);

            GetCountResponse response = facade.getFollowingCount(request ,"/user/@allen/following/count");
            Assertions.assertTrue(response.isSuccess());
            Assertions.assertEquals(20, response.getCount());
        } catch (Exception e) {
            fail();
        }
    }

}
