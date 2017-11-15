package ve.needforock.barberseat.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ve.needforock.barberseat.R;
import ve.needforock.barberseat.models.Barber;

/**
 * Created by Soporte on 14-Nov-17.
 */

public class BarberAdapter extends RecyclerView.Adapter<BarberAdapter.BarberHolder> {

    private List<Barber> barberList;

    private BarberListener barberListener;

    public BarberAdapter(List<Barber> barberList, BarberListener barberListener) {
        this.barberList = barberList;
        this.barberListener =barberListener;
    }

    @Override
    public BarberHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_barber, parent, false);
        return new BarberHolder(view);
    }

    @Override
    public void onBindViewHolder(final BarberHolder holder, final int position) {

        Barber barber = barberList.get(position);
        holder.barber.setText(barber.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Barber auxBarber = barberList.get(position);
                barberListener.barberClicked(auxBarber.getUid());
            }
        });

    }

    @Override
    public int getItemCount() {
        return barberList.size();
    }

    public static class BarberHolder extends RecyclerView.ViewHolder{
        private TextView barber;

        public BarberHolder(View itemView) {
            super(itemView);

            barber = itemView.findViewById(R.id.barberTv);




        }
    }
}
