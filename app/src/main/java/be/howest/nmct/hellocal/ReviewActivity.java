package be.howest.nmct.hellocal;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import be.howest.nmct.hellocal.adapters.AvailabeGuidesAdapter;
import be.howest.nmct.hellocal.adapters.ReviewAdapter;
import be.howest.nmct.hellocal.models.AvaiableGuides;
import be.howest.nmct.hellocal.models.Reviews;

public class ReviewActivity extends AppCompatActivity {

    FirebaseUser mUser;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    String Uid;

    private List<Reviews> listReviews = new ArrayList<>();
    private RecyclerView recyclerView;
    private ReviewAdapter mAdapter;

    private String Name, uid,photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Name = intent.getStringExtra("name");
        uid = intent.getStringExtra("userId");
        photo = intent.getStringExtra("photo");



        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewReviews);

        getCurrentUserId();

        getAllReviews();




    }

    public void getCurrentUserId(){

        mUser = FirebaseAuth.getInstance().getCurrentUser();

        if(mUser != null){
            Uid = mUser.getUid();
        }

    }

    public void getAllReviews(){


        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<Object> td = (ArrayList<Object>) dataSnapshot.getValue();




                for (int i=0; i<td.size(); i++){

                    Map<String, Object> ts = (HashMap<String,Object>) td.get(i);
                    List<Object> list = new ArrayList<>(ts.values());

                    Log.e("list",list.toString());

                        String comment = list.get(1).toString();
                        Float rating = Float.parseFloat(list.get(2).toString());
                        String userId = list.get(0).toString();




                        Reviews review = new Reviews(comment,rating,userId);

                        listReviews.add(review);



                }
                displayInList();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        };
        DatabaseReference myRef = database.getReference("reviews");
        myRef.addListenerForSingleValueEvent(postListener);

    }

    public void displayInList(){




        mAdapter = new ReviewAdapter(listReviews,Name,photo,this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

//        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
//            @Override
//            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
//
//                TextView date = (TextView) v.findViewById(R.id.textViewTime);
//
//
//                Toast.makeText(getActivity(), date.getText().toString(),
//                        Toast.LENGTH_LONG).show();
//
//            }
//        });


    }


}
