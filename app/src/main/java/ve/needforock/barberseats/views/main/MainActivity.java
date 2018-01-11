package ve.needforock.barberseats.views.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ve.needforock.barberseats.R;
import ve.needforock.barberseats.data.CurrentUser;
import ve.needforock.barberseats.data.UserToFireBase;
import ve.needforock.barberseats.models.Barber;
import ve.needforock.barberseats.models.Job;
import ve.needforock.barberseats.models.Rating;
import ve.needforock.barberseats.views.appointment.AddAppointmentRequestListener;
import ve.needforock.barberseats.views.appointment.AppointmentFragment;
import ve.needforock.barberseats.views.job_selection.JobFragment;
import ve.needforock.barberseats.views.login.LoginActivity;
import ve.needforock.barberseats.views.top_rated.TopRatedFragment;
import ve.needforock.barberseats.views.user_detail.UserCallBack;
import ve.needforock.barberseats.views.user_detail.UserDetailActivity;
import ve.needforock.barberseats.views.user_detail.UserPresenter;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, UserCallBack, AddAppointmentRequestListener {

    private UserPresenter userPresenter;
    private TextView userName, email;
    private String userUid;
    private Uri userImageUri;
    private CircularImageView circularImageView;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Fragment fragment = null;
        Class fragmentClass = null;
        fragmentClass = JobFragment.class;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        getSupportActionBar().setTitle("Reservar");

        //////////////////////Manejar Header del Drawer///////////////////////

        //Registrar el barbero en firebase database
        final Barber newBarber = new Barber();
        newBarber.setPhone("+5612345678");
        newBarber.setUid("456");
        newBarber.setName("Pedrito Perez");
        Map<String, String> map1 = new HashMap<>();
        map1.put(String.valueOf(System.currentTimeMillis()), "Corte");
        map1.put(String.valueOf(System.currentTimeMillis()+1), "Rasurado");
        newBarber.setJobs(map1);

        Rating rating = new Rating();
        rating.setRatingTimes(0);
        rating.setRating(0);
        /*Map<String ,Boolean> stars = new HashMap<>();
        stars.put("x", false);
        rating.setStars(stars);
        new Nodes().barberRating("456").setValue(rating);*/




        final Barber newBarber2 = new Barber();
        newBarber2.setPhone("+5645678941");
        newBarber2.setUid("457");
        newBarber2.setName("Juanito Alimana");
        newBarber2.setJobs(map1);

        //new RatingPresenter(MainActivity.this).rateBarber(newBarber2.getUid(), 0);



        Job shave = new Job();
        shave.setName("Rasurado");
        List<Barber> barbers = new ArrayList<>();
        barbers.add(newBarber);
        barbers.add(newBarber2);
        shave.setBarberList(barbers);
        //new Nodes().jobs().child("rasurado").setValue(shave);


        Job cut = new Job();
        cut.setName("Corte");
        List<Barber> barbers2 = new ArrayList<>();
        barbers2.add(newBarber);
        barbers2.add(newBarber2);
        cut.setBarberList(barbers2);
        //new Nodes().jobs().child("corte").setValue(cut);


       // new Nodes().barber(newBarber.getUid()).setValue(newBarber);
       // new Nodes().barber(newBarber2.getUid()).setValue(newBarber2);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);

        userName = header.findViewById(R.id.userName2Tv);
        email = header.findViewById(R.id.email2Tv);
        circularImageView =  header.findViewById(R.id.avatar2Ci);

        firebaseUser = new CurrentUser().getCurrentUser();
        userImageUri = firebaseUser.getPhotoUrl();


        email.setText(firebaseUser.getEmail());
        userName.setText(firebaseUser.getDisplayName());
        userUid = firebaseUser.getUid();

        userPresenter=new UserPresenter(this);
        userPresenter.startListening(userUid);

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



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;
        Class fragmentClass = null;

        if (id == R.id.nav_appointments) {
            fragmentClass = AppointmentFragment.class;
            setFragment(fragment, fragmentClass);
            getSupportActionBar().setTitle("Mis Reservas");

        } else if (id == R.id.nav_book) {
            fragmentClass = JobFragment.class;
            setFragment(fragment, fragmentClass);
            getSupportActionBar().setTitle("Reservar");


        }else if (id == R.id.best_rated) {
            fragmentClass = TopRatedFragment.class;
            setFragment(fragment, fragmentClass);
            getSupportActionBar().setTitle("Mejor Evaluados");

        } else if (id == R.id.nav_profile) {
            Intent intent = new Intent(MainActivity.this, UserDetailActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            finish();
                        }

                    });

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setFragment (Fragment fragment, Class fragmentClass){

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

    }


    @Override
    public void photoNoNull(String photo) {
        Picasso.with(this).invalidate(photo);
        Picasso.with(this).load(photo).into(circularImageView);

    }

    @Override
    public void userPhoneNoNull(String phone) {

    }

    @Override
    public void userPhoneNull() {

    }

    @Override
    public void userNull() {
        new UserToFireBase().SaveUserToFireBase(userImageUri);

    }

    @Override
    public void photoNull() {

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("TAG", "onStart: ");
        userPresenter.startListening(userUid);

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("TAG", "onStop: ");
        userPresenter.stopListening();
    }

    @Override
    public void addRequested() {
        Class fragmentClass = JobFragment.class;
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        getSupportActionBar().setTitle("Reservar");
    }
}
