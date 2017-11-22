package ve.needforock.barberseat.data;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import ve.needforock.barberseat.models.Appointment;
import ve.needforock.barberseat.models.BarberDay;

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
        hour = String.valueOf(calendar.get(calendar.HOUR_OF_DAY));

        Log.d("FECHA", year + month + day + hour);
        final DatabaseReference ref3 = new Nodes().appointmentDay(appointment.getBarberUid()).child(year).child(month).child(day);
        ref3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                BarberDay auxBarberDay = dataSnapshot.getValue(BarberDay.class);
                if(hour.equals("9")){
                    auxBarberDay.setNine(false);
                }
                if(hour.equals("10")){
                    auxBarberDay.setTen(false);
                }
                if(hour.equals("11")){
                    auxBarberDay.setEleven(false);
                }
                if(hour.equals("12")){
                    auxBarberDay.setTwelve(false);
                }
                if(hour.equals("13")){
                    auxBarberDay.setThirteen(false);
                }
                if(hour.equals("14")){
                    auxBarberDay.setFourteen(false);
                }
                if(hour.equals("15")){
                    auxBarberDay.setFifteen(false);
                }
                if(hour.equals("16")){
                    auxBarberDay.setSixteen(false);
                }
                if(hour.equals("17")){
                    auxBarberDay.setSeventeen(false);
                }
                if(hour.equals("18")){
                    auxBarberDay.setEightteen(false);
                }
                if(hour.equals("19")){
                    auxBarberDay.setNinteen(false);
                }
                if(hour.equals("20")){
                    auxBarberDay.setTwenty(false);
                }
                ref3.setValue(auxBarberDay);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
