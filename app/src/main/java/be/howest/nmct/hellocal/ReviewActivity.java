package be.howest.nmct.hellocal;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
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
import be.howest.nmct.hellocal.models.ProfileDetails;
import be.howest.nmct.hellocal.models.Reviews;

public class ReviewActivity extends AppCompatActivity {

    FirebaseUser mUser;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    String Uid;

    private List<Reviews> mAvailableReviews;
    private ArrayList<String> mUserids;
    private ArrayList<ProfileDetails> mProfileDetails;

    private List<Reviews> listReviews = new ArrayList<>();
    private RecyclerView recyclerView;
    private ReviewAdapter mAdapter;

    private List<String> ListNames = new ArrayList<>();
    private List<String> ListPhotos = new ArrayList<>();


    private String Name, uid,photo;
    private EditText comment;
    private RatingBar score;
    private Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        uid = intent.getStringExtra("userId");


        comment = (EditText) findViewById(R.id.editTextComment);
        score = (RatingBar) findViewById(R.id.ratingBars);
        save = (Button) findViewById(R.id.buttonSave);


        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewReviews);


        getCurrentUser();

        getAllReviews();



        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postReview();
            }
        });


    }

    public void getCurrentUser(){
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        if(mUser != null){
            Uid = mUser.getUid();
        }
    }


    public void getAllReviews(){


        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Map<String, Object> td = (HashMap<String,Object>) dataSnapshot.getValue();

                List<Object> lst = new ArrayList<>(td.values());


                mAvailableReviews = new ArrayList<>();
                mUserids = new ArrayList<>();



                for (int i=0; i<lst.size(); i++){

                    Reviews review = new Reviews();

                    Map<String, Object> ts = (HashMap<String,Object>) lst.get(i);
                    List<Object> list = new ArrayList<>(ts.values());

                    Log.e("list",list.toString());

                        review.comment = list.get(2).toString();
                        review.rating = Float.parseFloat(list.get(3).toString());
                        review.userId = list.get(0).toString();
                        review.guideId = list.get(1).toString();


                        mUserids.add(review.userId);
                        mAvailableReviews.add(review);







                }
                getUsersInfo();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        };
        DatabaseReference myRef = database.getReference("reviews");
        myRef.addListenerForSingleValueEvent(postListener);

    }


    private ProfileDetails getProfileDetail (String userid){
        ProfileDetails pd = new ProfileDetails();
        for(int i = 0 ; i < mProfileDetails.size(); i++){
            if(mProfileDetails.get(i).getProfileId().equals(userid)){
                pd = mProfileDetails.get(i);
                i = mProfileDetails.size();
            }
        }
        return pd;
    }

    public void filterList()
    {
        for(int i = 0 ;i < mAvailableReviews.size(); i++) {
            ProfileDetails pd = getProfileDetail(mAvailableReviews.get(i).userId);
            Reviews rv = mAvailableReviews.get(i);

            if(rv.guideId.equals(uid)){

                ListNames.add(pd.getName());
                ListPhotos.add(pd.getPhotoUri());

                listReviews.add(rv);
            }



        }
        displayInList();
    }



    private void  getUsersInfo(){
        if(mUserids.size() != 0){
            ValueEventListener postEventListenerLang = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Map<String, Object> td = (HashMap<String,Object>) dataSnapshot.getValue();
                    List<Object> lst = new ArrayList<>(td.values());

                    mProfileDetails = new ArrayList<ProfileDetails>();
                    for(int i = 0; i< lst.size(); i++)
                    {

                        ProfileDetails pd = new ProfileDetails();
                        Map<String, Object> ts = (HashMap<String,Object>) lst.get(i);
                        for(int j = 0; j<mAvailableReviews.size(); j++)
                        {
                            if(mAvailableReviews.get(j).userId.equals((String) ts.get("profileId")))
                            {
                                pd.setName((String) ts.get("name"));
                                pd.setProfileId((String) ts.get("profileId"));
                                pd.setPhotoUri((String) ts.get("photoUri"));
                                mProfileDetails.add(pd);
                                j = mAvailableReviews.size();
                            }
                        }
                    }
                    filterList();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            DatabaseReference myRef = database.getReference("profileDetails");
            myRef.addListenerForSingleValueEvent(postEventListenerLang);
        }

    }



    public void displayInList(){




        mAdapter = new ReviewAdapter(listReviews,ListNames,ListPhotos,this);
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


    private void postReview(){

        if(!comment.equals("")){
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            Reviews review = new Reviews(comment.getText().toString(),score.getRating(),Uid, uid);
            mDatabase.child("reviews").push().setValue(review);
            Toast.makeText(getApplication(), "Succesfully saved your review!", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplication(), "Please add some text", Toast.LENGTH_SHORT).show();

        }



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return(super.onOptionsItemSelected(item));

    }



}
