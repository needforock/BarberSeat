package ve.needforock.barberseat.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;

import ve.needforock.barberseat.R;
import ve.needforock.barberseat.models.Appointment;

/**
 * Created by Soporte on 13-Nov-17.
 */

public class AppointmentAdapter extends FirebaseRecyclerAdapter<Appointment, AppointmentAdapter.AppointmentHolder>{

    private AppointmentListener appointmentListener;

    public AppointmentAdapter(AppointmentListener appointmentListener, Query ref) {
        super(Appointment.class, R.layout.list_item_appointment, AppointmentHolder.class, ref);
        this.appointmentListener = appointmentListener;

    }

    @Override
    protected void populateViewHolder(AppointmentHolder viewHolder, final Appointment model, int position) {


            viewHolder.barber.setText(model.getBarberName());
            viewHolder.date.setText(String.valueOf(model.getDate()));

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    appointmentListener.appointmentClicked(model);
                }
            });



    }

    public static class AppointmentHolder extends RecyclerView.ViewHolder{
        private TextView barber, date;

        public AppointmentHolder(View itemView) {
            super(itemView);

            barber = itemView.findViewById(R.id.barberTv);
            date = itemView.findViewById(R.id.dateTv);



        }
    }
}
