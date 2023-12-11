package edu.byu.cs.tweeter.client.presenter;


import java.util.List;

import edu.byu.cs.tweeter.client.model.services.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class PagedPresenter<I, V extends PagedPresenter.PagedView> extends Presenter<V> implements PagedObserver<I> {

    protected boolean isLoading = false;
    protected boolean hasMorePages = false;
    protected I lastItem;
    protected AuthenticatedObserver observer;
    protected User user;
    protected abstract void callServiceClassToGetItems();


    public PagedPresenter(V v, User user) {
        super(v);
        this.user = user;
    }

    public boolean getIsLoading() {
        return isLoading;
    }
    public boolean getHasMorePages() {
        return hasMorePages;
    }


    public void getUser(AuthToken authToken, String alias) {
        view.showInfoMessage("Getting user's profile...");
        var userService = new UserService();
        userService.getUser(authToken, alias, observer);
    }


    public void setObserver(AuthenticatedObserver observer) {
        this.observer = observer;
    }


    // duplicate presenter code
    public interface PagedView<U> extends View {
        // duplicate view code
        void openMainView(User user);
        void startingLoad();
        void addItems(List<U> items);
        void endingLoad();
    }

    public void loadMoreItems() {
        if (!isLoading) {
            isLoading = true;
            view.startingLoad();
            callServiceClassToGetItems();
        }
    }

    @Override
    public void getItemsSucceeded(List<I> items, boolean hasMore){
        isLoading = false;
        view.endingLoad();
        lastItem = (items.size() > 0) ? items.get(items.size() - 1) : null;
        view.addItems(items);
        hasMorePages = hasMore;
    }

    @Override
    public void getUserSucceeded(User user) {
        view.openMainView(user);
    }

    @Override
    public void handleFailure(String message) {
        isLoading = false;
        view.endingLoad();
        view.showErrorMessage(message);
    }

    @Override
    public void handleException(Exception exception, String exceptionMessageToPrepend) {
        isLoading = false;
        view.endingLoad();
        view.showErrorMessage(exceptionMessageToPrepend  + exception.getMessage());
    }

    @Override
    public void afterFailure(String actionKey) {}
}

