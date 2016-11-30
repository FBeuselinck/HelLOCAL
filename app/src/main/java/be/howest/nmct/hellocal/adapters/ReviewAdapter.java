package be.howest.nmct.hellocal.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

import be.howest.nmct.hellocal.R;
import be.howest.nmct.hellocal.models.Reviews;

/**
 * Created by Simon on 28/11/2016.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyViewHolder> {

    private List<Reviews> reviewsList;

    String Name, PhotoUri;

    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView comment, userId, name;
        public RatingBar rating;
        public ImageView photo;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.textViewNaam);
            rating = (RatingBar) view.findViewById(R.id.ratingBar);
            comment = (TextView) view.findViewById(R.id.textViewReviews);
            photo = (ImageView) view.findViewById(R.id.imagePhoto);
        }
    }


    public ReviewAdapter(List<Reviews> reviewsList, String Name, String PhotoUri, Context context) {
        this.reviewsList = reviewsList;
        this.Name = Name;
        this.PhotoUri = PhotoUri;
        this.context = context;
    }

    @Override
    public ReviewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_reviews, parent, false);

        return new ReviewAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(ReviewAdapter.MyViewHolder holder, int position) {
        Reviews review = reviewsList.get(position);
        holder.rating.setRating(review.getRating());
        holder.comment.setText(review.getComment());
        holder.name.setText(Name);

        Picasso.with(this.context).load(PhotoUri).into(holder.photo);

    }

    @Override
    public int getItemCount() {
        return reviewsList.size();
    }
}
