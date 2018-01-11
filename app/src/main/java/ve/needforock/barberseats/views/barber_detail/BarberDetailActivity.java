package ve.needforock.barberseats.views.barber_detail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RatingBar;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.Map;

import ve.needforock.barberseats.R;
import ve.needforock.barberseats.models.Barber;
import ve.needforock.barberseats.views.appointment.AppointmentFragment;
import ve.needforock.barberseats.views.barber_selection.BarberSelectionActivity;
import ve.needforock.barberseats.views.day.BarberCallBack;
import ve.needforock.barberseats.views.day.BarberPresenter;

public class BarberDetailActivity extends AppCompatActivity implements RatingCallBack, BarberCallBack {

    private String barberUid1,barberUid2, specialties;
    private TextView name, phone, specialtiesTv;
    private  RatingBar ratingBar;
    private CircularImageView barberPhoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barber_detail);
        getSupportActionBar().setTitle("Detalle de Barbero");

        barberUid1 = getIntent().getStringExtra(BarberSelectionActivity.BARBER1_UID);
        barberUid2 = getIntent().getStringExtra(AppointmentFragment.BARBER2_UID);

        barberPhoto = (CircularImageView) findViewById(R.id.detailAvatarCiv);
        name = (TextView) findViewById(R.id.detailNameTv);
        phone = (TextView) findViewById(R.id.detailPhoneTv);
        specialtiesTv = (TextView) findViewById(R.id.specialtiesTv);
        specialties="";
        ratingBar = (RatingBar) findViewById(R.id.ratingbar);

        if(barberUid2 == null){

            new BarberPresenter(this).checkBarber(barberUid1);
            new RatingPresenter(BarberDetailActivity.this).checkRateNoNull(barberUid1);
            new BarberPresenter(this).checkJobs(barberUid1);

        }

        if(barberUid1 == null){

            new BarberPresenter(this).checkBarber(barberUid2);
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
        Picasso.with(this).load(barber.getPhoto()).into(barberPhoto);
    }

    @Override
    public void jobsChecked(String specialties) {
        specialtiesTv.setText(specialties);
    }
}
