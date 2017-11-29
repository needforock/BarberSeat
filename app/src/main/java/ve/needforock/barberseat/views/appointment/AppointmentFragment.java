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

import ve.needforock.barberseat.R;
import ve.needforock.barberseat.adapters.AppointmentAdapter;
import ve.needforock.barberseat.adapters.AppointmentListener;
import ve.needforock.barberseat.data.CurrentUser;
import ve.needforock.barberseat.data.Queries;
import ve.needforock.barberseat.models.Appointment;
import ve.needforock.barberseat.views.appointment_detail.AppointmentDetailActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class AppointmentFragment extends Fragment implements AppointmentListener {

    private RecyclerView recyclerView;
    private AppointmentAdapter appointmentAdapter;
    public static final String APPOINTMENT = "ve.needforock.barberseat.views.appointment.KEY.APPOINTMENT";


    public AppointmentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_appointment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        final String customerUid = new CurrentUser().getUid();

        Query query = new Queries().CustomerAppointments(customerUid);


        recyclerView = view.findViewById(R.id.appointmentRv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        appointmentAdapter = new AppointmentAdapter(AppointmentFragment.this, query);
        recyclerView.setAdapter(appointmentAdapter);
    }

    @Override
    public void appointmentClicked(Appointment appointment) {
        Intent intent = new Intent(getActivity(), AppointmentDetailActivity.class);
        intent.putExtra(APPOINTMENT, appointment);
        startActivity(intent);
    }
}
