package ve.needforock.barberseat.views.appointment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.Query;

import java.util.ArrayList;

import ve.needforock.barberseat.R;
import ve.needforock.barberseat.adapters.JobAdapter;
import ve.needforock.barberseat.adapters.JobListener;
import ve.needforock.barberseat.data.Queries;
import ve.needforock.barberseat.models.Barber;
import ve.needforock.barberseat.models.Job;

/**
 * A simple {@link Fragment} subclass.
 */
public class JobFragment extends Fragment implements JobListener {

    private RecyclerView recyclerView;
    private JobAdapter jobAdapter;
    public static final String BARBERS = "ve.needforock.barberseat.views.appointment.KEY.BARBERS";
    public static final String SELECTED_JOB ="ve.needforock.barberseat.views.appointment.KEY.SELECTED_JOB";



    public JobFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_job, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        Query query = new Queries().BarberJobs();
        recyclerView = view.findViewById(R.id.jobRv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        jobAdapter = new JobAdapter(this, query, getContext());
        recyclerView.setAdapter(jobAdapter);



    }

    @Override
    public void clicked(Job job) {
        if (job.getBarberList() != null) {
            ArrayList<Barber> barbers = new ArrayList<>();
            barbers.addAll(job.getBarberList());
            Intent intent = new Intent(getActivity(), BarberSelectionActivity.class);
            intent.putExtra(BARBERS, barbers);
            intent.putExtra(SELECTED_JOB, job);
            startActivity(intent);

        }
    }
}
