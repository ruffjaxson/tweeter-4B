package edu.byu.cs.tweeter.client;

import edu.byu.cs.tweeter.client.model.services.StatusService;
import edu.byu.cs.tweeter.client.presenter.MainPresenter;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class MainPresenterTest {

    @Mock
    private MainPresenter.View mockView;
    @Mock
    private StatusService mockStatusService;

    @InjectMocks
    private MainPresenter mainPresenter = new MainPresenter(mockView, mockStatusService);


    private Status status;
    private User user;
    private String post;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        post = "Test status";

        user = new User("Jaxson", "Ruff", "@ruffjaxson", "https://google.com");
        status = new Status(post, user, mainPresenter.getTime(), MainPresenter.parseURLs(post), MainPresenter.parseMentions(post));
    }

    @Test
    public void testPostStatusSucceeded() {
        mainPresenter.postStatus(post, user);
        verify(mockView).showPostingToast("Posting Status...");

        mainPresenter.postStatusSucceeded();
        verify(mockView).showPostingToast("Successfully Posted!");
    }


    @Test
    public void testPostStatusFailed() {
        mainPresenter.postStatus(post, user);
        verify(mockView).showPostingToast("Posting Status...");

        mainPresenter.handleFailure("Failed to post status");
        verify(mockView).showErrorMessage("Failed to post status");
    }

    @Test
    public void testPostStatusException() {
        mainPresenter.postStatus(post, user);
        verify(mockView).showPostingToast("Posting Status...");

        mainPresenter.handleException(new NullPointerException("you can't do that"), "Failed to post status because of exception: ");
        verify(mockView).showErrorMessage("Failed to post status because of exception: you can't do that");
    }

    @Test
    public void testPostStatusParameters() {
        mainPresenter.postStatus(post, user);
        verify(mockStatusService).postStatus(status, mainPresenter);
    }

}
