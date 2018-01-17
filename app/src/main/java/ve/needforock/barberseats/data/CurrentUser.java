package ve.needforock.barberseats.data;

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

    public String getUid(){
        return currentUser.getUid();
    }

    public String sanitizedEmail(String email){
        return email.replace("@", "AT").replace(".","DOT");
    }


}
