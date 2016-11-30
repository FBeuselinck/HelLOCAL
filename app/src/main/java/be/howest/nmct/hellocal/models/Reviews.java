package be.howest.nmct.hellocal.models;

import android.widget.RatingBar;

/**
 * Created by Simon on 28/11/2016.
 */

public class Reviews {

    public Float rating;
    public String comment;
    public String userId;


    public Reviews(){

    }

    public Reviews(String comment, Float rating, String userId) {
        this.comment = comment;
        this.rating = rating;
        this.userId = userId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
