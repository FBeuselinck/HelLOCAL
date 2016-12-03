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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import be.howest.nmct.hellocal.adapters.AllGuidesAdapter;
import be.howest.nmct.hellocal.models.AvaiableGuides;
import be.howest.nmct.hellocal.models.ProfileDetails;
import be.howest.nmct.hellocal.models.Reviews;

public class ListActivity extends AppCompatActivity {

    private List<AvaiableGuides> mAvailibleGuides;
    private ArrayList<String> mUserids;
    private ArrayList<ProfileDetails> mProfileDetails;
    private ArrayList<Reviews> mReviews;
    ScaleAnimation shrinkAnim;
    private StaggeredGridLayoutManager mLayoutManager;
    private TextView noGuides;

    private Map<String, List<Float>> tq = new HashMap<>();


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
    private List<Reviews> ListAllReviews = new ArrayList<>();

    private List<String> ListAmounts = new ArrayList<>();


    private List<String> Languages = new ArrayList<>();

    private Boolean lang = false;
    private Boolean typeTrue = false;
    boolean blDate = false;

    boolean UserAvailable = true;



    static Activity thisActivity = null;

    // TODO -> 1. Get all review scores | 2. If review GuideId equals userId in availabeGuides -> save score
    // TODO -> 3. Take the average and add it to ratingbar.



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
        noGuides.setVisibility(View.GONE);

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
                mAvailibleGuides = new ArrayList<>();
                List<Object> lst = new ArrayList<>(td.values());
                List keys = new ArrayList(td.keySet());
                mUserids = new ArrayList<>();
                for(int i = 0; i<lst.size(); i++){
                    AvaiableGuides ag = new AvaiableGuides();
                    Map<String, Object> ts = (HashMap<String,Object>) lst.get(i);
                    List<Object> gd = new ArrayList<>(ts.values());


                    ag.setId(keys.get(i).toString());
                    ag.country = (String) gd.get(2);
                    ag.dateFrom = (String) gd.get(7);
                    ag.dateTill = (String) gd.get(1);
                    ag.location = (String) gd.get(4);
                    ag.maxPeople = (String) gd.get(6);
                    ag.name = (String) gd.get(8);
                    ag.photoUri = (String) gd.get(3);
                    ag.price = (String) gd.get(0);
                    ag.transport = (String) gd.get(10);
                    ag.userId = (String) gd.get(5);
                    ag.type = (ArrayList<String>) gd.get(9);

                    mUserids.add(ag.userId);
                    mAvailibleGuides.add(ag);
                }

                getUsersInfo();

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


    private Reviews getReviews (String userid){
        Reviews rv = new Reviews();

        if(tq.get(userid)!=null){

            List<List<Float>> ts = new ArrayList<>(Arrays.asList(tq.get(userid)));
            String list = ts.toString();
            String[] parts = list.split(",");
            String ratingString = parts[0];
            String amountString = parts[1];
            ratingString = ratingString.substring(2,ratingString.length());
            amountString = amountString.substring(0,amountString.length()-2);
            Float ratingEffective = Float.parseFloat(ratingString) / Float.parseFloat(amountString);
            rv.setRating(ratingEffective);
            ListAmounts.add(amountString);


        }else{

            rv.setRating(Float.parseFloat("0"));
            ListAmounts.add("0");

        }

        return rv;
    }

    private boolean getTypesFilter(ArrayList<String> types){
        boolean bReturn = false;
        if(Type.equals(NO_PREF)){
            bReturn = true;
            return true;
        }
        for(int i = 0 ; i<types.size(); i++)
        {
            if(types.get(i).equals(Type)){
                bReturn = true;
                i= types.size();
            }
        }

        return  bReturn;
    }

    private boolean getDateFilter (String startDate, String tillDate){
        boolean bReturn = false;

        try{
            String myFormat = "dd/MM/yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
            Date DateFrom = sdf.parse(startDate);
            Date DateTill = sdf.parse(tillDate);
            Date DateWanted = sdf.parse(DateWant);
            bReturn =  DateWanted.compareTo(DateFrom) >=0  && DateWanted.compareTo(DateTill) <=0;
        }catch (java.text.ParseException e){
            e.printStackTrace();
            e.printStackTrace();
        }

        return bReturn;
    }

    private boolean getLanguageFilter(List<String> languages) {
        boolean bReturn = false;
        if(Language.equals(NO_PREF)) bReturn = true;
        for(int i = 0 ; i<languages.size(); i++){
            if(languages.get(i).equals(Language)){
                bReturn = true;
                i= languages.size();
            }
        }

        return bReturn;
    }

