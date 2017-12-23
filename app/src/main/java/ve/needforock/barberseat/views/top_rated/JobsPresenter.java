package ve.needforock.barberseat.views.top_rated;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Soporte on 23-Dec-17.
 */

public class JobsPresenter {

    private JobsCallBack jobsCallBack;

    public JobsPresenter(JobsCallBack jobsCallBack) {
        this.jobsCallBack = jobsCallBack;
    }

    public void jobs(DatabaseReference barberJobs){

        final Map<String,String> map = new HashMap<>();
        barberJobs.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot children : dataSnapshot.getChildren()) {
                    map.put(children.getKey(), children.getValue(String.class));

                }
                jobsCallBack.jobs(map);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
