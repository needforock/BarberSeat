package ve.needforock.barberseat.views.barber_detail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import ve.needforock.barberseat.R;
import ve.needforock.barberseat.data.Nodes;
import ve.needforock.barberseat.models.Barber;
import ve.needforock.barberseat.views.appointment.BarberSelectionActivity;

public class BarberDetailActivity extends AppCompatActivity {

    private String barberUid;
    private TextView name, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barber_detail);


        barberUid = getIntent().getStringExtra(BarberSelectionActivity.BARBER_UID);
        name = findViewById(R.id.detailNameTv);
        phone = findViewById(R.id.detailPhoneTv);


        DatabaseReference barber = new Nodes().barber(barberUid);
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
}
