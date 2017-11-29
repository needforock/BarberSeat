package ve.needforock.barberseat.views.appointment_detail;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import ve.needforock.barberseat.R;
import ve.needforock.barberseat.data.DeleteAppointment;
import ve.needforock.barberseat.data.Nodes;
import ve.needforock.barberseat.models.Appointment;
import ve.needforock.barberseat.models.Barber;
import ve.needforock.barberseat.views.appointment.AppointmentFragment;

public class AppointmentDetailActivity extends AppCompatActivity {

    private Appointment appointment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        appointment = (Appointment) getIntent().getSerializableExtra(AppointmentFragment.APPOINTMENT);

        getSupportActionBar().setTitle("");

        final TextView name = findViewById(R.id.barberNameTv);
        final TextView phone = findViewById(R.id.barberPhoneTv);
        final TextView job = findViewById(R.id.appJobTv);
        final TextView date = findViewById(R.id.appointmentDateTv);

        String barberUid = appointment.getBarberUid();

        DatabaseReference barber = new Nodes().barber(barberUid);
        barber.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Barber auxBarber = dataSnapshot.getValue(Barber.class);
                name.setText(auxBarber.getName());
                phone.setText(auxBarber.getPhone());
                job.setText(appointment.getJob());
                date.setText(appointment.getDate().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(AppointmentDetailActivity.this);
                // 2. Chain together various setter methods to set the dialog characteristics
                builder.setMessage("Desea Eliminar la reserva?");


                // Add the buttons
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        new DeleteAppointment().delete(appointment);
                        Toast.makeText(AppointmentDetailActivity.this, "Eliminada", Toast.LENGTH_SHORT).show();
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
        });
    }

}
