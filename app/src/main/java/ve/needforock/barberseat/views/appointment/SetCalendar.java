package ve.needforock.barberseat.views.appointment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ve.needforock.barberseat.R;
import ve.needforock.barberseat.data.Nodes;

/**
 * Created by Soporte on 14-Nov-17.
 */

public class SetCalendar {


    Calendar cal = Calendar.getInstance();
    SelectedDateCallBack selectedDateCallBack;
    Context context;

    public SetCalendar(SelectedDateCallBack selectedDateCallBack) {
        this.selectedDateCallBack = selectedDateCallBack;
    }

    public void Set(final String barberUid, final CaldroidFragment dialogCaldroidFragment, final Context context) {

        refreshData(cal, barberUid, context, dialogCaldroidFragment);

        final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");

        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {

                selectedDateCallBack.selectedDate(date, barberUid);
                dialogCaldroidFragment.dismiss();

            }

            @Override
            public void onChangeMonth(int month, int year) {
                year = year - 1900;
                cal.set(year + 1900, month, 15);
                dialogCaldroidFragment.refreshView();
                refreshData(cal, barberUid, context, dialogCaldroidFragment);
            }
        };

        dialogCaldroidFragment.setCaldroidListener(listener);

    }


    public void refreshData(final Calendar cal, String barberUid, final Context context, final CaldroidFragment dialogCaldroidFragment) {

        final int year = cal.get(cal.YEAR);
        final int month = cal.get(cal.MONTH);

        DatabaseReference barberAppMonth = new Nodes().appointmentDay(barberUid)
                .child(String.valueOf(year))
                .child(String.valueOf(month));




        barberAppMonth.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    String day;
                    for (int i = 1; i < 31; i++) {
                        day = String.valueOf(i);
                        Map<String, Boolean> map = new HashMap<>();
                        for (DataSnapshot children : dataSnapshot.child(day).getChildren()) {
                            map.put(children.getKey(), children.getValue(Boolean.class));

                        }
                        cal.set(year, month, i);
                        int hoursCount = checkDay( map);
                        if (hoursCount == 12) {
                            setFullDayColor(dialogCaldroidFragment, cal.getTime());
                        } else if (hoursCount >0 && hoursCount<12){
                            setGreenDayColor(dialogCaldroidFragment, cal.getTime(), map);
                        }
                    }
                } else {
                    Toast.makeText(context, "snapshot Nulo", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void setFullDayColor(CaldroidFragment dialogCaldroidFragment, Date date) {

        Date redDate = date;

        if (dialogCaldroidFragment != null) {
            ColorDrawable red = new ColorDrawable(Color.parseColor("#b7ff0000"));
            dialogCaldroidFragment.setBackgroundDrawableForDate(red, redDate);
            dialogCaldroidFragment.setTextColorForDate(R.color.white, redDate);
        }
        dialogCaldroidFragment.refreshView();

    }

    public void setGreenDayColor(CaldroidFragment dialogCaldroidFragment, Date date, Map<String, Boolean> map ) {

        Date greenDate = date;


        if (dialogCaldroidFragment != null) {


                ColorDrawable green = new ColorDrawable(Color.parseColor("#a4ff96"));
                dialogCaldroidFragment.setBackgroundDrawableForDate(green, greenDate);
                dialogCaldroidFragment.setTextColorForDate(R.color.white, greenDate);


        }
        dialogCaldroidFragment.refreshView();

    }

    public int checkDay( Map<String, Boolean> map) {

        int i = 0;
        for (String key : map.keySet()) {
            if (map.get(key)) {
                i++;
            }
        }

        return i;
    }


}

