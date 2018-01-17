package ve.needforock.barberseats.application;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ve.needforock.barberseats.data.CurrentUser;
import ve.needforock.barberseats.data.Nodes;

/**
 * Created by Soporte on 30-Nov-17.
 */

public class Offline {
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference();

    public void setPersistence (){
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        new Nodes().users().keepSynced(true);
        root.child("barbers").child("456").keepSynced(true);
        root.child("barbers").child("457").keepSynced(true);
        root.child("barber_rating").child("456").keepSynced(true);
        root.child("barber_rating").child("457").keepSynced(true);
        root.child("barber_jobs").keepSynced(true);
        root.child("appointments").keepSynced(true);
        root.child("appointmentDays").child("456").keepSynced(true);
        root.child("appointmentDays").child("457").keepSynced(true);

        FirebaseUser user = new CurrentUser().getCurrentUser();
        root.child("users_appointments").child(user.getUid()).keepSynced(true);
        root.child("users").child(user.getUid()).keepSynced(true);

    }
}