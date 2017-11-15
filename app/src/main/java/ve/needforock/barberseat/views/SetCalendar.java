package ve.needforock.barberseat.views;

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

import ve.needforock.barberseat.R;
import ve.needforock.barberseat.data.Nodes;
import ve.needforock.barberseat.models.BarberDay;

/**
 * Created by Soporte on 14-Nov-17.
 */

public class SetCalendar {


    Calendar cal = Calendar.getInstance();
    SelectedDateCallBack selectedDateCallBack;

    public SetCalendar(SelectedDateCallBack selectedDateCallBack) {
        this.selectedDateCallBack = selectedDateCallBack;
    }

    public void Set(final String barberUid, final CaldroidFragment dialogCaldroidFragment, final Context context) {

        refreshData(cal, barberUid, context, dialogCaldroidFragment);

        final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");

        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {

                selectedDateCallBack.selectedDate(date);
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


    public void refreshData(Calendar cal, String barberUid, final Context context, final CaldroidFragment dialogCaldroidFragment) {

        DatabaseReference barberAppMonth = new Nodes().appointmentDay(barberUid)
                .child(String.valueOf(cal.get(cal.YEAR)))
                .child(String.valueOf(cal.get(cal.MONTH)));


        barberAppMonth.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    String day;
                    for (int i = 0; i < 32; i++) {
                        day = String.valueOf(i);
                        BarberDay barberDay = dataSnapshot.child(day).getValue(BarberDay.class);
                        if (barberDay != null) {
                            if (barberDay != null) {
                                boolean fullDay = checkDay(barberDay);
                                if (fullDay) {
                                    setFullDayColor(dialogCaldroidFragment, barberDay.getDate());
                                } else {
                                    setGreenDayColor(dialogCaldroidFragment, barberDay.getDate());
                                }
                            } else {
                                Toast.makeText(context, "Dia Nulo", Toast.LENGTH_SHORT).show();
                            }
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
            ColorDrawable red = new ColorDrawable(Color.RED);
            dialogCaldroidFragment.setBackgroundDrawableForDate(red, redDate);
            dialogCaldroidFragment.setTextColorForDate(R.color.white, redDate);
        }
        dialogCaldroidFragment.refreshView();

    }

    public void setGreenDayColor(CaldroidFragment dialogCaldroidFragment, Date date) {

        Date greenDate = date;
        if (dialogCaldroidFragment != null) {
            ColorDrawable green = new ColorDrawable(Color.GREEN);
            dialogCaldroidFragment.setBackgroundDrawableForDate(green, greenDate);
            dialogCaldroidFragment.setTextColorForDate(R.color.white, greenDate);
        }
        dialogCaldroidFragment.refreshView();

    }

    public boolean checkDay(BarberDay barberDay) {
        if (barberDay.isNine() && barberDay.isTen() && barberDay.isEleven() && barberDay.isTwelve()
                && barberDay.isThirteen() && barberDay.isFourteen() && barberDay.isFifteen() &&
                barberDay.isSixteen() && barberDay.isSeventeen() && barberDay.isEightteen() &&
                barberDay.isNinteen() && barberDay.isTwenty()) {
            return true;
        } else {
            return false;
        }

    }


}

