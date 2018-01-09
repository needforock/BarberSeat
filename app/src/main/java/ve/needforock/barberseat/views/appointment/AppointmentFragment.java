package ve.needforock.barberseat.views.appointment;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import ve.needforock.barberseat.R;
import ve.needforock.barberseat.adapters.AppointmentAdapter;
import ve.needforock.barberseat.adapters.AppointmentListener;
import ve.needforock.barberseat.data.CurrentUser;
import ve.needforock.barberseat.data.DeleteAppointment;
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
    private AddAppointmentRequestListener listener;


    public AppointmentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (AddAppointmentRequestListener) context;
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
        recyclerView = view.findViewById(R.id.appointmentRv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        appointmentAdapter = new AppointmentAdapter(AppointmentFragment.this, customerUid);
        recyclerView.setAdapter(appointmentAdapter);

        FloatingActionButton addFab = view.findViewById(R.id.addFab);

        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.addRequested();

            }
        });
    }



    @Override
    public void deleteClicked(final Appointment appointment) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("¿Desea Eliminar la reserva?");
        builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                new DeleteAppointment().delete(appointment);
                Toast.makeText(getActivity(), "Reserva Eliminada", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
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
