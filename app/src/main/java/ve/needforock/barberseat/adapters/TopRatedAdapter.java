package ve.needforock.barberseat.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import ve.needforock.barberseat.R;
import ve.needforock.barberseat.data.Nodes;
import ve.needforock.barberseat.models.Barber;
import ve.needforock.barberseat.models.Rating;

/**
 * Created by Soporte on 07-Dec-17.
 */

public class TopRatedAdapter extends FirebaseRecyclerAdapter<Rating, TopRatedAdapter.TopRatedHolder> {
    private TopRatedListener topRatedListener;


    public TopRatedAdapter(Query ref, TopRatedListener topRatedListener) {
        super(Rating.class, R.layout.list_item_top_rated,TopRatedHolder.class, ref);
        this.topRatedListener = topRatedListener;
    }

    @Override
    protected void populateViewHolder(final TopRatedHolder viewHolder, final Rating model, int position) {

        DatabaseReference barber = new Nodes().barber(model.getBarberUid());
        barber.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Barber barber = dataSnapshot.getValue(Barber.class);
                viewHolder.barber.setText(barber.getName());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

        viewHolder.ratingBar.setRating(5-model.getRating());

        viewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                topRatedListener.viewClicked(model.getBarberUid());
            }
        });

        viewHolder.book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                topRatedListener.bookClicked(model.getBarberUid());
            }
        });

    }

    public static class TopRatedHolder extends RecyclerView.ViewHolder {
        private TextView barber, date, job, view, book;
        private CircularImageView circularImageView;
        private RatingBar ratingBar;

        public TopRatedHolder(View itemView) {
            super(itemView);

            barber = itemView.findViewById(R.id.barberNTv);
            ratingBar = itemView.findViewById(R.id.ratingbar);

            circularImageView = itemView.findViewById(R.id.barberPhotoCiv);

            view = itemView.findViewById(R.id.viewTv);
            book = itemView.findViewById(R.id.bookTv);


        }
    }
}
