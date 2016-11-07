package be.howest.nmct.hellocal;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import be.howest.nmct.hellocal.models.AvaiableGuides;

public class TestActivity extends AppCompatActivity {

    ScaleAnimation shrinkAnim;
    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mLayoutManager;
    private TextView noGuides;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabaseReference = database.getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);


        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewList);
        noGuides = (TextView) findViewById(R.id.noGuides);

        //scale animation to shrink floating actionbar
        shrinkAnim = new ScaleAnimation(1.15f, 0f, 1.15f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        if (mRecyclerView != null) {
            //to enable optimization of recyclerview
            mRecyclerView.setHasFixedSize(true);
        }

        //using staggered grid pattern in recyclerview
        mLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

        FirebaseRecyclerAdapter<AvaiableGuides,GuideViewHolder> adapter = new FirebaseRecyclerAdapter<AvaiableGuides, GuideViewHolder>(
                AvaiableGuides.class,
                R.layout.row_list,
                GuideViewHolder.class,
                //referencing the node where we want the database to store the data from our Object
                mDatabaseReference.child("avaiableGuides").getRef()
        ) {
            @Override
            protected void populateViewHolder(GuideViewHolder viewHolder, AvaiableGuides model, int position) {
                if(noGuides.getVisibility()== View.VISIBLE){
                    noGuides.setVisibility(View.GONE);
                }
                viewHolder.textViewNaam.setText(model.getName());
                viewHolder.textViewCity.setText(model.getLocation());
                viewHolder.textViewCountry.setText(model.getLocation());
                viewHolder.textViewPrice.setText(model.getPrice());
            }
        };

        mRecyclerView.setAdapter(adapter);







    }




    public static class GuideViewHolder extends RecyclerView.ViewHolder{

        TextView textViewNaam;
        TextView textViewCity;
        TextView textViewCountry;
        TextView textViewPrice;

        public GuideViewHolder(View v) {
            super(v);
            textViewNaam = (TextView) v.findViewById(R.id.textViewNaam);
            textViewCity = (TextView) v.findViewById(R.id.textViewCity);
            textViewCountry = (TextView) v.findViewById(R.id.textViewCountry);
            textViewPrice = (TextView) v.findViewById(R.id.textViewPrice);
        }
    }
}
