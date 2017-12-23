package ve.needforock.barberseat.views.day;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import ve.needforock.barberseat.data.Nodes;
import ve.needforock.barberseat.models.Barber;

/**
 * Created by Soporte on 23-Dec-17.
 */

public class BarberPresenter {

    private BarberCallBack barberCallBack;

    public BarberPresenter(BarberCallBack barberCallBack) {
        this.barberCallBack = barberCallBack;
    }

    public void checkDay(DatabaseReference barberAppDay){

        barberAppDay.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Boolean> map = new HashMap<>();
                if (dataSnapshot != null) {

                    for (DataSnapshot children : dataSnapshot.getChildren()) {
                        map.put(children.getKey(), children.getValue(Boolean.class));
                    }
                }
                barberCallBack.dayChecked(map);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void checkBarber(DatabaseReference barber){

        barber.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Barber auxBarber = dataSnapshot.getValue(Barber.class);
                barberCallBack.barberChecked(auxBarber);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    public void checkJobs(String barberUid){
        DatabaseReference cut = new Nodes().barber(barberUid);
        final String[] specialties = {""};
        cut.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Barber auxBarber = dataSnapshot.getValue(Barber.class);
                Map<String, String> map = auxBarber.getJobs();

                for (Map.Entry<String, String> entry: map.entrySet()){
                    if(specialties[0].trim().length()==0){
                        specialties[0] =entry.getValue();
                    }else{
                        specialties[0] = specialties[0] + ", " + entry.getValue();
                    }
                }
                barberCallBack.jobsChecked(specialties[0]);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
