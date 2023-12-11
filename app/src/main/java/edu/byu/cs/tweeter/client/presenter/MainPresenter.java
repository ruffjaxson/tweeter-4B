package edu.byu.cs.tweeter.client.presenter;


import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.services.FollowService;
import edu.byu.cs.tweeter.client.model.services.StatusService;
import edu.byu.cs.tweeter.client.model.services.UserService;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.handler.FollowHandler;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.handler.UnfollowHandler;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.Timestamp;

public class MainPresenter extends Presenter<MainPresenter.View> implements FollowService.IsFollowerObserver, FollowService.GetFollowersCountObserver,
        FollowService.GetFollowingCountObserver, FollowService.UnfollowObserver, FollowService.FollowObserver,
        UserService.LogoutObserver, StatusService.PostStatusObserver {

    private User selectedUser;
    private FollowService followService = new FollowService();
    private StatusService statusService;
    private boolean useExactTime = true;

    public MainPresenter(View v, StatusService statusService) {
        super(v);
        if (statusService == null) {
            this.statusService = new StatusService();
        } else {
            this.statusService = statusService;
            this.useExactTime = false;
        }
    }


    public void initialize(User selectedUser) {
        this.selectedUser = selectedUser;
        updateSelectedUserFollowingAndFollowers();
        view.setFolloweeCount("...");
        view.setFollowerCount("...");

        if (selectedUser.compareTo(Cache.getInstance().getCurrUser()) == 0) {
            view.hideFollowButton();
        } else {
            view.showFollowButton();
            followService.isFollower(selectedUser, this);
        }

    }

    @Override
    public void isFollowerSucceeded(boolean isFollower) {
        view.updateFollowButton(isFollower);
    }

    @Override
    public void getFollowersCountSucceeded(int count) {
        view.setFollowerCount(String.valueOf(count));
    }

    @Override
    public void getFollowingCountSucceeded(int count) {
        view.setFolloweeCount(String.valueOf(count));
    }

    @Override
    public void handleFollowSuccessMessage() {
        updateSelectedUserFollowingAndFollowers();
        view.updateFollowButton(true);
        view.enableFollowButton();
    }

    public void logout() {
        view.showLogoutToast("Logging Out...");
        var userService = new UserService();
        userService.logout(this);

    }

    @Override
    public void logoutSucceeded() {
        view.hideLogoutMessage();
        Cache.getInstance().clearCache();
        view.openLoginActivity();
    }

    public void postStatus(String post, User user) {
        view.showPostingToast("Posting Status...");
        try {
            Status newStatus = new Status(post, user, getTime(), parseURLs(post), parseMentions(post));
            Timestamp.getFormattedDate(newStatus.getTimestamp());
            statusService.postStatus(newStatus, this);

        } catch (Exception ex) {
            view.showPostingToast("Failed to post the status because of exception: " + ex.getMessage());
        }
    }

    @Override
    public void postStatusSucceeded() {
        view.hidePostingToast();
        view.showPostingToast("Successfully Posted!");
    }

    @Override
    public void handleFailure(String message) {
        view.showErrorMessage(message);
    }

    @Override
    public void handleException(Exception exception, String exceptionMessageToPrepend) {
        view.showErrorMessage(exceptionMessageToPrepend  + exception.getMessage());
    }

    @Override
    public void afterFailure(String actionKey) {
        if (Objects.equals(actionKey, FollowHandler.FOLLOW_ACTION_KEY) || Objects.equals(actionKey, UnfollowHandler.UNFOLLOW_ACTION_KEY)){
            view.enableFollowButton();
        }
    }

    @Override
    public void handleUnfollowSuccessMessage() {
        updateSelectedUserFollowingAndFollowers();
        view.updateFollowButton(false);
        view.enableFollowButton();
    }

    public interface View extends Presenter.View {

        void disableFollowButton();
        void setFolloweeCount(String s);
        void setFollowerCount(String s);
        void hideFollowButton();
        void showFollowButton();
        void updateFollowButton(boolean isFollowing);
        void enableFollowButton();
        void hideLogoutMessage();
        void showLogoutToast(String s);
        void openLoginActivity();
        void showPostingToast(String s);
        void hidePostingToast();
    }


    private void updateSelectedUserFollowingAndFollowers() {
        followService.getFollowersCount(selectedUser, this);
        followService.getFollowingCount(selectedUser, this);
    }

    public void followButtonClicked(boolean isAlreadyFollowing) {
        view.disableFollowButton();

        var followService = new FollowService();
        if (isAlreadyFollowing){
            followService.unfollow(selectedUser, this);
            view.showInfoMessage("Removing " + selectedUser.getName() + "...");
        } else {
            followService.follow(selectedUser, this);
            view.showInfoMessage("Adding " + selectedUser.getName() + "...");
        }
    }


    public static List<String> parseURLs(String post) {
        List<String> containedUrls = new ArrayList<>();
        for (String word : post.split("\\s")) {
            if (word.startsWith("http://") || word.startsWith("https://")) {

                int index = findUrlEndIndex(word);

                word = word.substring(0, index);

                containedUrls.add(word);
            }
        }

        return containedUrls;
    }


    public static List<String> parseMentions(String post) {
        List<String> containedMentions = new ArrayList<>();

        for (String word : post.split("\\s")) {
            if (word.startsWith("@")) {
                word = word.replaceAll("[^a-zA-Z0-9]", "");
                word = "@".concat(word);

                containedMentions.add(word);
            }
        }

        return containedMentions;
    }

    private static int findUrlEndIndex(String word) {
        if (word.contains(".com")) {
            int index = word.indexOf(".com");
            index += 4;
            return index;
        } else if (word.contains(".org")) {
            int index = word.indexOf(".org");
            index += 4;
            return index;
        } else if (word.contains(".edu")) {
            int index = word.indexOf(".edu");
            index += 4;
            return index;
        } else if (word.contains(".net")) {
            int index = word.indexOf(".net");
            index += 4;
            return index;
        } else if (word.contains(".mil")) {
            int index = word.indexOf(".mil");
            index += 4;
            return index;
        } else {
            return word.length();
        }
    }

    public long getTime() {
        if (this.useExactTime) {
            return System.currentTimeMillis();
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfDay = LocalDateTime.of(now.toLocalDate(), LocalTime.MIDNIGHT);
        return startOfDay.toInstant(ZoneOffset.UTC).toEpochMilli();
    }

}
