package ve.needforock.barberseat.views.appointment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ve.needforock.barberseat.R;
import ve.needforock.barberseat.adapters.DayAdapter;
import ve.needforock.barberseat.adapters.DayListener;
import ve.needforock.barberseat.data.AppointmentToFireBase;
import ve.needforock.barberseat.data.Nodes;
import ve.needforock.barberseat.models.Barber;
import ve.needforock.barberseat.models.Job;



public class DayView extends AppCompatActivity implements DayListener, CheckHourCallBack{

    private RecyclerView recyclerView;
    private DayAdapter dayAdapter;
    private String barberUid, jobName, day, month, year, realMonth;
    private Job job;
    private String barberName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_view);
       barberUid = getIntent().getStringExtra(BarberSelectionActivity.BARBER1_UID);
        final Date date = new Date(getIntent().getLongExtra(BarberSelectionActivity.SELECTED_DATE, -1));



        job = (Job) getIntent().getSerializableExtra(BarberSelectionActivity.JOB );
        jobName = job.getName();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        year = String.valueOf(cal.get(cal.YEAR));
        month = String.valueOf(cal.get(cal.MONTH));
        realMonth = String.valueOf(cal.get(cal.MONTH)+1);
        day = String.valueOf(cal.get(cal.DAY_OF_MONTH));

        getSupportActionBar().setTitle(jobName + " el " + day + "-" + realMonth + "-" + year);



        final DatabaseReference barberAppDay = new Nodes().appointmentDay(barberUid)
                .child(year)
                .child(month)
                .child(day);

        barberAppDay.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Boolean> map = new HashMap<>();
                if (dataSnapshot != null) {

                        for (DataSnapshot children : dataSnapshot.getChildren()) {
                            map.put(children.getKey(), children.getValue(Boolean.class));
                        }
                }
                recyclerView = findViewById(R.id.hourRv);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(linearLayoutManager);
                dayAdapter = new DayAdapter(date, map, DayView.this);
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                        linearLayoutManager.getOrientation());
                recyclerView.addItemDecoration(dividerItemDecoration);
                recyclerView.setAdapter(dayAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(DayView.this, "Problema de Conexion", Toast.LENGTH_SHORT).show();
            }
        });


        DatabaseReference barber = new Nodes().barber(barberUid);
        barber.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Barber auxBarber = dataSnapshot.getValue(Barber.class);
                barberName = auxBarber.getName();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

    }


    @Override
    public void clickedHour(final String hour, final Date date) {
        new CheckHourPresenter(this, this).checkHour(hour, date, barberUid);
    }

    @Override
    public void available(final String hour, final Date date) {
        // 1. Instantiate an AlertDialog.Builder with its constructor
        final AlertDialog.Builder builder = new AlertDialog.Builder(DayView.this);
       /* Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String day = String.valueOf(cal.get(cal.DAY_OF_MONTH));
        String month = String.valueOf(cal.get(cal.MONTH)+1);
        String year = String.valueOf(cal.get(cal.YEAR));
*/

// 2. Chain together various setter methods to set the dialog characteristics
        builder.setTitle("Confirma tu reserva:")
                .setMessage("¿Desea reservar las " + hour + " horas del dia " + day + "-" +realMonth + "-" + year + " para " + jobName + " con " + barberName + "?"  );


        // Add the buttons
        builder.setPositiveButton("RESERVAR", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                new AppointmentToFireBase().SaveAppointment(DayView.this, barberUid,date,hour, jobName);
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);

                finish();

            }
        });
        builder.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

// 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
        dialog.show();

    }

   @Override
    public void noAvailable() {
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        Toast.makeText(this, "Hora no Disponible, Seleccione otra Hora", Toast.LENGTH_SHORT).show();

    }
}
