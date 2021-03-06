package ve.needforock.barberseat.adapters;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;
import java.util.Map;

import ve.needforock.barberseat.R;

/**
 * Created by Soporte on 20-Nov-17.
 */

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.DayHolder>{

    private Map<String, Boolean> hours;
    private DayListener dayListener;
    private Date date;

    //TODO remove the date and make the activity handle it
    public DayAdapter(Date date, Map<String, Boolean> hours, DayListener dayListener) {
        this.date = date;
        this.hours = hours;
        this.dayListener = dayListener;
    }

    @Override
    public DayHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hour, parent, false);
        return new DayHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final DayHolder holder, int position) {


        //String appointment = hours.get(position);
        Log.d("HORA", String.valueOf(hours.get(0)));
        String hour = String.valueOf(position+9) +":00";
        holder.hour.setText(hour);
        if (hours.containsKey(hour) && hours.get(hour)) {
            holder.occupied.setText("Ocupado");
            holder.itemView.setBackgroundColor(Color.parseColor("#94FF973C"));

        } else {
            holder.occupied.setText("Disponible");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String auxHour = String.valueOf(holder.getAdapterPosition()+9) +":00";
                dayListener.clickedHour(auxHour, date);
            }
        });

    }

    @Override
    public int getItemCount() {
        return 12;
    }

    public static class DayHolder extends RecyclerView.ViewHolder{
        private TextView hour, occupied;

        public DayHolder(View itemView) {
            super(itemView);

            hour = itemView.findViewById(R.id.hourTv);
            occupied = itemView.findViewById(R.id.eventTv);


        }
    }
}
