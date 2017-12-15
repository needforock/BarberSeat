package ve.needforock.barberseat.views.barber_detail;

/**
 * Created by Soporte on 05-Dec-17.
 */

public interface RatingCallBack {

    void ratingSuccess();
    void rateChecked(float rating);
    void ratingNoSuccess();
    void doRating();
}
