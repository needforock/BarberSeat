package ve.needforock.barberseat.application;

import android.app.Application;

import com.google.firebase.FirebaseApp;

/**
 * Created by Soporte on 11-Nov-17.
 */

public class BarberApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseApp.initializeApp(this);
        new Offline().setPersistence();
    }


}
