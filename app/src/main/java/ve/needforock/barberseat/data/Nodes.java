package ve.needforock.barberseat.data;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Soporte on 13-Nov-17.
 */

public class Nodes {

    private DatabaseReference root = FirebaseDatabase.getInstance().getReference();

    public DatabaseReference users(){
        return root.child("users");
    }

    public DatabaseReference user(String key){
        return root.child("users").child(key);
    }

    public DatabaseReference barber(String key){
        return root.child("barbers").child(key);
    }

    public DatabaseReference appointments(String barberUid){
        return root.child("appointments").child(barberUid);
    }

    public DatabaseReference appointmentDay(String barberUid){
        return root.child("appointmentDays").child(barberUid);
    }

    public DatabaseReference jobs(){
        return root.child("barber_jobs");
    }

    public DatabaseReference userAppointment(String userUid){
        return root.child("users").child(userUid).child("appointments");
    }

    public DatabaseReference barberRating(String barberUid){
        return root.child("barber_rating").child(barberUid);
    }

}
