package be.howest.nmct.hellocal;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import be.howest.nmct.hellocal.models.AvaiableGuides;

public class TestActivity extends AppCompatActivity {

    ScaleAnimation shrinkAnim;
    private static RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mLayoutManager;
    private TextView noGuides;

    private String Location;
    private String Country;
    private String Date;
    private String People;
    private String Transport;
    private String Type;
    private String Language;
    private String Price;

//    private String Combined;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabaseReference = database.getReference();




    static Activity thisActivity = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        thisActivity = this;

        Intent intent = getIntent();
        Location = intent.getStringExtra("Location");
        Country = intent.getStringExtra("Country");
        Date = intent.getStringExtra("Date");
        People = intent.getStringExtra("People");
        Transport = intent.getStringExtra("Transport");
        Type = intent.getStringExtra("Type");
        Language = intent.getStringExtra("Language");
        Price = intent.getStringExtra("Price");



        final ProgressDialog progress = new ProgressDialog(thisActivity);
        progress.setTitle("Loading guides");
        progress.setMessage("Hold on, we are finding your guides!");
        progress.setCancelable(false);
        progress.show();


        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);


        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewList);
        noGuides = (TextView) findViewById(R.id.noGuides);
        noGuides.setVisibility(View.INVISIBLE);

        //scale animation to shrink floating actionbar
        shrinkAnim = new ScaleAnimation(1.15f, 0f, 1.15f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        if (mRecyclerView != null) {
            //to enable optimization of recyclerview
            mRecyclerView.setHasFixedSize(true);
        }

        //using staggered grid pattern in recyclerview
        mLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mDatabaseReference = mDatabaseReference.child("avaiableGuides").getRef();

        Query queryRef;

        if (Location.isEmpty()) {
            queryRef = mDatabaseReference;
        }else{
            queryRef = mDatabaseReference.orderByChild("location").equalTo(Location);

        }



        FirebaseRecyclerAdapter<AvaiableGuides,GuideViewHolder> adapter = new FirebaseRecyclerAdapter<AvaiableGuides, GuideViewHolder>(
                AvaiableGuides.class,
                R.layout.row_list,
                GuideViewHolder.class,
                //referencing the node where we want the database to store the data from our Object
                queryRef
        ) {
            @Override
            protected void populateViewHolder(GuideViewHolder viewHolder, AvaiableGuides model, int position) {
//                if(noGuides.getVisibility()== View.VISIBLE){
                    progress.dismiss();

//                    noGuides.setVisibility(View.GONE);
//                }


                    viewHolder.textViewNaam.setText(model.getName());
                    viewHolder.textViewCity.setText(model.getLocation());
                    viewHolder.textViewCountry.setText(model.getCountry());
                    viewHolder.textViewPrice.setText("€ "+model.getPrice()+"/h");




                    viewHolder.textViewNaam.setTag(R.id.guideId,model.getUserId());





            }
        };

        mRecyclerView.setAdapter(adapter);

    }



    public static class GuideViewHolder extends RecyclerView.ViewHolder{

        TextView textViewNaam;
        TextView textViewCity;
        TextView textViewCountry;
        TextView textViewPrice;

        String transport;

        public AvaiableGuides avaiableGuides;

        public GuideViewHolder(View v) {
            super(v);
            textViewNaam = (TextView) v.findViewById(R.id.textViewNaam);
            textViewCity = (TextView) v.findViewById(R.id.textViewCity);
            textViewCountry = (TextView) v.findViewById(R.id.textViewCountry);
            textViewPrice = (TextView) v.findViewById(R.id.textViewPrice);


            ItemClickSupport.addTo(mRecyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                @Override
                public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                    TextView Name = (TextView) v.findViewById(R.id.textViewNaam);
                    TextView City = (TextView) v.findViewById(R.id.textViewCity);
                    TextView Country = (TextView) v.findViewById(R.id.textViewCountry);
                    TextView Price = (TextView) v.findViewById(R.id.textViewPrice);




                    Intent intent = new Intent(thisActivity, InfoActivity.class);
                    intent.putExtra("Name",Name.getText().toString());
                    intent.putExtra("City",City.getText().toString());
                    intent.putExtra("Country",Country.getText().toString());
                    intent.putExtra("Price",Price.getText().toString());
                    intent.putExtra("UserId",Name.getTag(R.id.guideId).toString());

                    thisActivity.startActivity(intent);
                }
            });

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.orderby, menu);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_price_low: populateRecyclerView("PriceLow");
//                return true;
//            case R.id.action_price_high: populateRecyclerView("PriceHigh");
//                return true;
//            case R.id.action_name: populateRecyclerView("Name");
//                return true;
//            case R.id.action_rating: populateRecyclerView("Rating");
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
//
//
//    public void populateRecyclerView(String OrderBy){
//
//        String Order = OrderBy;
//
//        mDatabaseReference = mDatabaseReference.child("avaiableGuides").getRef();
//        Query queryRef = mDatabaseReference.orderByChild("location").equalTo(Location);
//
//        switch (Order){
//            case "PriceLow": queryRef = mDatabaseReference.orderByChild("location").equalTo(Location);
//        }
//
//
//
//
//        FirebaseRecyclerAdapter<AvaiableGuides,GuideViewHolder> adapter = new FirebaseRecyclerAdapter<AvaiableGuides, GuideViewHolder>(
//                AvaiableGuides.class,
//                R.layout.row_list,
//                GuideViewHolder.class,
//                //referencing the node where we want the database to store the data from our Object
//                queryRef
//        ) {
//            @Override
//            protected void populateViewHolder(GuideViewHolder viewHolder, AvaiableGuides model, int position) {
//                if(noGuides.getVisibility()== View.VISIBLE){
//                    noGuides.setVisibility(View.GONE);
//                }
//
//
//                viewHolder.textViewNaam.setText(model.getName());
//                viewHolder.textViewCity.setText(model.getLocation());
//                viewHolder.textViewCountry.setText(model.getCountry());
//                viewHolder.textViewPrice.setText("€ "+model.getPrice()+"/h");
//
//
//
//
//                viewHolder.textViewNaam.setTag(R.id.guideId,model.getUserId());
//
//
//
//
//
//            }
//        };
//
//        mRecyclerView.setAdapter(adapter);
//    }

}
