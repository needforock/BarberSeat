package ve.needforock.barberseats.views.barber_selection;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.roomorama.caldroid.CaldroidFragment;

import java.util.ArrayList;
import java.util.Date;

import ve.needforock.barberseats.R;
import ve.needforock.barberseats.adapters.BarberAdapter;
import ve.needforock.barberseats.adapters.BarberListener;
import ve.needforock.barberseats.models.Barber;
import ve.needforock.barberseats.models.Job;
import ve.needforock.barberseats.views.barber_detail.BarberDetailActivity;
import ve.needforock.barberseats.views.calendar.SelectedDateCallBack;
import ve.needforock.barberseats.views.calendar.SetCalendar;
import ve.needforock.barberseats.views.day.DayView;
import ve.needforock.barberseats.views.job_selection.JobFragment;

public class BarberSelectionActivity extends AppCompatActivity implements BarberListener, SelectedDateCallBack {

    public static final String JOB = "ve.needforock.barberseat.views.appointment.KEY.JOB";
    public static final String SELECTED_DATE = "ve.needforock.barberseat.views.appointment.KEY.SELECTED_DATE" ;
    public static final String BARBER1_UID = "ve.needforock.barberseat.views.appointment.KEY.BARBER1_UID";
    private static final int RC_CODE = 363;


    private ArrayList<Barber> barbers;
    private RecyclerView recyclerView;
    private BarberAdapter barberAdapter;
    private CaldroidFragment dialogCaldroidFragment,caldroidFragment;
    private Bundle state;
    private Job job;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barber_selection);

        barbers = (ArrayList<Barber>) getIntent().getSerializableExtra(JobFragment.BARBERS);
        job  = (Job) getIntent().getSerializableExtra(JobFragment.SELECTED_JOB);

        getSupportActionBar().setTitle("Barberos que hacen " + job.getName());

        recyclerView = (RecyclerView) findViewById(R.id.barberRv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        barberAdapter = new BarberAdapter(barbers, this);
        recyclerView.setAdapter(barberAdapter);
        state = savedInstanceState;
        caldroidFragment = new CaldroidFragment();

    }

    @Override
    public void reserveClicked(String barberUid) {
        dialogCaldroidFragment = new CaldroidFragment();
        final String dialogTag = "CALDROID_DIALOG_FRAGMENT";
        if (state != null) {
            dialogCaldroidFragment.restoreDialogStatesFromKey(
                    getSupportFragmentManager(), state,
                    "DIALOG_CALDROID_SAVED_STATE", dialogTag);
            Bundle args = dialogCaldroidFragment.getArguments();
            if (args == null) {
                args = new Bundle();
                dialogCaldroidFragment.setArguments(args);
            }
        } else {
            // Setup arguments
            Bundle bundle = new Bundle();
            // Setup dialogTitle
            dialogCaldroidFragment.setArguments(bundle);
        }

        dialogCaldroidFragment.show(getSupportFragmentManager(),
                dialogTag);
        new SetCalendar(this).Set(barberUid, dialogCaldroidFragment);

    }

    @Override
    public void seeClicked(String barberUid) {
        Intent intent = new Intent(BarberSelectionActivity.this, BarberDetailActivity.class);
        intent.putExtra(BARBER1_UID, barberUid);
        startActivity(intent);
    }


    @Override
    public void selectedDate(Date date, String barberUid) {

        Intent intent = new Intent(BarberSelectionActivity.this, DayView.class);
        intent.putExtra(SELECTED_DATE, date.getTime());
        intent.putExtra(BARBER1_UID, barberUid);
        intent.putExtra(JOB, job);
        startActivityForResult(intent, RC_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_CODE){
            if (resultCode == RESULT_OK){
                Toast.makeText(this, "Reserva Realizada", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 600);

            }
        }
    }
}
