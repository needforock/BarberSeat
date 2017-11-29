package ve.needforock.barberseat.data;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;

import ve.needforock.barberseat.models.Appointment;
import ve.needforock.barberseat.models.Barber;
import ve.needforock.barberseat.models.BarberDay;

/**
 * Created by Soporte on 20-Nov-17.
 */

public class AppointmentToFireBase {
    public void SaveAppointment(final Context context, final String barberUid, Date date, final String hour, String jobName){

        final Appointment appointment = new Appointment();
        appointment.setBarberUid(barberUid);
        final String customerUid = new CurrentUser().getUid();
        appointment.setUserUID(customerUid);
        appointment.setJob(jobName);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date);

        int day = cal2.get(cal2.DAY_OF_MONTH);
        int month = cal2.get(cal2.MONTH);
        int year = cal2.get(cal2.YEAR);
        String[] fullHour = hour.split(":");
        int hours = Integer.parseInt(fullHour[0]);
        int  minutes = Integer.parseInt(fullHour[1]);
        cal2.set(year, month, day, hours,minutes);
        Date finalDate = cal2.getTime();

        appointment.setDate(finalDate);

        DatabaseReference barber = new Nodes().barber(barberUid);
        barber.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Barber auxBarber = dataSnapshot.getValue(Barber.class);
                Log.d("Barbero", auxBarber.getName());
                appointment.setBarberName(auxBarber.getName());


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        Calendar cal = Calendar.getInstance();
        cal.setTime(date);


        final BarberDay barberDay = new BarberDay();
        barberDay.setDate(date);
        final DatabaseReference ref =  new Nodes().appointmentDay(barberUid)
                .child(String.valueOf(cal.get(cal.YEAR)))
                .child(String.valueOf(cal.get(cal.MONTH)))
                .child(String.valueOf(cal.get(cal.DAY_OF_MONTH)));

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                BarberDay auxBarberDay = dataSnapshot.getValue(BarberDay.class);
                ref.child(hour).setValue(true);
                uploadAppointment(appointment, barberUid, customerUid);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void uploadAppointment(Appointment appointment, String barberUid, String customerUid){
        String appointKey = new Nodes().user(customerUid).child("appointments").push().getKey();
        appointment.setKey(appointKey);
        new Nodes().appointments(barberUid).child(appointKey).setValue(appointment);
        new Nodes().user(customerUid).child("appointments").child(appointKey).setValue(appointment);

    }
}
