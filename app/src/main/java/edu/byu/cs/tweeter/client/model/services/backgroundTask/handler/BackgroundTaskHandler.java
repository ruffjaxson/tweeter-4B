package edu.byu.cs.tweeter.client.model.services.backgroundTask.handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.model.services.backgroundTask.BackgroundTask;
import edu.byu.cs.tweeter.client.model.services.observer.ServiceObserver;

public abstract class BackgroundTaskHandler<T extends ServiceObserver> extends Handler {

    public final T observer;
    private final String exceptionMessageToPrepend;
    private String actionKey;

    public BackgroundTaskHandler(T observer, String exceptionMessageToPrepend) {
        super(Looper.getMainLooper());
        this.observer = observer;
        this.exceptionMessageToPrepend = exceptionMessageToPrepend;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        boolean success = msg.getData().getBoolean(BackgroundTask.SUCCESS_KEY);
        if (success) {
            handleSuccessMessage(observer, msg.getData());
            return;
        }

        if (msg.getData().containsKey(BackgroundTask.MESSAGE_KEY)) {
            String message = msg.getData().getString(BackgroundTask.MESSAGE_KEY);
            observer.handleFailure(message);
        } else if (msg.getData().containsKey(BackgroundTask.EXCEPTION_KEY)) {
            Exception ex = (Exception) msg.getData().getSerializable(BackgroundTask.EXCEPTION_KEY);
            observer.handleException(ex, exceptionMessageToPrepend);
        }
        observer.afterFailure(actionKey);
    }

    public void setActionKey(String actionKey) {
        this.actionKey = actionKey;
    }

    protected abstract void handleSuccessMessage(T observer, Bundle data);
}

// Pull all handlers into handler package, extend this class
// De-dup observer interfaces. Have serviceObserver be the base that is extended
