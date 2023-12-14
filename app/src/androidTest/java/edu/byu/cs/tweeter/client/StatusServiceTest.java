package edu.byu.cs.tweeter.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.List;


import edu.byu.cs.tweeter.client.model.services.StatusService;
import edu.byu.cs.tweeter.client.presenter.PagedObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * This class exists purely to prove that tests in your androidTest/java folder have the correct dependencies.
 * Click on the green arrow to the left of the class declarations to run. These tests should pass if all
 * dependencies are correctly set up.
 */
public class StatusServiceTest {

    private void waitForMethodToRun() throws InterruptedException {
        Thread.sleep(2000);
    }

    StatusService service;

    @BeforeEach
    public void setup() {
        service = new StatusService();
    }

//    @Test
//    public void testGetStory() throws InterruptedException {
//        AuthToken authToken = new AuthToken("auth");
//        User newUser = new User("Jaxson", "Ruff", "imageUrl");
//        Status lastStatus = null;
//
//        PagedObserver<Status> observerSpy = Mockito.mock(StatusServiceObserver.class);
//        service.getStory(authToken, newUser, 5, lastStatus, observerSpy);
//        waitForMethodToRun();
//        Mockito.verify(observerSpy).getItemsSucceeded(Mockito.anyList(), Mockito.anyBoolean());
//    }


    private class StatusServiceObserver implements PagedObserver<Status> {

        @Override
        public void handleFailure(String message) {

        }

        @Override
        public void handleException(Exception exception, String exceptionMessageToPrepend) {

        }

        @Override
        public void afterFailure(String actionKey) {

        }

        @Override
        public void getUserSucceeded(User user) {

        }

        @Override
        public void getItemsSucceeded(List<Status> items, boolean hasMore) {

        }
    }

}

