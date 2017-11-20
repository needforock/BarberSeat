package ve.needforock.barberseat.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ve.needforock.barberseat.R;

/**
 * Created by Soporte on 20-Nov-17.
 */

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.DayHolder>{

    List<Boolean> hours;

    public DayAdapter(List<Boolean> hours) {
        this.hours = hours;
    }

    @Override
    public DayHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hour, parent, false);
        return new DayHolder(view);
    }

    @Override
    public void onBindViewHolder(DayHolder holder, int position) {
        boolean appointment = hours.get(position);
        Log.d("HORA", String.valueOf(hours.get(0)));
        String hour = String.valueOf(position+9) +":00";
        holder.hour.setText(hour);
        if(appointment){
            holder.occupied.setText("Ocupado");
        }else{
            holder.occupied.setText("Disponible");
        }

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
