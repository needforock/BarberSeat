package ve.needforock.barberseat.views;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ve.needforock.barberseat.R;
import ve.needforock.barberseat.adapters.DayAdapter;
import ve.needforock.barberseat.adapters.DayListener;
import ve.needforock.barberseat.data.AppointmentToFireBase;
import ve.needforock.barberseat.data.Nodes;
import ve.needforock.barberseat.models.BarberDay;

public class DayView extends AppCompatActivity implements DayListener{

    private RecyclerView recyclerView;
    private DayAdapter dayAdapter;
    private String barberUid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_view);

       barberUid = getIntent().getStringExtra(BarberSelectionActivity.BARBER_UID);
        final Date date = new Date(getIntent().getLongExtra(BarberSelectionActivity.SELECTED_DATE, -1));





        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        final DatabaseReference barberAppDay = new Nodes().appointmentDay(barberUid)
                .child(String.valueOf(cal.get(cal.YEAR)))
                .child(String.valueOf(cal.get(cal.MONTH)))
                .child(String.valueOf(cal.get(cal.DAY_OF_MONTH)));

        barberAppDay.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    BarberDay barberDay = dataSnapshot.getValue(BarberDay.class);

                    if(barberDay!=null){
                        List<Boolean> hours = new ArrayList<>();
                        hours.add(barberDay.isNine());
                        hours.add(barberDay.isTen());
                        hours.add(barberDay.isEleven());
                        hours.add(barberDay.isTwelve());
                        hours.add(barberDay.isThirteen());
                        hours.add(barberDay.isFourteen());
                        hours.add(barberDay.isFifteen());
                        hours.add(barberDay.isSixteen());
                        hours.add(barberDay.isSeventeen());
                        hours.add(barberDay.isEightteen());
                        hours.add(barberDay.isNinteen());
                        hours.add(barberDay.isTwenty());
                        recyclerView = findViewById(R.id.hourRv);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(linearLayoutManager);
                        dayAdapter = new DayAdapter(date,hours, DayView.this);
                        recyclerView.setAdapter(dayAdapter);
                    }else{
                        BarberDay barberDay1 = new BarberDay();
                        barberDay1.setDate(date);
                        barberAppDay.setValue(barberDay1);
                        List<Boolean> hours = new ArrayList<>();
                        for (int i = 0; i < 12; i++) {
                            hours.add(i, false);
                        }

                        recyclerView = findViewById(R.id.hourRv);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(linearLayoutManager);
                        dayAdapter = new DayAdapter(date, hours, DayView.this);
                        recyclerView.setAdapter(dayAdapter);
                    }

                }else{
                    Toast.makeText(DayView.this, "No hay dia", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    @Override
    public void clickedHour(final String hour, final Date date) {
        // 1. Instantiate an AlertDialog.Builder with its constructor
        final AlertDialog.Builder builder = new AlertDialog.Builder(DayView.this);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String day = String.valueOf(cal.get(cal.DAY_OF_MONTH));
        String month = String.valueOf(cal.get(cal.MONTH)+1);
        String year = String.valueOf(cal.get(cal.YEAR));


// 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage(day + "-" + month + "-" + year + " a las " + hour)
                .setTitle("Desea reservar la siguiente hora?");


        // Add the buttons
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            new AppointmentToFireBase().SaveAppointment(DayView.this, barberUid,date,hour);
            finish();

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

// 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
