package ve.needforock.barberseats.views.top_rated;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.roomorama.caldroid.CaldroidFragment;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

import ve.needforock.barberseats.R;
import ve.needforock.barberseats.adapters.TopRatedAdapter;
import ve.needforock.barberseats.adapters.TopRatedListener;
import ve.needforock.barberseats.data.Nodes;
import ve.needforock.barberseats.models.Job;
import ve.needforock.barberseats.views.barber_detail.BarberDetailActivity;
import ve.needforock.barberseats.views.calendar.SelectedDateCallBack;
import ve.needforock.barberseats.views.calendar.SetCalendar;
import ve.needforock.barberseats.views.day.DayView;
import ve.needforock.barberseats.views.dialog_fragment.ReserveCallBack;
import ve.needforock.barberseats.views.dialog_fragment.ReserveDialogFragment;

import static android.app.Activity.RESULT_OK;
import static ve.needforock.barberseats.views.appointment.AppointmentFragment.BARBER2_UID;
import static ve.needforock.barberseats.views.barber_selection.BarberSelectionActivity.BARBER1_UID;
import static ve.needforock.barberseats.views.barber_selection.BarberSelectionActivity.JOB;
import static ve.needforock.barberseats.views.barber_selection.BarberSelectionActivity.SELECTED_DATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class TopRatedFragment extends Fragment implements TopRatedListener, SelectedDateCallBack, ReserveCallBack, JobsCallBack {


    private RecyclerView recyclerView;
    private TopRatedAdapter topRatedAdapter;
    private CaldroidFragment dialogCaldroidFragment;
    private Bundle state;
    private String barberUID;
    private DialogFragment dialogFragment;
    private Date selectedDate;
    private static final int RC_CODE = 123;

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


        recyclerView = view.findViewById(R.id.topRatedRv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        topRatedAdapter = new TopRatedAdapter( TopRatedFragment.this);
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

        new SetCalendar(this).Set(barberUid, dialogCaldroidFragment);

    }

    @Override
    public void selectedDate(Date date, String barberUid) {
        barberUID = barberUid;
        selectedDate = date;
        DatabaseReference barberJobs = new Nodes().barber(barberUid).child("jobs");
        new JobsPresenter(this).jobs(barberJobs);

    }
    @Override
    public void jobs(Map<String, String> map) {
        selectJob(map);
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
        Intent intent = new Intent(getActivity(), DayView.class);
        intent.putExtra(BARBER1_UID, barberUID);
        intent.putExtra(SELECTED_DATE, selectedDate.getTime());
        Job job = new Job();
        job.setName(jobStr);
        intent.putExtra(JOB, job);
        dialogFragment.dismiss();
        startActivityForResult(intent, RC_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_CODE){
            if (resultCode == RESULT_OK){
                Toast.makeText(getContext(), "Reserva Realizada", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
