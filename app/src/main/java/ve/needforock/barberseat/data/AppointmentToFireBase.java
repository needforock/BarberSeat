package ve.needforock.barberseat.data;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;

import ve.needforock.barberseat.models.Appointment;
import ve.needforock.barberseat.models.BarberDay;
import ve.needforock.barberseat.models.Customer;

/**
 * Created by Soporte on 20-Nov-17.
 */

public class AppointmentToFireBase {


    private String customerUid;

    private int day, month, year, hours;
    public void SaveAppointment(final String barberUid, Date date, final String hour, String jobName){

        final Appointment appointment = new Appointment();
        appointment.setBarberUid(barberUid);
        customerUid = new CurrentUser().getUid();
        appointment.setUserUID(customerUid);
        appointment.setJob(jobName);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date);

        day = cal2.get(cal2.DAY_OF_MONTH);

        month = cal2.get(cal2.MONTH);
        year = cal2.get(cal2.YEAR);
        String[] fullHour = hour.split(":");
        hours = Integer.parseInt(fullHour[0]);
        int  minutes = Integer.parseInt(fullHour[1]);
        cal2.set(year, month, day, hours,minutes);
        Date finalDate = cal2.getTime();
        appointment.setDate(finalDate);





        prepareUpload(date, barberUid, appointment, hour);


    }

    public void prepareUpload(Date date, final String barberUid, final Appointment appointment, final String hour){

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

                ref.child(hour).setValue(true);
                uploadAppointment(appointment, barberUid, customerUid);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void uploadAppointment(Appointment appointment, String barberUid, String customerUid){
        String keyPush = new Nodes().userAppointment(customerUid).push().getKey();
        String dayString = String.valueOf(day);
        if (dayString.trim().length() == 1){
            dayString = "0" + dayString;
        }
        String dateKey = String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(dayString) + "-" + hours;
        String appointKey = dateKey + "_" + keyPush;
        appointment.setKey(appointKey);
        increaseAppointmentCount(customerUid);
        new Nodes().appointments(barberUid).child(appointKey).setValue(appointment);
        new Nodes().userAppointment(customerUid).child(appointKey).setValue(appointment);


    }

    public void increaseAppointmentCount(String customerUid){

        final DatabaseReference ref = new Nodes().user(customerUid);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Customer auxCustomer = dataSnapshot.getValue(Customer.class);
                auxCustomer.setAppointments(auxCustomer.getAppointments()+1);
                ref.setValue(auxCustomer);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


}
