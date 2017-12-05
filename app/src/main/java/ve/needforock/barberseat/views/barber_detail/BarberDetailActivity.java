package ve.needforock.barberseat.views.barber_detail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import ve.needforock.barberseat.R;
import ve.needforock.barberseat.data.Nodes;
import ve.needforock.barberseat.models.Barber;
import ve.needforock.barberseat.views.appointment.AppointmentFragment;
import ve.needforock.barberseat.views.appointment.BarberSelectionActivity;

public class BarberDetailActivity extends AppCompatActivity {

    private String barberUid1,barberUid2, specialties;
    private TextView name, phone, specialtiesTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barber_detail);

        getSupportActionBar().setTitle("Detalle de Barbero");


        barberUid1 = getIntent().getStringExtra(BarberSelectionActivity.BARBER1_UID);
        barberUid2 = getIntent().getStringExtra(AppointmentFragment.BARBER2_UID);


        name = findViewById(R.id.detailNameTv);
        phone = findViewById(R.id.detailPhoneTv);
        specialtiesTv = findViewById(R.id.specialtiesTv);
        specialties="";



        if(barberUid2 == null){
            DatabaseReference barber = new Nodes().barber(barberUid1);
            barber.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Barber auxBarber = dataSnapshot.getValue(Barber.class);
                    name.setText(auxBarber.getName());
                    phone.setText(auxBarber.getPhone());

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        if(barberUid1 == null){
            DatabaseReference barber = new Nodes().barber(barberUid2);
            barber.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Barber auxBarber = dataSnapshot.getValue(Barber.class);
                    name.setText(auxBarber.getName());
                    phone.setText(auxBarber.getPhone());

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        checkJobs(barberUid1, barberUid2);




    }

    public void checkJobs(final String barberUid1, final String barberUid2){
        DatabaseReference cut = new Nodes().barber(barberUid1);
        cut.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Barber auxBarber = dataSnapshot.getValue(Barber.class);
               Map<String, String> map = auxBarber.getJobs();

                for (Map.Entry<String, String> entry: map.entrySet()){
                    if(specialties.trim().length()==0){
                        specialties =entry.getValue();
                    }else{
                        specialties = specialties + ", " + entry.getValue();
                    }

                }


                specialtiesTv.setText(specialties);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

       /* DatabaseReference shave = new Nodes().jobs().child("rasurado");
        shave.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Job auxJob = dataSnapshot.getValue(Job.class);
                List<Barber> barberList = auxJob.getBarberList();
                for (Barber barber: barberList) {
                    if(barber.getUid().equals(barberUid1) || barber.getUid().equals(barberUid2));
                    specialties = specialties + auxJob.getName();


                }
                specialtiesTv.setText(specialties);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

    }
}
