package ve.needforock.barberseat.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import ve.needforock.barberseat.R;
import ve.needforock.barberseat.models.Job;

/**
 * Created by Soporte on 13-Nov-17.
 */

public class JobAdapter extends FirebaseRecyclerAdapter<Job, JobAdapter.JobHolder> {

    private JobListener jobListener;
    private Context context;


    public JobAdapter(JobListener jobListener, Query ref, Context context) {
        super(Job.class, R.layout.list_item_job, JobHolder.class, ref);
        this.jobListener = jobListener;
        this.context = context;
    }

    @Override
    protected void populateViewHolder(final JobHolder viewHolder, Job model, int position) {
        viewHolder.job.setText(model.getName());

        if(model.getName().equals("Corte")){
            Picasso.with(context).load(R.mipmap.cut).centerCrop().fit().into(viewHolder.jobPic);
        }else if(model.getName().equals("Rasurado")){
            Picasso.with(context).load(R.mipmap.shave).centerCrop().fit().into(viewHolder.jobPic);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Job auxJob = getItem(viewHolder.getAdapterPosition());
                jobListener.clicked(auxJob);
            }
        });
    }

    public static class JobHolder extends RecyclerView.ViewHolder{
        private TextView job;
        private ImageView jobPic;

        public JobHolder(View itemView) {
            super(itemView);

            job = itemView.findViewById(R.id.jobTv);
            jobPic = itemView.findViewById(R.id.jobPicIv);




        }
    }
}
