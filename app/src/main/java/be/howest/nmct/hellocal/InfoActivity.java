package be.howest.nmct.hellocal;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import be.howest.nmct.hellocal.models.BookingRequests;
import be.howest.nmct.hellocal.models.Const;
import be.howest.nmct.hellocal.models.ProfileDetails;

public class InfoActivity extends AppCompatActivity {

    private String Name;
    private String City;
    private String Country;
    private String Price;
    private String UserId;
    private String Transport;
    private String Active;
    private String Culture;
    private String CityIcon;
    private String SmthElse;
    private String PhotoUri;
    private String Date;
    private String Rating;
    private String AvaiableGuidesId;
    private String Amount;

    private TextView textViewName;
    private TextView textViewCity;
    private TextView textViewCountry;
    private RatingBar ratingBar;
    private TextView textViewReviews;
    private TextView textViewPrice;
    private ImageView imagePhoto;



    private Button btnBook;
    private Button btnContact;


    private Image iconActive;
    private Image iconCulture;
    private Image iconCity;
    private Image iconElse;

    private ImageView ImageView1;
    private ImageView ImageView2;
    private ImageView ImageView3;
    private ImageView ImageView4;
    private ImageView ImageView5;
    private ImageView ImageView6;
    private ImageView ImageView7;
    private ImageView ImageView8;
    private ImageView ImageView9;
    private ImageView ImageView10;
    private ImageView ImageView11;
    private ImageView ImageView12;

    private LinearLayout linearLayout;

    private FirebaseUser mUser;

    private TextView Bio;

    private ProfileDetails profileDetails;

    FirebaseDatabase database = FirebaseDatabase.getInstance();



    static Activity thisActivity = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        thisActivity = this;

        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        Name = intent.getStringExtra("Name");
        City = intent.getStringExtra("City");
        Country = intent.getStringExtra("Country");
        Price = intent.getStringExtra("Price");
        UserId = intent.getStringExtra("UserId");
        Date = intent.getStringExtra("Date");
        AvaiableGuidesId = intent.getStringExtra("AvaiableGuidesId");

        Transport = intent.getStringExtra("Transport");
        Active = intent.getStringExtra("Active");
        CityIcon = intent.getStringExtra("City2");
        Culture = intent.getStringExtra("Culture");
        SmthElse = intent.getStringExtra("SmthElse");
        PhotoUri = intent.getStringExtra("PhotoUri");
        Amount = intent.getStringExtra("Amount");
        Rating = intent.getStringExtra("Rating");


        textViewName = (TextView) findViewById(R.id.textViewName);
        textViewCity = (TextView) findViewById(R.id.textViewCity);
        textViewCountry = (TextView) findViewById(R.id.textViewCountry);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        textViewReviews = (TextView) findViewById(R.id.textViewReviews);
        textViewPrice = (TextView) findViewById(R.id.textViewPrice);
        imagePhoto = (ImageView) findViewById(R.id.imagePhoto);



        btnBook = (Button) findViewById(R.id.btnBook);
        btnContact = (Button) findViewById(R.id.btnContact);

        Bio = (TextView) findViewById(R.id.textViewBio);

        ImageView1 = (ImageView) findViewById(R.id.imageIcon1);
        ImageView2 = (ImageView) findViewById(R.id.imageIcon2);
        ImageView3 = (ImageView) findViewById(R.id.imageIcon3);
        ImageView4 = (ImageView) findViewById(R.id.imageIcon4);
        ImageView5 = (ImageView) findViewById(R.id.imageIcon5);

        ImageView6 = (ImageView) findViewById(R.id.imageIconLang1);
        ImageView7 = (ImageView) findViewById(R.id.imageIconLang2);
        ImageView8 = (ImageView) findViewById(R.id.imageIconLang3);
        ImageView9 = (ImageView) findViewById(R.id.imageIconLang4);
        ImageView10 = (ImageView) findViewById(R.id.imageIconLang5);
        ImageView11 = (ImageView) findViewById(R.id.imageIconLang6);
        ImageView12 = (ImageView) findViewById(R.id.imageIconLang7);

        ImageView6.setVisibility(View.GONE);
        ImageView7.setVisibility(View.GONE);
        ImageView8.setVisibility(View.GONE);
        ImageView9.setVisibility(View.GONE);
        ImageView10.setVisibility(View.GONE);
        ImageView11.setVisibility(View.GONE);
        ImageView12.setVisibility(View.GONE);

