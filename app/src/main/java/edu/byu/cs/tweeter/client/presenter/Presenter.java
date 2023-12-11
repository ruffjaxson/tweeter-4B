package edu.byu.cs.tweeter.client.presenter;


public abstract class Presenter<T extends Presenter.View> {
    public T view;

    public Presenter(T v){
        this.view = v;
    }

    public interface View {
        void showInfoMessage(String message);
        void showErrorMessage(String message);

    }

}
