package ve.needforock.barberseat.views.barber_detail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

import java.util.Map;

import ve.needforock.barberseat.R;
import ve.needforock.barberseat.data.Nodes;
import ve.needforock.barberseat.models.Barber;
import ve.needforock.barberseat.views.appointment.AppointmentFragment;
import ve.needforock.barberseat.views.appointment.BarberSelectionActivity;
import ve.needforock.barberseat.views.day.BarberCallBack;
import ve.needforock.barberseat.views.day.BarberPresenter;

public class BarberDetailActivity extends AppCompatActivity implements RatingCallBack, BarberCallBack {

    private String barberUid1,barberUid2, specialties;
    private TextView name, phone, specialtiesTv;
    private  RatingBar ratingBar;
    private  float ratingValue;

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
        ratingBar = findViewById(R.id.ratingbar);

        if(barberUid2 == null){
            DatabaseReference barber = new Nodes().barber(barberUid1);
            new BarberPresenter(this).checkBarber(barber);
            new RatingPresenter(BarberDetailActivity.this).checkRateNoNull(barberUid1);
            new BarberPresenter(this).checkJobs(barberUid1);

        }

        if(barberUid1 == null){
            DatabaseReference barber = new Nodes().barber(barberUid2);
            new BarberPresenter(this).checkBarber(barber);
            new RatingPresenter(BarberDetailActivity.this).checkRateNoNull(barberUid2);
            new BarberPresenter(this).checkJobs(barberUid2);

        }
    }


    @Override
    public void ratingSuccess() {

    }

    @Override
    public void rateChecked(float rating) {
        ratingBar.setRating(rating);
    }

    @Override
    public void ratingNoSuccess() {

    }

    @Override
    public void doRating() {

    }

    @Override
    public void dayChecked(Map<String, Boolean> map) {

    }

    @Override
    public void barberChecked(Barber barber) {
        name.setText(barber.getName());
        phone.setText(barber.getPhone());
    }

    @Override
    public void jobsChecked(String specialties) {
        specialtiesTv.setText(specialties);
    }
}
