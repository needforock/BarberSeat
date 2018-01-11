package ve.needforock.barberseats.views.login;

import ve.needforock.barberseats.data.CurrentUser;

/**
 * Created by Soporte on 11-Nov-17.
 */

public class LoginPresenter {
    LoginCallback loginCallback;

    public LoginPresenter(LoginCallback loginCallback) {
        this.loginCallback = loginCallback;
    }

    public void validation() {
        if (new CurrentUser().getCurrentUser() != null) {
            loginCallback.logged();
        } else {
            loginCallback.signUp();
        }
    }
}
