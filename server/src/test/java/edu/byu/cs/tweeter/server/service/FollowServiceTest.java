package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.dynamodb.DDBFollowDAO;
import edu.byu.cs.tweeter.util.Pair;

public class FollowServiceTest {

//    private FollowingRequest request;
//    private PagedFollowersOrFollowingResponse expectedResponse;
    private DDBFollowDAO mockDDBFollowDAO;
    private FollowService followServiceSpy;

    @BeforeEach
    public void setup() {
        AuthToken authToken = new AuthToken();

        User currentUser = new User("FirstName", "LastName", null);

        User resultUser1 = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        User resultUser2 = new User("FirstName2", "LastName2",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");
        User resultUser3 = new User("FirstName3", "LastName3",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");

        // Setup a request object to use in the tests
//        request = new FollowingRequest(authToken, currentUser.getAlias(), 3, null);
//
//        // Setup a mock FollowDAO that will return known responses
//        expectedResponse = new PagedFollowersOrFollowingResponse(Arrays.asList(resultUser1, resultUser2, resultUser3), false);
//        mockDDBFollowDAO = Mockito.mock(DDBFollowDAO.class);
//        Mockito.when(mockDDBFollowDAO.getFollowees(request.getFollowerAlias(), request.getLimit(), request.getLastFolloweeAlias()))
//                .thenReturn(new Pair<>(expectedResponse.getFollowees(), expectedResponse.getHasMorePages()));

        followServiceSpy = Mockito.spy(FollowService.class);
        Mockito.when(followServiceSpy.getFollowingDAO()).thenReturn(mockDDBFollowDAO);
    }

    /**
     * Verify that the {@link FollowService#getFollowees(FollowingRequest)}
     * method returns the same result as the {@link DDBFollowDAO} class.
     */
    @Test
    public void testGetFollowees_validRequest_correctResponse() {
//        PagedFollowersOrFollowingResponse response = followServiceSpy.getFollowees(request);
//        Assertions.assertEquals(expectedResponse, response);
    }
}
