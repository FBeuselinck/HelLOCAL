package be.howest.nmct.hellocal;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import be.howest.nmct.hellocal.adapters.AllGuidesAdapter;
import be.howest.nmct.hellocal.models.AvaiableGuides;
import be.howest.nmct.hellocal.models.ProfileDetails;

public class ListActivity extends AppCompatActivity {

    ScaleAnimation shrinkAnim;
    private StaggeredGridLayoutManager mLayoutManager;
    private TextView noGuides;

    private String filterCountry, filterMaxPeople, filterTransport, filterPrice, filterName,filterLocation,filterDateFrom,filterDateTill,filterUserId,filterPhotoUri;
    private ArrayList<String> filterType;
    private String Location;
    private String Country;
    private String DateWant;
    private String People;
    private String Transport;
    private String Type;
    private String Language;
    private String Price;

    private String blActive = "false";
    private String blCulture = "false";
    private String blCity = "false";
    private String blElse = "false";

    private ProgressDialog progress;

    private static final String NO_PREF = "No preference";


//    private String Combined;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabaseReference = database.getReference();

    private AllGuidesAdapter mAdapter;
    private RecyclerView recyclerView;


    private List<AvaiableGuides> ListAllGuides = new ArrayList<>();

    private List<String> Languages = new ArrayList<>();

    private Boolean lang = false;
    private Boolean typeTrue = false;
    boolean blDate = false;

    boolean UserAvailable = true;



    static Activity thisActivity = null;

    // TODO -> 1. Get all profiledetails | 2. Get all user IDs where 'available' = false |
    // TODO -> 3. for each availableBooking, check if user id equals user ID from (2)
    // TODO -> 4. if 3 -> do not show in list


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        thisActivity = this;

        Intent intent = getIntent();
        Location = intent.getStringExtra("Location");
        Country = intent.getStringExtra("Country");
        DateWant = intent.getStringExtra("Date");
        People = intent.getStringExtra("People");
        Transport = intent.getStringExtra("Transport");
        Type = intent.getStringExtra("Type");
        Language = intent.getStringExtra("Language");
        Price = intent.getStringExtra("Price");

        noGuides = (TextView) findViewById(R.id.noGuides);
        noGuides.setVisibility(View.INVISIBLE);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewList);

        progress = new ProgressDialog(this);
        progress.setTitle("Loading guides");
        progress.setMessage("Hold on, we are finding your guides!");
        progress.setCancelable(false);
        progress.show();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
//                noGuides.setVisibility(View.VISIBLE);

                progress.dismiss();
            }
        }, 5000);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        //scale animation to shrink floating actionbar
        shrinkAnim = new ScaleAnimation(1.15f, 0f, 1.15f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        getAllAvailableBookings();

    }

    public void getAllAvailableBookings(){


        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Map<String, Object> td = (HashMap<String,Object>) dataSnapshot.getValue();

                List<Object> values = new ArrayList<>(td.values());


                for (int i=0; i<values.size(); i++){

                    Map<String, Object> ts = (HashMap<String,Object>) values.get(i);
                    List<Object> list = new ArrayList<>(ts.values());


                       filterName = list.get(8).toString();
                       filterCountry = list.get(2).toString();
                       filterLocation = list.get(4).toString();
                       filterDateFrom = list.get(7).toString();
                       filterDateTill = list.get(1).toString();
                       filterMaxPeople = list.get(6).toString();
                       filterPrice = list.get(0).toString();
                       filterTransport = list.get(10).toString();
                       filterUserId = list.get(5).toString();
                       filterPhotoUri = list.get(3).toString();

                    ArrayList<String> list2 = (ArrayList<String>) list.get(9);
                    filterType = new ArrayList<>();


                    for (int o = 0; o<list2.size();o++){
                        filterType.add(list2.get(o).toString());
                        if(list2.get(o).equals(Type)){
                            typeTrue = true;
                        }
                    }

                    if(Type.equals(NO_PREF)){
                        typeTrue = true;
                    }

                    try{
                        String myFormat = "dd/MM/yyyy";
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
                        Date DateFrom = sdf.parse(filterDateFrom);
                        Date DateTill = sdf.parse(filterDateTill);
                        Date DateWanted = sdf.parse(DateWant);

                        blDate =  DateWanted.compareTo(DateFrom) >=0  && DateWanted.compareTo(DateTill) <=0;

                    }catch(java.text.ParseException e){
                        e.printStackTrace();
                    }

                    if (Price == null){
                        Price = "0";
                    }

                    getLang(filterUserId);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        };


        DatabaseReference myRef = database.getReference("avaiableGuides");

        Query queryRef;
        if (Location.isEmpty()) {
            queryRef = myRef;
        }else{
            queryRef = myRef.orderByChild("location").equalTo(Location);

        }

        queryRef.addListenerForSingleValueEvent(postListener);

    }

    public void filterList()
    {
        if(UserAvailable){
            if(Country.equals(filterCountry)){
                if(Integer.parseInt(People) <= Integer.parseInt(filterMaxPeople) ){
                    if(typeTrue){
                        if(Transport.equals(filterTransport)||Transport.equals(NO_PREF)){
                            if(Integer.parseInt(Price) >= Integer.parseInt(filterPrice) || Price.equals("0")){
                                if(blDate){
                                    if(lang){
                                        AvaiableGuides guide = new AvaiableGuides(filterName,filterCountry,filterLocation,filterDateFrom,filterDateTill,filterMaxPeople,filterPrice,filterType,filterTransport,filterUserId,filterPhotoUri);
                                        ListAllGuides.add(guide);
                                    }else{
                                        Toast.makeText(getApplicationContext(), "nuuuuuup",
                                                Toast.LENGTH_LONG).show();
                                    }

                                }
                            }
                        }
                    }
                }
            }
        }

        displayInList();
    }

    public void getLang(String userId){

        ValueEventListener postEventListenerLang = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ProfileDetails profileDetails = dataSnapshot.getValue(ProfileDetails.class);

                Languages = profileDetails.getLanguage();
                UserAvailable = profileDetails.getAvailable();
                if(Languages != null) {
                    for (int o = 0; o < Languages.size(); o++) {
                        if (Languages.get(o).equals(Language)) {
                            lang = true;
                        }
                    }
                }
                filterList();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        };
        DatabaseReference myRef = database.getReference("profileDetails").child(userId);
        myRef.addListenerForSingleValueEvent(postEventListenerLang);

    }



    public void displayInList(){


        progress.dismiss();

        mAdapter = new AllGuidesAdapter(ListAllGuides,thisActivity);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
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
                intent.putExtra("Transport",Name.getTag(R.id.transport).toString());
                intent.putExtra("Active",Name.getTag(R.id.active).toString());
                intent.putExtra("City2",Name.getTag(R.id.city).toString());
                intent.putExtra("Culture",Name.getTag(R.id.culture).toString());
                intent.putExtra("SmthElse",Name.getTag(R.id.smthElse).toString());
                intent.putExtra("PhotoUri",Name.getTag(R.id.photoUri).toString());



                thisActivity.startActivity(intent);

            }
        });


    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.orderby, menu);
        return true;
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
