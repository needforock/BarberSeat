package ve.needforock.barberseat.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.Query;

import java.util.ArrayList;

import ve.needforock.barberseat.R;
import ve.needforock.barberseat.adapters.JobAdapter;
import ve.needforock.barberseat.adapters.JobListener;
import ve.needforock.barberseat.data.Queries;
import ve.needforock.barberseat.models.Barber;
import ve.needforock.barberseat.models.Job;

public class JobSelectionActivity extends AppCompatActivity implements JobListener {

    public static final String SELECTED_JOB = "ve.needforock.barberseat.views.KEY.SELECTED_JOB";
    private RecyclerView recyclerView;
    private JobAdapter jobAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Query query = new Queries().BarberJobs();


        recyclerView = findViewById(R.id.jobRv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        jobAdapter = new JobAdapter(this, query);
        recyclerView.setAdapter(jobAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public void clicked(Job job) {
        if (job.getBarberList() != null) {
            ArrayList<Barber> barbers = new ArrayList<>();
            barbers.addAll(job.getBarberList());

            Toast.makeText(JobSelectionActivity.this, job.getName(), Toast.LENGTH_SHORT).show();
            Toast.makeText(JobSelectionActivity.this, String.valueOf(barbers.get(0).getName()), Toast.LENGTH_SHORT).show();
            Toast.makeText(JobSelectionActivity.this, String.valueOf(barbers.get(1).getName()), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(JobSelectionActivity.this, BarberSelectionActivity.class);
            intent.putExtra(SELECTED_JOB, barbers);
            startActivity(intent);
        }
    }
}
