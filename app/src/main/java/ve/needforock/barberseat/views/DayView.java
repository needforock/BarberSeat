package ve.needforock.barberseat.views;

import android.os.Bundle;
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
import ve.needforock.barberseat.data.Nodes;
import ve.needforock.barberseat.models.BarberDay;

public class DayView extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DayAdapter dayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_view);

        String barberUid = getIntent().getStringExtra(BarberSelectionActivity.BARBER_UID);
        final Date date = new Date(getIntent().getLongExtra(BarberSelectionActivity.SELECTED_DATE, -1));

        Toast.makeText(this, String.valueOf(date), Toast.LENGTH_SHORT).show();



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
                        dayAdapter = new DayAdapter(hours);
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
                        dayAdapter = new DayAdapter(hours);
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


}
