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

import com.google.android.gms.ads.NativeExpressAdView;


/**
 * Created by Simon on 27/11/2016.
 */

public class AllGuidesAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<AvaiableGuides> guidesList;
    private List<Reviews> reviewsList;
    private List<String> amountsList;
    private List<Object> mRecyclerViewItems;

    private static final int ITEM_VIEW_TYPE = 0;
    private static final int AD_VIEW_TYPE = 1;

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

    public class NativeExpressAdViewHolder extends RecyclerView.ViewHolder{
        NativeExpressAdViewHolder(View view){
            super(view);
        }
    }


    public AllGuidesAdapter(List<AvaiableGuides> guidesList, List<Reviews> reviewsList, List<String> amountsList, List<Object> items, Context context) {


        this.guidesList = guidesList;
        this.reviewsList = reviewsList;
        this.context = context;
        this.amountsList = amountsList;
        this.mRecyclerViewItems = items;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case ITEM_VIEW_TYPE:
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_list, parent, false);

                return new MyViewHolder(itemView);
            case AD_VIEW_TYPE:
                // fall through
            default:
                View nativeExpressLayoutView = LayoutInflater.from(
                        parent.getContext()).inflate(R.layout.native_express_ad_container,
                        parent, false);
                return new NativeExpressAdViewHolder(nativeExpressLayoutView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        int viewType = getItemViewType(position);
        switch (viewType) {
            case ITEM_VIEW_TYPE:

                MyViewHolder myViewHolder = (MyViewHolder) holder;


                AvaiableGuides guide = (AvaiableGuides) guidesList.get(position);
                Reviews review = reviewsList.get(position);
                String amount = amountsList.get(position);

                float score = review.getRating();

                myViewHolder.name.setText(guide.getName());
                myViewHolder.city.setText(guide.getLocation());
                myViewHolder.country.setText(guide.getCountry());
                myViewHolder.price.setText("€ " + guide.getPrice() + "/h");
                myViewHolder.score.setRating(score);
                myViewHolder.id.setText(guide.getId());


                myViewHolder.name.setTag(R.id.guideId, guide.getUserId());
                myViewHolder.name.setTag(R.id.transport, guide.getTransport());
                myViewHolder.name.setTag(R.id.photoUri, guide.getPhotoUri());
                myViewHolder.name.setTag(R.id.rating, review.getRating());
                myViewHolder.name.setTag(R.id.amount, amount);

                Picasso.with(this.context).load(guide.getPhotoUri()).into(myViewHolder.photo);

                String blActive = "false";
                String blCity = "false";
                String blCulture = "false";
                String blElse = "false";

                ArrayList list = guide.getType();
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).toString().equals("Active")) {

                        blActive = "True";
                    } else if (list.get(i).toString().equals("City")) {

                        blCity = "True";
                    } else if (list.get(i).toString().equals("Culture")) {

                        blCulture = "True";
                    } else if (list.get(i).toString().equals("Else")) {

                        blElse = "True";
                    }
                }

                myViewHolder.name.setTag(R.id.active, blActive);
                myViewHolder.name.setTag(R.id.city, blCity);
                myViewHolder.name.setTag(R.id.culture, blCulture);
                myViewHolder.name.setTag(R.id.smthElse, blElse);


                break;
            case AD_VIEW_TYPE:
                // fall through
            default:

                NativeExpressAdViewHolder nativeExpressHolder =
                        (NativeExpressAdViewHolder) holder;
                NativeExpressAdView adView =
                        (NativeExpressAdView) mRecyclerViewItems.get(position);
                ViewGroup adCardView = (ViewGroup) nativeExpressHolder.itemView;
                // The NativeExpressAdViewHolder recycled by the RecyclerView may be a different
                // instance than the one used previously for this position. Clear the
                // NativeExpressAdViewHolder of any subviews in case it has a different
                // AdView associated with it, and make sure the AdView for this position doesn't
                // already have a parent of a different recycled NativeExpressAdViewHolder.
                if (adCardView.getChildCount() > 0) {
                    adCardView.removeAllViews();
                }
                if (adView.getParent() != null) {
                    ((ViewGroup) adView.getParent()).removeView(adView);
                }

                // Add the Native Express ad to the native express ad view.
                adCardView.addView(adView);
        }


    }

    @Override
    public int getItemCount() {
        return guidesList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position % 2 == 0) ? AD_VIEW_TYPE: ITEM_VIEW_TYPE;
    }


}
