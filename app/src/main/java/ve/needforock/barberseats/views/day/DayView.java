package ve.needforock.barberseats.views.day;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import ve.needforock.barberseats.R;
import ve.needforock.barberseats.adapters.DayAdapter;
import ve.needforock.barberseats.adapters.DayListener;
import ve.needforock.barberseats.data.AppointmentToFireBase;
import ve.needforock.barberseats.models.Barber;
import ve.needforock.barberseats.models.Job;
import ve.needforock.barberseats.views.barber_selection.BarberSelectionActivity;


public class DayView extends AppCompatActivity implements DayListener, CheckHourCallBack, BarberCallBack {

    private RecyclerView recyclerView;
    private DayAdapter dayAdapter;
    private String barberUid, jobName, day, month, year, realMonth;
    private Job job;
    private String barberName;
    private Date date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_view);
        barberUid = getIntent().getStringExtra(BarberSelectionActivity.BARBER1_UID);
        date = new Date(getIntent().getLongExtra(BarberSelectionActivity.SELECTED_DATE, -1));

        job = (Job) getIntent().getSerializableExtra(BarberSelectionActivity.JOB);
        jobName = job.getName();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        year = String.valueOf(cal.get(Calendar.YEAR));
        month = String.valueOf(cal.get(Calendar.MONTH));
        realMonth = String.valueOf(cal.get(Calendar.MONTH) + 1);
        day = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));

        getSupportActionBar().setTitle(jobName + " el " + day + "-" + realMonth + "-" + year);

        new BarberPresenter(this).checkDay(barberUid, year, month, day);

        new BarberPresenter(this).checkBarber(barberUid);


    }


    @Override
    public void clickedHour(final String hour) {
        new CheckHourPresenter(this).checkHour(hour, date, barberUid);
    }

    @Override
    public void available(final String hour, final Date date) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(DayView.this);
        builder.setTitle("Confirma tu reserva:")
                .setMessage("¿Desea reservar las " + hour + " horas del dia " + day + "-" + realMonth + "-" + year + " para " + jobName + " con " + barberName + "?");

        builder.setPositiveButton("RESERVAR", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                new AppointmentToFireBase().SaveAppointment(barberUid, date, hour, jobName);
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();

            }
        });
        builder.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    @Override
    public void noAvailable() {
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        Toast.makeText(this, "Hora no Disponible, Seleccione otra Hora", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void dayChecked(Map<String, Boolean> map) {
        recyclerView = (RecyclerView) findViewById(R.id.hourRv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        dayAdapter = new DayAdapter( map, DayView.this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(dayAdapter);

    }

    @Override
    public void barberChecked(Barber barber) {
        barberName = barber.getName();
    }

    @Override
    public void jobsChecked(String specialties) {

    }
}
