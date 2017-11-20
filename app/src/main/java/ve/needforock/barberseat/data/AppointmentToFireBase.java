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
    public void SaveAppointment(final Context context, final String barberUid, Date date, final String hour){

        final Appointment appointment = new Appointment();
        appointment.setBarberUid(barberUid);
        final String customerUid = new CurrentUser().getUid();
        appointment.setUserUID(customerUid);
        appointment.setDate(date);

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

                if(hour.equals("9:00") && !auxBarberDay.isNine()){
                    auxBarberDay.setNine(true);
                    uploadAppointment(appointment, barberUid, customerUid);
                }
                if(hour.equals("10:00") && !auxBarberDay.isTen()){
                    auxBarberDay.setTen(true);
                    uploadAppointment(appointment, barberUid, customerUid);
                }
                if(hour.equals("11:00") && !auxBarberDay.isEleven()){
                    auxBarberDay.setEleven(true);
                    uploadAppointment(appointment, barberUid, customerUid);
                }
                if(hour.equals("12:00") && !auxBarberDay.isTwelve()){
                    auxBarberDay.setTwelve(true);
                    uploadAppointment(appointment, barberUid, customerUid);
                }
                if(hour.equals("13:00") && !auxBarberDay.isThirteen()){
                    auxBarberDay.setThirteen(true);
                    uploadAppointment(appointment, barberUid, customerUid);
                }
                if(hour.equals("14:00") && !auxBarberDay.isTwelve()){
                    auxBarberDay.setFourteen(true);
                    uploadAppointment(appointment, barberUid, customerUid);
                }
                if(hour.equals("15:00") && !auxBarberDay.isFifteen()){
                    auxBarberDay.setFifteen(true);
                    uploadAppointment(appointment, barberUid, customerUid);
                }
                if(hour.equals("16:00")&& !auxBarberDay.isSixteen()){
                    auxBarberDay.setSixteen(true);
                    uploadAppointment(appointment, barberUid, customerUid);
                }
                if(hour.equals("17:00") && !auxBarberDay.isSeventeen()){
                    auxBarberDay.setSeventeen(true);
                    uploadAppointment(appointment, barberUid, customerUid);
                }
                if(hour.equals("18:00")&& !auxBarberDay.isEightteen()){
                    auxBarberDay.setEightteen(true);
                    uploadAppointment(appointment, barberUid, customerUid);
                }
                if(hour.equals("19:00") && !auxBarberDay.isNinteen()){
                    auxBarberDay.setNinteen(true);
                    uploadAppointment(appointment, barberUid, customerUid);
                }
                if(hour.equals("20:00") && !auxBarberDay.isTwenty()){
                    auxBarberDay.setTwenty(true);
                    uploadAppointment(appointment, barberUid, customerUid);
                }

                ref.setValue(auxBarberDay);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void uploadAppointment(Appointment appointment, String barberUid, String customerUid){
        String appointKey = new Nodes().user(customerUid).child("appointments").push().getKey();
        new Nodes().appointments(barberUid).child(appointKey).setValue(appointment);
        new Nodes().user(customerUid).child("appointments").child(appointKey).setValue(appointment);
    }
}
