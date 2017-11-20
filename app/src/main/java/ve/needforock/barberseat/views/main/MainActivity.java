package ve.needforock.barberseat.views.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ve.needforock.barberseat.R;
import ve.needforock.barberseat.adapters.AppointmentAdapter;
import ve.needforock.barberseat.data.CurrentUser;
import ve.needforock.barberseat.data.Nodes;
import ve.needforock.barberseat.data.Queries;
import ve.needforock.barberseat.models.Appointment;
import ve.needforock.barberseat.models.Barber;
import ve.needforock.barberseat.models.BarberDay;
import ve.needforock.barberseat.models.Job;
import ve.needforock.barberseat.views.JobSelectionActivity;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerView;
    private AppointmentAdapter appointmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        final String customerUid = new CurrentUser().getUid();

        //Registrar el barbero en firebase database
        final Barber newBarber = new Barber();
        newBarber.setPhone("+5612345678");
        newBarber.setUid("456");
        newBarber.setName("pedrito perez");
        new Nodes().barber(newBarber.getUid()).setValue(newBarber);

        final Barber newBarber2 = new Barber();
        newBarber2.setPhone("+5645678941");
        newBarber2.setUid("457");
        newBarber2.setName("juanito alimana");
        new Nodes().barber(newBarber2.getUid()).setValue(newBarber2);


        Query query = new Queries().CustomerAppointments(customerUid);


        recyclerView = findViewById(R.id.appointmentRv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        appointmentAdapter = new AppointmentAdapter(query);
        recyclerView.setAdapter(appointmentAdapter);





        Calendar finalCal = Calendar.getInstance();

        Appointment appointment1 = new Appointment();
        appointment1.setBarberUid("456");
        appointment1.setBarberName("pedrito perez");
        appointment1.setUserUID(customerUid);





        finalCal.set(117 + 1900, 10, 12, 13, 0);
        Date appDate =  finalCal.getTime();
        appointment1.setDate(appDate);

        BarberDay barberDay1 = new BarberDay();
        barberDay1.setThirteen(true);
        barberDay1.setDate(appDate);

        /*String appointKey = new Nodes().user(customerUid).child("appointments").push().getKey();
        new Nodes().user(customerUid).child("appointments").child(appointKey).setValue(appointment1);
        new Nodes().appointments(appointment1.getBarberUid()).child(appointKey).setValue(appointment1);
        new Nodes().appointmentDay(appointment1.getBarberUid())
                .child(String.valueOf(finalCal.get(finalCal.YEAR)))
                .child(String.valueOf(finalCal.get(finalCal.MONTH)))
                .child(String.valueOf(finalCal.get(finalCal.DAY_OF_MONTH)))
                .setValue(barberDay1);*/

        Calendar finalCal2 = Calendar.getInstance();
        Appointment appointment2 = new Appointment();
        appointment2.setBarberUid("456");
        appointment2.setBarberName("pedrito perez");
        appointment2.setUserUID(customerUid);
        finalCal2.set(118 + 1900, 11, 18, 9, 0);
        Date appDate2 =  finalCal2.getTime();
        appointment2.setDate(appDate2);

        BarberDay barberDay2 = new BarberDay();
        barberDay2.setNine(true);
        barberDay2.setDate(appDate2);

       /* String appointKey2 = new Nodes().user(customerUid).child("appointments").push().getKey();
        new Nodes().user(customerUid).child("appointments").child(appointKey2).setValue(appointment2);
        new Nodes().appointments(appointment2.getBarberUid()).child(appointKey2).setValue(appointment2);
        new Nodes().appointmentDay(appointment1.getBarberUid())
                .child(String.valueOf(finalCal2.get(finalCal2.YEAR)))
                .child(String.valueOf(finalCal2.get(finalCal2.MONTH)))
                .child(String.valueOf(finalCal2.get(finalCal2.DAY_OF_MONTH)))
                .setValue(barberDay2);*/


        Job shave = new Job();
        shave.setName("shave");
        List<Barber> barbers = new ArrayList<>();
        barbers.add(newBarber);
        barbers.add(newBarber2);
        shave.setBarberList(barbers);
        new Nodes().jobs().child("shave").setValue(shave);


        Job cut = new Job();
        cut.setName("cut");
        List<Barber> barbers2 = new ArrayList<>();
        barbers2.add(newBarber);
        barbers2.add(newBarber2);
        cut.setBarberList(barbers2);
        new Nodes().jobs().child("cut").setValue(cut);








        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, JobSelectionActivity.class);
                startActivity(intent);

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
