package edu.byu.cs.tweeter.client.presenter;


import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

import edu.byu.cs.tweeter.client.model.services.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class RegisterPresenter extends Presenter<RegisterPresenter.View> implements UserService.RegisterObserver {


    public RegisterPresenter(View passedView) {
        super(passedView);
        this.view = passedView;
    }
    final private View view;

    public void register(String firstName, String lastName, String alias,
                         String password, ImageView imageToUpload) {
        if(validateRegistration(firstName, lastName, alias, password, imageToUpload)){
            view.hideErrorMessage();
            view.showInfoMessage("Registering...");

            Bitmap image = ((BitmapDrawable) imageToUpload.getDrawable()).getBitmap();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            byte[] imageBytes = bos.toByteArray();

            // Intentionally, Use the java Base64 encoder so it is compatible with M4.
            String imageBytesBase64 = Base64.getEncoder().encodeToString(imageBytes);

            var userService = new UserService();
            userService.register(firstName, lastName, alias, password, imageBytesBase64, this);
        }


    }

    @Override
    public void registerSucceeded(AuthToken authToken, User user) {
        view.hideInfoMessage();
        view.showInfoMessage("Hello " + user.getName());
        view.openMainView(user);
        
    }


    @Override
    public void handleFailure(String message) {
        view.showErrorMessage(message);
    }

    @Override
    public void handleException(Exception exception, String exceptionMessageToPrepend) {
        view.showErrorMessage(exceptionMessageToPrepend + exception.getMessage());
    }

    @Override
    public void afterFailure(String actionKey) {}

    public interface View extends Presenter.View {
        void hideInfoMessage();
        void hideErrorMessage();
        void openMainView(User user);
    }


    private boolean validateRegistration(String firstName, String lastName, String alias,
                                         String password, ImageView imageToUpload) {
        if (firstName.length() == 0) {
            view.showErrorMessage("First Name cannot be empty.");
            return false;
        }
        if (lastName.length() == 0) {
            view.showErrorMessage("Last Name cannot be empty.");
            return false;
        }
        if (alias.length() == 0) {
            view.showErrorMessage("Alias cannot be empty.");
            return false;
        }
        if (alias.charAt(0) != '@') {
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
        if (imageToUpload.getDrawable() == null) {
            view.showErrorMessage("Profile image must be uploaded.");
            return false;
        }
        return true;
    }

}