    public void filterList()
    {

        for(int i = 0 ;i < mAvailibleGuides.size(); i++)
        {
            ProfileDetails pd = getProfileDetail(mAvailibleGuides.get(i).userId);
            Reviews rv = getReviews(mAvailibleGuides.get(i).userId);
            AvaiableGuides av = mAvailibleGuides.get(i);
            if(pd.getAvailable() == null) pd.setAvailable(true);
            if(Price == null) Price ="0";
            if(pd.getAvailable()){
                if(av.country.equals(Country)){
                    if(Integer.parseInt(av.maxPeople) >= Integer.parseInt(People)){
                        if(getTypesFilter(av.getType())){
                            if(Transport.equals(av.transport) || Transport.equals(NO_PREF)){
                                if(Integer.parseInt(Price) >= Integer.parseInt(av.price) || Price.equals("0")){
                                    if(getDateFilter(av.dateFrom, av.dateTill)) {
                                            if(getLanguageFilter(pd.getLanguage())){
                                                ListAllGuides.add(av);
                                                ListAllReviews.add(rv);
                                            }
                                    }else if(DateWant.equals("")){
                                        if(getLanguageFilter(pd.getLanguage())){
                                            ListAllGuides.add(av);
                                            ListAllReviews.add(rv);

                                        }
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
                        for(int j = 0; j<mAvailibleGuides.size(); j++)
                        {
                            if(mAvailibleGuides.get(j).userId.equals((String) ts.get("profileId")))
                            {
                                pd.setAvailable((Boolean) ts.get("available"));
                                pd.setProfileId((String) ts.get("profileId"));
                                pd.setLanguage((ArrayList<String>) ts.get("language"));
                                mProfileDetails.add(pd);
                                j = mAvailibleGuides.size();
                            }
                        }
                    }
                    getReviews();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            DatabaseReference myRef = database.getReference("profileDetails");
            myRef.addListenerForSingleValueEvent(postEventListenerLang);
        }

    }


    private void getReviews(){
        if(mUserids.size() != 0){
            ValueEventListener postListenerReview = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Map<String, Object> td = (HashMap<String,Object>) dataSnapshot.getValue();

                    List<Object> lst = new ArrayList<>(td.values());


                    mReviews = new ArrayList<Reviews>();
                    Reviews rv = new Reviews();

                    for(int i = 0; i< lst.size(); i++)
                    {

                        Map<String, Object> ts = (HashMap<String,Object>) lst.get(i);

                        if(!tq.isEmpty()){

                            Boolean key = false;
                            String keyUpdate = "";
                            Map<String,Object> keyRating = new HashMap<>();;

                            Iterator it = tq.entrySet().iterator();
                            while (it.hasNext()){
                                Map.Entry pair = (Map.Entry)it.next();
                                if (pair.getKey().equals(ts.get("guideId"))){
                                    key=true;
                                    keyUpdate=pair.getKey().toString();
                                    keyRating.put(keyUpdate,pair.getValue());
                                    break;
                                }
                            }

                            if(key){

                                String id = keyUpdate;
                                List<Object> testq = new ArrayList<>(keyRating.values());

                                ArrayList<Object> wtf = new ArrayList<>(Arrays.asList(testq.get(0)));

                                String list = wtf.get(0).toString();

                                String[] parts = list.split(",");
                                String ratingString = parts[0];
                                String amountString = parts[1];

                                ratingString = ratingString.substring(1,ratingString.length());
                                amountString = amountString.substring(0,amountString.length()-1);

                                Float ratings = Float.parseFloat(ratingString);
                                Float amount = Float.parseFloat(amountString);

                                ratings = ratings + Float.parseFloat(ts.get("rating").toString());
                                amount ++;

                                tq.put(id, Arrays.asList(ratings,amount));

                            }else{
                                tq.put(ts.get("guideId").toString(), Arrays.asList(Float.parseFloat(ts.get("rating").toString()),Float.parseFloat("1")));
                            }
                        }else{
                            tq.put(ts.get("guideId").toString(), Arrays.asList(Float.parseFloat(ts.get("rating").toString()),Float.parseFloat("1")));
                        }

                    }

                    filterList();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            DatabaseReference myRef = database.getReference("reviews");
            myRef.addListenerForSingleValueEvent(postListenerReview);
        }
    }



    public void displayInList(){




        progress.dismiss();

        mAdapter = new AllGuidesAdapter(ListAllGuides,ListAllReviews,ListAmounts,thisActivity);
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
                TextView id = (TextView) v.findViewById(R.id.textViewAvailibleGuideId);

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
                intent.putExtra("Rating",Name.getTag(R.id.rating).toString());
                intent.putExtra("Date", DateWant);
                intent.putExtra("AvaiableGuidesId", id.getText());
                intent.putExtra("Amount",Name.getTag(R.id.amount).toString());


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
