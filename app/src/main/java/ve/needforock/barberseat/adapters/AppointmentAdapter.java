package ve.needforock.barberseat.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;

import ve.needforock.barberseat.R;
import ve.needforock.barberseat.data.Nodes;
import ve.needforock.barberseat.data.Queries;
import ve.needforock.barberseat.models.Appointment;
import ve.needforock.barberseat.models.Barber;

/**
 * Created by Soporte on 13-Nov-17.
 */

public class AppointmentAdapter extends FirebaseRecyclerAdapter<Appointment, AppointmentAdapter.AppointmentHolder> {

    private AppointmentListener appointmentListener;


    public AppointmentAdapter(AppointmentListener appointmentListener, String customerUid) {
        super(Appointment.class, R.layout.list_item_appointment, AppointmentHolder.class, new Queries().CustomerAppointments(customerUid).orderByChild("key"));
        this.appointmentListener = appointmentListener;

    }

    @Override
    protected void populateViewHolder(final AppointmentHolder viewHolder, final Appointment model, int position) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(model.getDate());
        Date currentTime = Calendar.getInstance().getTime();
        int day = calendar.get(calendar.DAY_OF_MONTH);
        String dayString = String.valueOf(day);
        if (dayString.trim().length() == 1){
            dayString = "0" + dayString;
        }
        String month = String.valueOf(calendar.get(calendar.MONTH) + 1);
        String year = String.valueOf(calendar.get(calendar.YEAR));
        String hour = String.valueOf(calendar.get(calendar.HOUR_OF_DAY)) + ":00";
        String date = dayString + "-" + month + "-" + year + " / " + hour;

        if(model.getDate().getTime()<currentTime.getTime()) {
            viewHolder.date.setText(date   + " (Vencido)");
        }else{
            viewHolder.date.setText(date);
        }
        viewHolder.job.setText(model.getJob());

        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appointmentListener.deleteClicked(model);
            }
        });



        viewHolder.circularImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                appointmentListener.barberClicked(model.getBarberUid());
            }
        });

        viewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appointmentListener.viewClicked(model);
            }
        });

        DatabaseReference barber = new Nodes().barber(model.getBarberUid());
        barber.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Barber auxBarber = dataSnapshot.getValue(Barber.class);
                    viewHolder.barber.setText(auxBarber.getName());
                if(auxBarber.getPhoto().trim().length()>0) {
                    Picasso.with(viewHolder.circularImageView.getContext()).load(auxBarber.getPhoto()).into(viewHolder.circularImageView);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public static class AppointmentHolder extends RecyclerView.ViewHolder {
        private TextView barber, date, job, view, delete;
        private CircularImageView circularImageView;
        public AppointmentHolder(View itemView) {
            super(itemView);
            barber = itemView.findViewById(R.id.barberNTv);
            date = itemView.findViewById(R.id.dateTv);
            circularImageView = itemView.findViewById(R.id.barberPhotoCiv);
            job = itemView.findViewById(R.id.jobTv);
            view = itemView.findViewById(R.id.viewTv);
            delete = itemView.findViewById(R.id.bookTv);
        }
    }
}
