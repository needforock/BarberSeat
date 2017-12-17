package ve.needforock.barberseat.views.appointment_detail;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;

import ve.needforock.barberseat.R;
import ve.needforock.barberseat.data.DeleteAppointment;
import ve.needforock.barberseat.data.Nodes;
import ve.needforock.barberseat.models.Appointment;
import ve.needforock.barberseat.models.Barber;
import ve.needforock.barberseat.views.appointment.AppointmentFragment;
import ve.needforock.barberseat.views.barber_detail.RatingCallBack;
import ve.needforock.barberseat.views.barber_detail.RatingPresenter;

public class AppointmentDetailActivity extends AppCompatActivity implements DayCallBack, RatingCallBack{

    private Appointment appointment;
    private String year, month, day, hour, realMonth, barberUid;
    private TextView dateTv;
    private RatingBar appointmentRating;
    private float rate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        appointment = (Appointment) getIntent().getSerializableExtra(AppointmentFragment.APPOINTMENT);

        getSupportActionBar().setTitle("Detalle de Reserva");

        final TextView name = findViewById(R.id.barberNameTv);
        final TextView phone = findViewById(R.id.barberPhoneTv);
        final TextView job = findViewById(R.id.appJobTv);
        TextView isRated = findViewById(R.id.isRatedTv);
        dateTv = findViewById(R.id.appointmentDateTv);

        barberUid = appointment.getBarberUid();


        Date date = appointment.getDate();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        year = String.valueOf(cal.get(cal.YEAR));
        month = String.valueOf(cal.get(cal.MONTH));
        realMonth = String.valueOf(cal.get(cal.MONTH)+1);
        day = String.valueOf(cal.get(cal.DAY_OF_MONTH));
        appointmentRating = findViewById(R.id.appointmentRating);


        new DayValidation(AppointmentDetailActivity.this).validate(cal.get(cal.DAY_OF_WEEK));
        Date currentTime = Calendar.getInstance().getTime();
        Log.d("TIME", String.valueOf(currentTime.getTime()));
        Log.d("TIME", String.valueOf(date.getTime()));

        if(appointment.isRated()){
            appointmentRating.setVisibility(View.GONE);
            isRated.setVisibility(View.VISIBLE);
            isRated.setText("Ya Evaluaste Esta Cita");
        }
        if(date.getTime()<currentTime.getTime() && !appointment.isRated()){

            isRated.setVisibility(View.VISIBLE);
            isRated.setText("Evalúa la calidad del servicio");
        } else if(date.getTime()>currentTime.getTime()){
            appointmentRating.setVisibility(View.GONE);
            isRated.setVisibility(View.GONE);
        }

        appointmentRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rateV, boolean b) {
                rate = rateV;

               new RatingPresenter(AppointmentDetailActivity.this).checkAppointmentIsRated(appointment);
            }
        });


        DatabaseReference barber = new Nodes().barber(barberUid);
        barber.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Barber auxBarber = dataSnapshot.getValue(Barber.class);
                name.setText(auxBarber.getName());
                phone.setText(auxBarber.getPhone());
                job.setText(appointment.getJob());

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

    @Override
    public void dayInString(String dayString) {
        dateTv.setText(dayString + " " + day + "-" + realMonth + "-" + year);
    }

    @Override
    public void ratingSuccess() {
        appointmentRating.setIsIndicator(true);
        Toast.makeText(this, "Evaluacion Realizada", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void rateChecked(float rating) {
        appointmentRating.setRating(rating);
    }

    @Override
    public void ratingNoSuccess() {
        Toast.makeText(this, "No se puede evaluar mas de una vez", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void doRating() {
        new RatingPresenter(AppointmentDetailActivity.this).rateBarber(barberUid, rate);
    }
}
