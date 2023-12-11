package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.client.model.services.UserService;

public class LoginPresenter extends Presenter<LoginPresenter.LoginView> implements UserService.LoginObserver {


    public LoginPresenter(LoginView passedView) {
        super(passedView);
    }

    @Override
    public void handleFailure(String message) {
        view.showErrorMessage(message);
        view.showInfoMessage(message);
    }

    @Override
    public void handleException(Exception exception, String exceptionMessageToPrepend) {
        view.hideInfoMessage();
        view.showErrorMessage(exceptionMessageToPrepend + exception.getMessage());
        view.showInfoMessage(exceptionMessageToPrepend + exception.getMessage());
    }

    @Override
    public void afterFailure(String actionKey) {}

    public interface LoginView extends Presenter.View {
        void hideInfoMessage();
        void hideErrorMessage();
        void openMainView(User user);
    }

    public void login(String alias, String password){
        if (validateLogin(alias, password)){
            view.hideErrorMessage();
            view.showInfoMessage("Logging in...");

            var userService = new UserService();
            userService.login(alias, password, this);
        }
    }

    @Override
    public void loginSucceeded(AuthToken authToken, User user) {
        view.hideInfoMessage();
        view.hideErrorMessage();
        view.showInfoMessage("Hello" + user.getName());
        view.openMainView(user);
    }


    public boolean validateLogin(String alias, String password) {
        if (alias.length() > 0 && alias.charAt(0) != '@') {
            view.showErrorMessage("Alias must begin with @.");
            return false;
        }
        if (alias.length() < 2) {
            view.showErrorMessage("Alias must contain 1 or more characters after the @.");
            return false;
        }
        if (password.length() == 0) {
            view.showErrorMessage("Password cannot be empty.");
            return false;
        }
        return true;
    }
}
