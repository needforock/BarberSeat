package ve.needforock.barberseat.views.barber_detail;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import ve.needforock.barberseat.data.CurrentUser;
import ve.needforock.barberseat.data.Nodes;
import ve.needforock.barberseat.models.Appointment;
import ve.needforock.barberseat.models.Rating;

/**
 * Created by Soporte on 05-Dec-17.
 */

public class RatingPresenter {

    private RatingCallBack ratingCallBack;
    private Context context;


    public RatingPresenter(RatingCallBack ratingCallBack) {
        this.ratingCallBack = ratingCallBack;
        this.context = context;
    }

    public void rateBarber(String barberUid, final float ratingValue){
        final String userUid = new CurrentUser().getUid();


         final DatabaseReference barberRatingRef = new Nodes().barberRating(barberUid);

        barberRatingRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {

                Rating rating = mutableData.getValue(Rating.class);
                if (rating == null){

                    return Transaction.success(mutableData);
                }

               /* if (rating.getStars().containsKey(userUid)){

                    return Transaction.abort();

                } else{*/


                    Map<String, Boolean> map = rating.getStars();
                    map.put(userUid, true);
                    rating.setStars(map);


                    float auxRating = rating.getRating();
                    int ratingTimes = rating.getRatingTimes();
                    if(auxRating==0 && ratingTimes==0){
                        rating.setRatingTimes(1);
                        rating.setRating(5-ratingValue);

                    }else{
                        float newRatingValue = (auxRating*ratingTimes+ratingValue)/(ratingTimes+1);
                        rating.setRatingTimes(rating.getRatingTimes()+1);
                        rating.setRating(5-newRatingValue);

                    }


                //}

                mutableData.setValue(rating);
                return Transaction.success(mutableData);


            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                if (b){
                    ratingCallBack.ratingSuccess();
                }else{
                    ratingCallBack.ratingNoSuccess();
                }

            }
        });





    }


    public void checkRateNoNull(final String barberUid){

        final DatabaseReference ref = new Nodes().barberRating(barberUid);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue(Rating.class)==null){
                    Log.d("REF", "mutable null");
                    Rating rating = new Rating();
                    rating.setRating(0);
                    rating.setRatingTimes(0);
                    rating.setBarberUid(barberUid);
                    Map<String,Boolean> stars = new HashMap<>();
                    stars.put("x", false);
                    rating.setStars(stars);
                    ref.setValue(rating);
                    ratingCallBack.rateChecked(5-rating.getRating());
                }else{
                    Rating rating = dataSnapshot.getValue(Rating.class);
                    ratingCallBack.rateChecked(5-rating.getRating());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void checkAppointmentIsRated(Appointment appointment){
        final String userUid = new CurrentUser().getUid();

        String appointmentKey = appointment.getKey();

        final DatabaseReference appointmentRef = new Nodes().userAppointment(userUid).child(appointmentKey);

        appointmentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Appointment auxApp = dataSnapshot.getValue(Appointment.class);
                if (auxApp.isRated()){
                    ratingCallBack.ratingNoSuccess();
                }else{
                    auxApp.setRated(true);
                    appointmentRef.setValue(auxApp);
                    ratingCallBack.doRating();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