        ImageView1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Toast.makeText(getApplicationContext(), "Active tour", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        ImageView2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Toast.makeText(getApplicationContext(), "City tour", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        ImageView3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Toast.makeText(getApplicationContext(), "Culture tour", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        ImageView4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Toast.makeText(getApplicationContext(), "Special tour", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        ImageView6.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Toast.makeText(getApplicationContext(), "English", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        ImageView7.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Toast.makeText(getApplicationContext(), "Dutch", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        ImageView8.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Toast.makeText(getApplicationContext(), "French", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        ImageView9.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Toast.makeText(getApplicationContext(), "German", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        ImageView10.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Toast.makeText(getApplicationContext(), "Spanish", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        ImageView11.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Toast.makeText(getApplicationContext(), "Portuguese", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        ImageView12.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Toast.makeText(getApplicationContext(), "Russian", Toast.LENGTH_SHORT).show();
                return false;
            }
        });


        linearLayout = (LinearLayout) findViewById(R.id.LinearLayoutsz4);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(thisActivity, ReviewActivity.class);
                intent.putExtra("userId",UserId);
//                intent.putExtra("name",Name);
//                intent.putExtra("photo",PhotoUri);
                thisActivity.startActivity(intent);
            }
        });



//        Toast.makeText(getApplicationContext(), UserId,
//                Toast.LENGTH_LONG).show();

        ratingBar.setRating(Float.parseFloat(Rating));

        String parts[] = Amount.split("\\.");
        String amountInt = parts[0];

        amountInt = amountInt.substring(1,amountInt.length());

        textViewReviews.setText(amountInt + " reviews");

        textViewName.setText(Name);
        textViewCity.setText(City);
        textViewCountry.setText(Country);
        textViewPrice.setText(Price);
        btnBook.setText("BOOK " + Name.toUpperCase());

        btnContact.setText("CONTACT " + Name.toUpperCase());
        btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Contact();
            }
        });

        Picasso.with(this.getApplicationContext()).load(PhotoUri.toString()).into(imagePhoto);

        if(Active.equals("True")&&CityIcon.equals("false")&&Culture.equals("false")&&SmthElse.equals("false")){
            ImageView1.setImageResource(R.drawable.active);
            ImageView2.setVisibility(View.GONE);
            ImageView3.setVisibility(View.GONE);
            ImageView4.setVisibility(View.GONE);
        }else if(Active.equals("True")&&CityIcon.equals("True")&&Culture.equals("false")&&SmthElse.equals("false")){
            ImageView1.setImageResource(R.drawable.active);
            ImageView2.setImageResource(R.drawable.city);
            ImageView3.setVisibility(View.GONE);
            ImageView4.setVisibility(View.GONE);
        }else if(Active.equals("True")&&CityIcon.equals("True")&&Culture.equals("True")&&SmthElse.equals("false")){
            ImageView1.setImageResource(R.drawable.active);
            ImageView2.setImageResource(R.drawable.city);
            ImageView3.setImageResource(R.drawable.culture);
            ImageView4.setVisibility(View.GONE);
        }else if(Active.equals("True")&&CityIcon.equals("false")&&Culture.equals("True")&&SmthElse.equals("false")){
            ImageView2.setImageResource(R.drawable.city);
            ImageView1.setVisibility(View.GONE);
            ImageView3.setVisibility(View.GONE);
            ImageView4.setVisibility(View.GONE);
        }else if(Active.equals("True")&&CityIcon.equals("false")&&Culture.equals("false")&&SmthElse.equals("True")){
            ImageView2.setImageResource(R.drawable.city);
            ImageView1.setVisibility(View.GONE);
            ImageView3.setVisibility(View.GONE);
            ImageView4.setVisibility(View.GONE);
        }else if(CityIcon.equals("True")&&Active.equals("false")&&Culture.equals("false")&&SmthElse.equals("false")){
            ImageView2.setImageResource(R.drawable.city);
            ImageView1.setVisibility(View.GONE);
            ImageView3.setVisibility(View.GONE);
            ImageView4.setVisibility(View.GONE);
        }else if(CityIcon.equals("True")&&Active.equals("false")&&Culture.equals("True")&&SmthElse.equals("false")){
            ImageView2.setImageResource(R.drawable.city);
            ImageView1.setVisibility(View.GONE);
            ImageView3.setImageResource(R.drawable.culture);
            ImageView4.setVisibility(View.GONE);
        }else if(CityIcon.equals("True")&&Active.equals("false")&&Culture.equals("True")&&SmthElse.equals("True")){
            ImageView2.setImageResource(R.drawable.city);
            ImageView1.setVisibility(View.GONE);
            ImageView3.setImageResource(R.drawable.culture);
            ImageView4.setImageResource(R.drawable.smthelse);
        }else if(Culture.equals("True")&&Active.equals("false")&&CityIcon.equals("false")&&SmthElse.equals("false")){
            ImageView3.setImageResource(R.drawable.culture);
            ImageView2.setVisibility(View.GONE);
            ImageView1.setVisibility(View.GONE);
            ImageView4.setVisibility(View.GONE);
        }else if(Culture.equals("True")&&Active.equals("false")&&CityIcon.equals("false")&&SmthElse.equals("True")){
            ImageView3.setImageResource(R.drawable.culture);
            ImageView2.setVisibility(View.GONE);
            ImageView1.setVisibility(View.GONE);
            ImageView4.setImageResource(R.drawable.smthelse);
        }else if(SmthElse.equals("True")&&Active.equals("false")&&Culture.equals("false")&&CityIcon.equals("false")){
            ImageView4.setImageResource(R.drawable.smthelse);
            ImageView2.setVisibility(View.GONE);
            ImageView3.setVisibility(View.GONE);
            ImageView1.setVisibility(View.GONE);
        }else{
            ImageView1.setImageResource(R.drawable.active);
            ImageView2.setImageResource(R.drawable.city);
            ImageView3.setImageResource(R.drawable.culture);
            ImageView4.setImageResource(R.drawable.smthelse);
        }


        if(Transport.equals("Guides transport")){
            ImageView5.setImageResource(R.drawable.guidetransport);
            ImageView5.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Toast.makeText(getApplicationContext(), "Guides transport", Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
        }else if(Transport.equals("Own transport")){
            ImageView5.setImageResource(R.drawable.owntransport);
            ImageView5.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Toast.makeText(getApplicationContext(), "Own transport", Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
        }else if(Transport.equals("No transport")){
            ImageView5.setImageResource(R.drawable.notransport);
            ImageView5.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Toast.makeText(getApplicationContext(), "No transport", Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
        }else if(Transport.equals("No preference")){
            ImageView5.setImageResource(R.drawable.notransport);
            ImageView5.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Toast.makeText(getApplicationContext(), "No transport preference", Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
        }

        mUser = FirebaseAuth.getInstance().getCurrentUser();

        getProfile();

        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeBookingRequest();
            }
        });


    }

    private void makeBookingRequest(){
        if(mUser.getUid() == UserId){
            Toast.makeText(this, "You can't book yourself", Toast.LENGTH_SHORT).show();
        }
        else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    this);

            // set title
            alertDialogBuilder.setTitle("Are you Sure");

            // set dialog message
            alertDialogBuilder
                    .setMessage("Are you sure you want to book " + Name + "?")
                    .setCancelable(false)
                    .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            // if this button is clicked, close
                            // current activity
                            RequestComfirmed();
                        }
                    })
                    .setNegativeButton("No",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // if this button is clicked, just close
                                    // the dialog box and do nothing
                                    dialog.cancel();
                                }
                            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    private void RequestComfirmed()
    {
        BookingRequests br = new BookingRequests(UserId, mUser.getUid(), false, Date, AvaiableGuidesId);
        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mDatabaseReference.child("bookingRequests").push().setValue(br);
        Toast.makeText(this, "Booking requested", Toast.LENGTH_SHORT).show();

        Map notification = new HashMap<>();
        notification.put("userId", UserId);
        notification.put("message", "You have a new bookingrequest!");

        FirebaseDatabase.getInstance().getReference("notificationRequests").push().setValue(notification);
    }

    private void getProfile(){
        ValueEventListener postEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                profileDetails = dataSnapshot.getValue(ProfileDetails.class);
                getLanguages();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        DatabaseReference myRef = database.getReference("profileDetails").child(UserId);
        myRef.addListenerForSingleValueEvent(postEventListener);
    }

    private void getLanguages()
    {
        ShowLanguages(profileDetails.getLanguage());
        Bio.setText(profileDetails.getDescription());
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

    private void ShowLanguages(List<String> listLanguages)
    {


        for(int i = 0; i < listLanguages.size(); i++){
            if(listLanguages.get(i).equals("English") ){
                ImageView6.setVisibility(View.VISIBLE);
                ImageView6.setImageResource(R.drawable.english);
            }else if(listLanguages.get(i).equals("Dutch") ){
                ImageView7.setVisibility(View.VISIBLE);
                ImageView7.setImageResource(R.drawable.dutch);
            }else if(listLanguages.get(i).equals("French") ){
                ImageView8.setVisibility(View.VISIBLE);
                ImageView8.setImageResource(R.drawable.french);
            }else if(listLanguages.get(i).equals("German") ){
                ImageView9.setVisibility(View.VISIBLE);
                ImageView9.setImageResource(R.drawable.german);
            }else if(listLanguages.get(i).equals("Spanish") ){
                ImageView10.setVisibility(View.VISIBLE);
                ImageView10.setImageResource(R.drawable.spanish);
            }else if(listLanguages.get(i).equals("Portuguese") ){
                ImageView11.setVisibility(View.VISIBLE);
                ImageView11.setImageResource(R.drawable.portuguese);
            }else if(listLanguages.get(i).equals("Russian") ){
                ImageView12.setVisibility(View.VISIBLE);
                ImageView12.setImageResource(R.drawable.russian);
            }
        }

    }

    private void Contact(){
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra(Const.EXTRA_DATA, (Serializable) profileDetails);
        startActivity(intent);
    }
}
