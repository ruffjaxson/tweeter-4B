package edu.byu.cs.tweeter.client.model.services;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.*;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.handler.GetUserHandler;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.handler.LoginHandler;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.handler.LogoutHandler;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.handler.RegisterHandler;
import edu.byu.cs.tweeter.client.model.services.observer.ServiceObserver;
import edu.byu.cs.tweeter.client.presenter.AuthenticatedObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class UserService {

    public static final String LOGIN_URL_PATH = "/login";
    public static final String REGISTER_URL_PATH = "/register";
    public static final String USER_URL_PATH = "/user";
    public static final String LOGOUT_URL_PATH = "/logout";

    public void logout(LogoutObserver observer) {
        LogoutTask logoutTask = new LogoutTask(Cache.getInstance().getCurrUserAuthToken(), new LogoutHandler(observer), LOGOUT_URL_PATH);
        BackgroundTaskUtils.runTask(logoutTask);
    }

    public interface RegisterObserver extends ServiceObserver {
        void registerSucceeded(AuthToken authToken, User user);
    }

    public interface LogoutObserver extends ServiceObserver{
        void logoutSucceeded();
    }

    public void register(String firstName, String lastName, String alias,
                         String password, String imageByteArray, RegisterObserver observer) {
        // Send register request.
        RegisterTask registerTask = new RegisterTask(firstName, lastName, alias,
                password, imageByteArray, new RegisterHandler(observer), REGISTER_URL_PATH);
        BackgroundTaskUtils.runTask(registerTask);
    }

    public interface LoginObserver extends ServiceObserver {
        void loginSucceeded(AuthToken authToken, User user);
    }

    public void login(String alias, String password, LoginObserver observer){
        LoginTask loginTask = new LoginTask(alias, password, new LoginHandler(observer), LOGIN_URL_PATH);
        BackgroundTaskUtils.runTask(loginTask);
    }


    public void getUser(AuthToken authToken, String alias, AuthenticatedObserver observer){
        GetUserTask getUserTask = new GetUserTask(authToken, alias, new GetUserHandler(observer), USER_URL_PATH);
        BackgroundTaskUtils.runTask(getUserTask);
    }

}
