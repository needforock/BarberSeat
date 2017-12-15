package ve.needforock.barberseat.views.appointment;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.Query;

import ve.needforock.barberseat.R;
import ve.needforock.barberseat.adapters.AppointmentAdapter;
import ve.needforock.barberseat.adapters.AppointmentListener;
import ve.needforock.barberseat.data.CurrentUser;
import ve.needforock.barberseat.data.DeleteAppointment;
import ve.needforock.barberseat.data.Queries;
import ve.needforock.barberseat.models.Appointment;
import ve.needforock.barberseat.views.appointment_detail.AppointmentDetailActivity;
import ve.needforock.barberseat.views.barber_detail.BarberDetailActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class AppointmentFragment extends Fragment implements AppointmentListener {

    public static final String BARBER2_UID ="ve.needforock.barberseat.views.appointment.KEY.BARBER2_UID" ;
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

        Query query = new Queries().CustomerAppointments(customerUid).orderByChild("key");


        recyclerView = view.findViewById(R.id.appointmentRv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        appointmentAdapter = new AppointmentAdapter(AppointmentFragment.this, query, getContext());
        recyclerView.setAdapter(appointmentAdapter);
    }



    @Override
    public void deleteClicked(final Appointment appointment) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage("¿Desea Eliminar la reserva?");


        // Add the buttons
        builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                new DeleteAppointment().delete(appointment);
                Toast.makeText(getActivity(), "Reserva Eliminada", Toast.LENGTH_SHORT).show();


            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        // 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    @Override
    public void viewClicked(Appointment appointment) {
        Intent intent = new Intent(getActivity(), AppointmentDetailActivity.class);
        intent.putExtra(APPOINTMENT, appointment);
        startActivity(intent);

    }

    @Override
    public void barberClicked(String barberUid) {
        Intent intent = new Intent(getActivity(), BarberDetailActivity.class);
        intent.putExtra(BARBER2_UID, barberUid);
        startActivity(intent);

    }
}
