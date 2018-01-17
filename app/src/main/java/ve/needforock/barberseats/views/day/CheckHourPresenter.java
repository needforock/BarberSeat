package ve.needforock.barberseats.views.day;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ve.needforock.barberseats.data.Nodes;

/**
 * Created by Soporte on 27-Nov-17.
 */

public class CheckHourPresenter {

    private CheckHourCallBack checkHourCallBack;


    public CheckHourPresenter(CheckHourCallBack checkHourCallBack) {
        this.checkHourCallBack = checkHourCallBack;

    }

    public void checkHour(final String hour, final Date date, String barberUid) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String year = String.valueOf(cal.get(Calendar.YEAR));
        String month = String.valueOf(cal.get(Calendar.MONTH));
        String day = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));

        DatabaseReference barberAppDay = new Nodes().day(barberUid, year, month, day);

        barberAppDay.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Map<String, Boolean> map = new HashMap<>();
                if (dataSnapshot != null) {
                    for (DataSnapshot children : dataSnapshot.getChildren()) {
                        map.put(children.getKey(), children.getValue(Boolean.class));
                    }

                    if (map.get(hour) != null && map.get(hour)) {

                        checkHourCallBack.noAvailable();
                    } else {

                        checkHourCallBack.available(hour, date);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}