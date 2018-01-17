package ve.needforock.barberseats.data;

import com.google.firebase.database.DatabaseReference;

/**
 * Created by Soporte on 13-Nov-17.
 */

public class Queries {

    public DatabaseReference CustomerAppointments (String key){
        return new Nodes().userAppointment(key);

    }

    public DatabaseReference BarberJobs(){
        return new Nodes().jobs();
    }

    public DatabaseReference UserDetails(String userUid){
        return new Nodes().user(userUid);
    }

    public DatabaseReference BarberRating(){
        return new Nodes().rating();
    }

}
