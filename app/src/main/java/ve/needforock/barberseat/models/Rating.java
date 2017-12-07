package ve.needforock.barberseat.models;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Soporte on 05-Dec-17.
 */

public class Rating {
    private int ratingTimes;
    private float rating;
    private Map<String, Boolean> stars = new HashMap<>();

    public Rating() {
    }

    public int getRatingTimes() {
        return ratingTimes;
    }

    public void setRatingTimes(int ratingTimes) {
        this.ratingTimes = ratingTimes;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public Map<String, Boolean> getStars() {
        return stars;
    }

    public void setStars(Map<String, Boolean> stars) {
        this.stars = stars;
    }
}
