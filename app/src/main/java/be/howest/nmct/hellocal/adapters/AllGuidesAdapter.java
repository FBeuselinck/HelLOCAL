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

import java.util.ArrayList;
import java.util.List;

import be.howest.nmct.hellocal.R;
import be.howest.nmct.hellocal.models.AvaiableGuides;
import be.howest.nmct.hellocal.models.Reviews;


/**
 * Created by Simon on 27/11/2016.
 */

public class AllGuidesAdapter  extends RecyclerView.Adapter<AllGuidesAdapter.MyViewHolder> {

    private List<AvaiableGuides> guidesList;
    private List<Reviews> reviewsList;
    private List<String> amountsList;

    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, city, country, price, id;
        public ImageView photo;
        public RatingBar score;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.textViewNaam);
            city = (TextView) view.findViewById(R.id.textViewCity);
            country = (TextView) view.findViewById(R.id.textViewCountry);
            price = (TextView) view.findViewById(R.id.textViewPrice);
            photo = (ImageView) view.findViewById(R.id.imagePhoto);
            score = (RatingBar) view.findViewById(R.id.ratingBar);
            id = (TextView) view.findViewById(R.id.textViewAvailibleGuideId);

        }
    }


    public AllGuidesAdapter(List<AvaiableGuides> guidesList, List<Reviews> reviewsList, List<String> amountsList, Context context) {

        List<AvaiableGuides> listje = new ArrayList<>();
        for(int i = 0; i< guidesList.size(); i++){
            if(guidesList.get(i).getCanBeBooked()){
                listje.add(guidesList.get(i));
            }
        }
        this.guidesList = listje;
        this.reviewsList = reviewsList;
        this.context = context;
        this.amountsList = amountsList;
    }

    @Override
    public AllGuidesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AllGuidesAdapter.MyViewHolder holder, int position) {
        AvaiableGuides guide = guidesList.get(position);
        Reviews review = reviewsList.get(position);
        String amount = amountsList.get(position);

        float score = review.getRating();

        holder.name.setText(guide.getName());
        holder.city.setText(guide.getLocation());
        holder.country.setText(guide.getCountry());
        holder.price.setText("€ "+guide.getPrice()+"/h");
        holder.score.setRating(score);
        holder.id.setText(guide.getId());


        holder.name.setTag(R.id.guideId,guide.getUserId());
        holder.name.setTag(R.id.transport, guide.getTransport());
        holder.name.setTag(R.id.photoUri, guide.getPhotoUri());
        holder.name.setTag(R.id.rating, review.getRating());
        holder.name.setTag(R.id.amount, amount);

        Picasso.with(this.context).load(guide.getPhotoUri()).into(holder.photo);

        String blActive = "false";
        String blCity = "false";
        String blCulture = "false";
        String blElse = "false";

        ArrayList list = guide.getType();
        for(int i = 0; i< list.size(); i++){
            if(list.get(i).toString().equals("Active")){

                blActive = "True";
            }
            else if(list.get(i).toString().equals("City")){

                blCity= "True";
            }
            else if(list.get(i).toString().equals("Culture")){

                blCulture = "True";
            }
            else if(list.get(i).toString().equals("Else")){

                blElse = "True";
            }
        }

        holder.name.setTag(R.id.active, blActive);
        holder.name.setTag(R.id.city, blCity);
        holder.name.setTag(R.id.culture, blCulture);
        holder.name.setTag(R.id.smthElse, blElse);


    }

    @Override
    public int getItemCount() {
        return guidesList.size();
    }

}
