package ve.needforock.barberseats.views.calendar;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import ve.needforock.barberseats.R;
import ve.needforock.barberseats.data.Nodes;

/**
 * Created by Soporte on 14-Nov-17.
 */

public class SetCalendar {


    private Calendar cal = Calendar.getInstance();
    private SelectedDateCallBack selectedDateCallBack;


    public SetCalendar(SelectedDateCallBack selectedDateCallBack) {
        this.selectedDateCallBack = selectedDateCallBack;
    }

    public void Set(final String barberUid, final CaldroidFragment dialogCaldroidFragment) {
        refreshData(cal, barberUid, dialogCaldroidFragment);
        ArrayList<Date> dates = new ArrayList<>();
        Date today = Calendar.getInstance().getTime();
        Calendar calendar = Calendar.getInstance();
        calendar.set(1970, 1, 1);
        Date startDay = calendar.getTime();
        dates = getDaysBetweenDates(startDay, today);
        dialogCaldroidFragment.setDisableDates(dates);

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
                refreshData(cal, barberUid, dialogCaldroidFragment);
            }
        };

        dialogCaldroidFragment.setCaldroidListener(listener);

    }


    private void refreshData(final Calendar cal, String barberUid, final CaldroidFragment dialogCaldroidFragment) {

        final int year = cal.get(Calendar.YEAR);
        final int month = cal.get(Calendar.MONTH);

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
                            setGreenDayColor(dialogCaldroidFragment, cal.getTime());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setFullDayColor(CaldroidFragment dialogCaldroidFragment, Date redDate) {


        if (dialogCaldroidFragment != null) {
            ColorDrawable red = new ColorDrawable(Color.parseColor("#b7ff0000"));
            dialogCaldroidFragment.setBackgroundDrawableForDate(red, redDate);
            dialogCaldroidFragment.setTextColorForDate(R.color.white, redDate);
        }
        dialogCaldroidFragment.refreshView();

    }

    private void setGreenDayColor(CaldroidFragment dialogCaldroidFragment, Date greenDate) {


        if (dialogCaldroidFragment != null) {
                ColorDrawable green = new ColorDrawable(Color.parseColor("#a4ff96"));
                dialogCaldroidFragment.setBackgroundDrawableForDate(green, greenDate);
                dialogCaldroidFragment.setTextColorForDate(R.color.white, greenDate);
        }
        dialogCaldroidFragment.refreshView();

    }

    private int checkDay( Map<String, Boolean> map) {
        int i = 0;
        for (String key : map.keySet()) {
            if (map.get(key)) {
                i++;
            }
        }
        return i;
    }

    private static ArrayList<Date> getDaysBetweenDates(Date startDate, Date endDate)
    {
        ArrayList<Date> dates = new ArrayList<Date>();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(startDate);
        while (calendar.getTime().before(endDate))
        {
            Date result = calendar.getTime();
            dates.add(result);
            calendar.add(Calendar.DATE, 1);
        }
        return dates;
    }
}

