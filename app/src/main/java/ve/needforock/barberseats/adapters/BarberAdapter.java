package ve.needforock.barberseats.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import ve.needforock.barberseats.R;
import ve.needforock.barberseats.models.Barber;

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

        holder.see.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Barber auxBarber = barberList.get(position);
                barberListener.seeClicked(auxBarber.getUid());
            }
        });

        holder.reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Barber auxBarber = barberList.get(position);
                barberListener.reserveClicked(auxBarber.getUid());
            }
        });


        if(barber.getPhoto().trim().length()>0 && barber.getPhoto()!=null) {
            Picasso.with(holder.barberPhoto.getContext()).load(barber.getPhoto()).into(holder.barberPhoto);

        }
    }

    @Override
    public int getItemCount() {
        return barberList.size();
    }

    public static class BarberHolder extends RecyclerView.ViewHolder{
        private TextView barber, see, reserve;
        private CircularImageView barberPhoto;


        public BarberHolder(View itemView) {
            super(itemView);

            barber = itemView.findViewById(R.id.barberTv);
            barberPhoto = itemView.findViewById(R.id.barberPhotoCiv);
            see = itemView.findViewById(R.id.seeTv);
            reserve = itemView.findViewById(R.id.reserveTv);




        }
    }
}
