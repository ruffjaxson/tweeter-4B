package edu.byu.cs.tweeter.client.model.services.observer;

public interface ServiceObserver {
    void handleFailure(String message);
    void handleException(Exception exception, String exceptionMessageToPrepend);
    void afterFailure(String actionKey);
}
