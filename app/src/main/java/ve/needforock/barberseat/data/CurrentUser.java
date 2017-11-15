package ve.needforock.barberseat.data;

import android.net.Uri;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Soporte on 11-Nov-17.
 */

public class CurrentUser {

    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    public FirebaseUser getCurrentUser() {
        return currentUser;
    }

    public String userEmail(){
        return getCurrentUser().getEmail();
    }
    public String userName(){
        return getCurrentUser().getDisplayName();
    }

    public String getUid(){
        return currentUser.getUid();
    }

    public String sanitizedEmail(String email){
        return email.replace("@", "AT").replace(".","DOT");
    }

    public Uri userPhoto(){
       return getCurrentUser().getPhotoUrl();
    }
}
