package ve.needforock.barberseat.views.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.crashlytics.android.Crashlytics;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ResultCodes;

import java.util.Arrays;

import io.fabric.sdk.android.Fabric;
import ve.needforock.barberseat.R;
import ve.needforock.barberseat.views.main.MainActivity;

public class LoginActivity extends AppCompatActivity implements LoginCallback {

    private static final int RC_SIGN_IN = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_login);
        new LoginPresenter(LoginActivity.this).validation();
    }


    @Override
    public void signUp() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setProviders(
                                Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                        new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()/*,
                                        new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build(),
                                        new AuthUI.IdpConfig.Builder(AuthUI.TWITTER_PROVIDER).build()*/))
                        .setTheme(R.style.LoginTheme)
                        .setLogo(R.mipmap.logo)
                        .build(),
                RC_SIGN_IN);

    }
    @Override
    public void logged() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (ResultCodes.OK == resultCode) {
                logged();
            }
        }

    }


}
