package ve.needforock.barberseat.data;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import ve.needforock.barberseat.models.Appointment;

/**
 * Created by Soporte on 22-Nov-17.
 */

public class DeleteAppointment {

    private String hour;

    public void delete(final Appointment appointment){

        DatabaseReference ref = new Nodes().userAppointment(appointment.getUserUID());
        ref.child(appointment.getKey()).removeValue();

        DatabaseReference ref2 = new Nodes().appointments(appointment.getBarberUid());
        ref2.child(appointment.getKey()).removeValue();


        Calendar calendar = Calendar.getInstance();
        calendar.setTime(appointment.getDate());
        String year = String.valueOf(calendar.get(calendar.YEAR));
        String month = String.valueOf(calendar.get(calendar.MONTH));
        String day = String.valueOf(calendar.get(calendar.DAY_OF_MONTH));
        hour = String.valueOf(calendar.get(calendar.HOUR_OF_DAY)) + ":00";

        Log.d("FECHA", year + month + day + hour);
        final DatabaseReference ref3 = new Nodes().appointmentDay(appointment.getBarberUid()).child(year).child(month).child(day);
        ref3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                            ref3.child(hour).removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
