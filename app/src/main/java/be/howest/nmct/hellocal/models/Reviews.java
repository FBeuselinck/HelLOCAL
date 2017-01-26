package be.howest.nmct.hellocal.models;

import android.widget.RatingBar;

/**
 * Created by Simon on 28/11/2016.
 */

public class Reviews {

    public Float rating;
    public String comment;
    public String userId;
    public String guideId;


    public Reviews(){

    }

    public Reviews(String comment, Float rating, String userId, String guideId) {
        this.comment = comment;
        this.rating = rating;
        this.userId = userId;
        this.guideId = guideId;
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


    public String getGuideId() {
        return guideId;
    }

    public void setGuideId(String guideId) {
        this.guideId = guideId;
    }
}
