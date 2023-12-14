package edu.byu.cs.tweeter.client;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.presenter.LoginPresenter;
import edu.byu.cs.tweeter.client.presenter.MainPresenter;
import edu.byu.cs.tweeter.client.presenter.StoryPresenter;
import edu.byu.cs.tweeter.client.view.login.LoginFragment;
import edu.byu.cs.tweeter.client.view.main.MainActivity;
import edu.byu.cs.tweeter.client.view.main.story.StoryFragment;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;


public class StatusTest4C {

    private void waitForMethodToRun(int time) throws InterruptedException {
        Thread.sleep(time);
    }

    @Test
    public void testPostStatus() throws InterruptedException {
        LoginFragment fragment = Mockito.mock(LoginFragment.class);
        LoginPresenter loginPresenter = Mockito.spy(new LoginPresenter(fragment));

        loginPresenter.login("@jax", "jax");
        waitForMethodToRun(2000);

        MainActivity mainActivity = Mockito.mock(MainActivity.class);
        MainPresenter mainPresenter = Mockito.spy(new MainPresenter(mainActivity, null));

        User currUser = Cache.getInstance().getCurrUser();
        String postMessage = "Hello I am under da water";

        mainPresenter.postStatus(postMessage, Cache.getInstance().getCurrUser());
        Mockito.verify(mainActivity).showPostingToast("Posting Status...");
        waitForMethodToRun(5000);
        Mockito.verify(mainActivity).showPostingToast("Successfully Posted!");


        StoryFragment storyView = Mockito.mock(StoryFragment.class);
        StoryPresenter storyPresenter = Mockito.spy(new StoryPresenter(storyView, currUser));

        ArgumentCaptor<List<Status>> stringCaptor = ArgumentCaptor.forClass(List.class);
        storyPresenter.loadMoreItems();

        waitForMethodToRun(5000);
        Mockito.verify(storyPresenter).getItemsSucceeded(stringCaptor.capture(), Mockito.anyBoolean());
        List<Status> statuses = stringCaptor.getValue();
        Status postedStatus = statuses.get(0);
        Assertions.assertEquals(postedStatus.user, currUser);
        Assertions.assertEquals(postedStatus.post, postMessage);
    }





}
