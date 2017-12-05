package ve.needforock.barberseat.models;

/**
 * Created by Soporte on 05-Dec-17.
 */

public class Rating {
    private int ratingTimes;
    private long rating;

    public Rating() {
    }

    public int getRatingTimes() {
        return ratingTimes;
    }

    public void setRatingTimes(int ratingTimes) {
        this.ratingTimes = ratingTimes;
    }

    public long getRating() {
        return rating;
    }

    public void setRating(long rating) {
        this.rating = rating;
    }
}
