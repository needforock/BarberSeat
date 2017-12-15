package ve.needforock.barberseat.views.top_rated;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.roomorama.caldroid.CaldroidFragment;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ve.needforock.barberseat.R;
import ve.needforock.barberseat.adapters.TopRatedAdapter;
import ve.needforock.barberseat.adapters.TopRatedListener;
import ve.needforock.barberseat.data.Nodes;
import ve.needforock.barberseat.data.Queries;
import ve.needforock.barberseat.models.Job;
import ve.needforock.barberseat.views.appointment.DayView;
import ve.needforock.barberseat.views.appointment.SelectedDateCallBack;
import ve.needforock.barberseat.views.appointment.SetCalendar;
import ve.needforock.barberseat.views.barber_detail.BarberDetailActivity;
import ve.needforock.barberseat.views.dialog_fragment.ReserveCallBack;
import ve.needforock.barberseat.views.dialog_fragment.ReserveDialogFragment;

import static ve.needforock.barberseat.views.appointment.AppointmentFragment.BARBER2_UID;
import static ve.needforock.barberseat.views.appointment.BarberSelectionActivity.BARBER1_UID;
import static ve.needforock.barberseat.views.appointment.BarberSelectionActivity.JOB;
import static ve.needforock.barberseat.views.appointment.BarberSelectionActivity.SELECTED_DATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class TopRatedFragment extends Fragment implements TopRatedListener, SelectedDateCallBack, ReserveCallBack {


    private RecyclerView recyclerView;
    private TopRatedAdapter topRatedAdapter;
    private CaldroidFragment dialogCaldroidFragment;
    private Bundle state;
    private String barberUID;
    DialogFragment dialogFragment;
    private Date selectedDate;

    public TopRatedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_top_rated, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Query query = new Queries().BarberRating().orderByChild("rating");

        recyclerView = view.findViewById(R.id.topRatedRv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        topRatedAdapter = new TopRatedAdapter(query, TopRatedFragment.this);

        recyclerView.setAdapter(topRatedAdapter);
        state = savedInstanceState;

    }

    @Override
    public void viewClicked(String barberUid) {
        Intent intent = new Intent(getActivity(), BarberDetailActivity.class);
        intent.putExtra(BARBER2_UID, barberUid);
        startActivity(intent);
    }

    @Override
    public void bookClicked(String barberUid) {

        dialogCaldroidFragment = new CaldroidFragment();

        final String dialogTag = "CALDROID_DIALOG_FRAGMENT";
        if (state != null) {
            dialogCaldroidFragment.restoreDialogStatesFromKey(
                    getFragmentManager(), state,
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

        dialogCaldroidFragment.show(getFragmentManager(),
                dialogTag);

        new SetCalendar(this).Set(barberUid, dialogCaldroidFragment, getActivity());

    }

    @Override
    public void selectedDate(Date date, String barberUid) {
        barberUID = barberUid;
        selectedDate = date;
        DatabaseReference barberJobs = new Nodes().barber(barberUid).child("jobs");
        final Map<String,String> map = new HashMap<>();
        barberJobs.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot children : dataSnapshot.getChildren()) {
                    map.put(children.getKey(), children.getValue(String.class));
                    Log.d("JOB",children.getValue(String.class) );

                }

               selectJob(map);

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void selectJob(Map<String, String> map){
        Collection<String> jobs = map.values();

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("reserve");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        dialogFragment = ReserveDialogFragment.newInstance(jobs, TopRatedFragment.this);
        dialogFragment.show(ft, "reserve");

    }

    @Override
    public void cancelClicked() {
        dialogFragment.dismiss();

    }

    @Override
    public void continueClicked(String jobStr) {
        Log.d("DATOS", String.valueOf(selectedDate));
        Log.d("DATOS", String.valueOf(barberUID));
        Intent intent = new Intent(getActivity(), DayView.class);
        intent.putExtra(BARBER1_UID, barberUID);
        intent.putExtra(SELECTED_DATE, selectedDate.getTime());
        Job job = new Job();
        job.setName(jobStr);
        intent.putExtra(JOB, job);
        dialogFragment.dismiss();
        startActivity(intent);

    }
}
