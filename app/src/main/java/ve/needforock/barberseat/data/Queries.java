package ve.needforock.barberseat.data;

import com.google.firebase.database.DatabaseReference;

/**
 * Created by Soporte on 13-Nov-17.
 */

public class Queries {

    public DatabaseReference CustomerAppointments (String key){
        return new Nodes().user(key).child("appointments");
    }

    public DatabaseReference BarberJobs(){
        return new Nodes().jobs();
    }

    public DatabaseReference BarberAppointments(String barberUid){
        return new Nodes().appointmentDay(barberUid);
    }

    public DatabaseReference AppmntsYear(String barberUid, int year){
        return new Queries().BarberAppointments(barberUid).child(String.valueOf(year));
    }

    public  DatabaseReference AppmntsMonth(String barberUid, int year, int month){
        return new Queries().AppmntsYear(barberUid,year).child(String.valueOf(month));
    }

    public DatabaseReference AppmntsDay(String barberUid, int year, int month, int day){
        return new Queries().AppmntsMonth(barberUid, year, month).child(String.valueOf(day));
    }

    public DatabaseReference UserDetails(String userUid){
        return new Nodes().user(userUid).child("details");
    }

    public DatabaseReference BarberRating(){
        return new Nodes().rating();
    }

}
