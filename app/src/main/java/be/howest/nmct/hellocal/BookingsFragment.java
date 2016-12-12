package be.howest.nmct.hellocal;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
<<<<<<< HEAD
import be.howest.nmct.hellocal.adapters.bookings_as_guide_adapter;
=======
import be.howest.nmct.hellocal.contracts.SqliteContract;
import be.howest.nmct.hellocal.helpers.SqliteHelper;
>>>>>>> origin/develop
import be.howest.nmct.hellocal.models.AvaiableGuides;
import be.howest.nmct.hellocal.models.BookingRequests;
import be.howest.nmct.hellocal.models.Gender;
import be.howest.nmct.hellocal.models.ProfileDetails;
import be.howest.nmct.hellocal.models.Reviews;


public class BookingsFragment extends Fragment {

    FirebaseUser mUser;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    SqliteHelper mdbHelper;
    Boolean mBooleanIsOnline;

    String Uid;
    String UidAvailabe;

    private List<AvaiableGuides> ListUserGuides = new ArrayList<>();
    private List<AvaiableGuides> ListUserGuides2 = new ArrayList<>();
    private List<AvaiableGuides> ListUserGuidesRequest = new ArrayList<>();
    private List<BookingRequests> ListBookingRequests = new ArrayList<>();
    private List<String> ListBookingIds = new ArrayList<>();
    private List<String> ListAvailableGuidesIds = new ArrayList<>();
    private List<String> ListRequestUserIds = new ArrayList<>();
    private List<ProfileDetails> ListProfileDetails = new ArrayList<>();
    private RecyclerView recyclerView, recyclerViewBookings;
    private AvailabeGuidesAdapter mAdapter;
    private bookings_as_guide_adapter mBAdapter;

    private ProgressDialog progress;

    private TextView textViewNoBookings;
    private TextView textViewBookings;
    private TextView textViewAvailable;
    private TextView textViewDate;

    private Button btnRemove;

    static Activity thisActivity = null;



    public BookingsFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_bookings, container, false);

        thisActivity = this.getActivity();


        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerViewBookings);
        recyclerViewBookings = (RecyclerView) v.findViewById(R.id.recyclerViewBookings2);

        textViewNoBookings = (TextView) v.findViewById(R.id.noGuides);
        textViewAvailable = (TextView) v.findViewById(R.id.availableBookings);
        textViewBookings = (TextView) v.findViewById(R.id.Bookings);

        textViewDate = (TextView) v.findViewById(R.id.textViewDate);

        btnRemove = (Button) v.findViewById(R.id.btnClose);



        textViewNoBookings.setVisibility(View.GONE);
        textViewAvailable.setVisibility(View.GONE);
        textViewBookings.setVisibility(View.GONE);


        progress = new ProgressDialog(getActivity());
        progress.setTitle("Loading bookings");
        progress.setMessage("Hold on, we are finding your bookings!");
        progress.setCancelable(false);
        progress.show();

        mdbHelper = new SqliteHelper(v.getContext());

        ConnectivityManager connMgr = (ConnectivityManager) getActivity()
                .getSystemService(v.getContext().CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            mBooleanIsOnline = true;
        } else {
            mBooleanIsOnline = false;
        }


        getCurrentUserId();

        getAllAvailableBookings();

        getAllBookingRequests();


        return v;
    }


    public void getCurrentUserId(){

        mUser = FirebaseAuth.getInstance().getCurrentUser();

        if(mUser != null){
            Uid = mUser.getUid();
        }

    }

    public void getAllAvailableBookings(){


        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Map<String, Object> td = (HashMap<String,Object>) dataSnapshot.getValue();
                List Keys = new ArrayList(td.keySet());

                List<Object> values = new ArrayList<>(td.values());


                for (int i=0; i<values.size(); i++){

                    Map<String, Object> ts = (HashMap<String,Object>) values.get(i);
                    List<Object> list = new ArrayList<>(ts.values());

                    UidAvailabe = list.get(5).toString();
                    if (UidAvailabe.equals(Uid)){


                        String name = list.get(8).toString();
                        String country = list.get(2).toString();
                        String location = list.get(4).toString();
                        String dateFrom = list.get(7).toString();
                        String dateTill = list.get(1).toString();
                        String maxPeople = list.get(6).toString();
                        String price = list.get(0).toString();
                        String transport = list.get(10).toString();
                        String userId = list.get(5).toString();
                        String photoUri = list.get(3).toString();


                        ArrayList<String> list2 = (ArrayList<String>) list.get(9);
                        ArrayList<String> type = new ArrayList<>();

                         for (int o = 0; o<list2.size();o++){
                             type.add(list2.get(o).toString());
                           }



                        AvaiableGuides guide = new AvaiableGuides(name,country,location,dateFrom,dateTill,maxPeople,price,type,transport,userId,photoUri);

                        ListUserGuides.add(guide);
                        ListBookingIds.add(Keys.get(i).toString());

                    }

                }
<<<<<<< HEAD
                displayInList("first");
=======
                displayInList();
                sqliteSave();
>>>>>>> origin/develop

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        };
        if(mBooleanIsOnline)
        {
            DatabaseReference myRef = database.getReference("avaiableGuides");
            myRef.addListenerForSingleValueEvent(postListener);
        }
        else getDataFromSql();


    }

    public void getDataFromSql()
    {
        ListUserGuides = new ArrayList<>();

        SQLiteDatabase db = mdbHelper.getReadableDatabase();

        String[] projection = {
                SqliteContract.MyBookings._ID,
                SqliteContract.MyBookings.COLUMN_TRANSPORT,
                SqliteContract.MyBookings.COLUMN_PRICE,
                SqliteContract.MyBookings.COLUMN_MAXPOEPLE,
                SqliteContract.MyBookings.COLUMN_LOCATION,
                SqliteContract.MyBookings.COLUMN_COUNTRY,
                SqliteContract.MyBookings.COLUMN_DATEFROM,
                SqliteContract.MyBookings.COLUMN_DATETILL,
                SqliteContract.MyBookings.COLUMN_FIREBASEID
        };

        String sortOrder =
                SqliteContract.MyBookings.COLUMN_DATEFROM + " ASC";

        Cursor c = db.query(
                SqliteContract.MyBookings.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        c.moveToFirst();
        do {
            AvaiableGuides ag = new AvaiableGuides();
            ag.dateFrom = c.getString(c.getColumnIndex(SqliteContract.MyBookings.COLUMN_DATEFROM));
            ag.price = c.getString(c.getColumnIndex(SqliteContract.MyBookings.COLUMN_PRICE));
            ag.country = c.getString(c.getColumnIndex(SqliteContract.MyBookings.COLUMN_COUNTRY));
            ag.dateTill = c.getString(c.getColumnIndex(SqliteContract.MyBookings.COLUMN_DATETILL));
            ag.location = c.getString(c.getColumnIndex(SqliteContract.MyBookings.COLUMN_LOCATION));
            ag.maxPeople = c.getString(c.getColumnIndex(SqliteContract.MyBookings.COLUMN_MAXPOEPLE));
            ag.transport = c.getString(c.getColumnIndex(SqliteContract.MyBookings.COLUMN_TRANSPORT));

            ListUserGuides.add(ag);
            ListBookingIds.add(c.getString(c.getColumnIndex(SqliteContract.MyBookings.COLUMN_FIREBASEID)));
        } while (c.moveToNext());

        displayInList();
    }

<<<<<<< HEAD
    public void getAllBookingRequests(){

        ValueEventListener postListener2 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
=======
    public void sqliteSave(){
        if(ListUserGuides.size() != 0)
        {
            for(int i = 0; i< ListUserGuides.size(); i++)
            {
                SQLiteDatabase db = mdbHelper.getWritableDatabase();
                mdbHelper.onUpgrade(db,0, 1);
                ContentValues values = new ContentValues();

                values.put(SqliteContract.MyBookings.COLUMN_COUNTRY, ListUserGuides.get(i).country);
                values.put(SqliteContract.MyBookings.COLUMN_DATEFROM, ListUserGuides.get(i).dateFrom);
                values.put(SqliteContract.MyBookings.COLUMN_DATETILL, ListUserGuides.get(i).dateTill);
                values.put(SqliteContract.MyBookings.COLUMN_LOCATION, ListUserGuides.get(i).location);
                values.put(SqliteContract.MyBookings.COLUMN_MAXPOEPLE, ListUserGuides.get(i).maxPeople);
                values.put(SqliteContract.MyBookings.COLUMN_PRICE, ListUserGuides.get(i).price);
                values.put(SqliteContract.MyBookings.COLUMN_TRANSPORT, ListUserGuides.get(i).transport);
                values.put(SqliteContract.MyBookings.COLUMN_FIREBASEID, ListBookingIds.get(i));

                long newRowId = db.insert(SqliteContract.MyBookings.TABLE_NAME, null, values);
            }
        }

    }
>>>>>>> origin/develop

                Map<String, Object> td = (HashMap<String,Object>) dataSnapshot.getValue();

                List<Object> values = new ArrayList<>(td.values());


                for (int i=0; i<values.size(); i++){

                    Map<String, Object> ts = (HashMap<String,Object>) values.get(i);
                    List<Object> list = new ArrayList<>(ts.values());

                    UidAvailabe = list.get(3).toString();
                    if (UidAvailabe.equals(Uid)){


                        String requestId = list.get(0).toString();
                        String date = list.get(1).toString();
                        Boolean confirmed = Boolean.parseBoolean(list.get(2).toString());
                        String guideId = list.get(3).toString();
                        String availableGuideAdapterId = list.get(4).toString();

                        BookingRequests req = new BookingRequests(guideId,requestId,confirmed,date,availableGuideAdapterId);

                        ListBookingRequests.add(req);
                        ListAvailableGuidesIds.add(availableGuideAdapterId);
                        ListRequestUserIds.add(requestId);

                    }

                }

                getRequestUserDetails();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        };
        DatabaseReference myRef = database.getReference("bookingRequests");
        myRef.addListenerForSingleValueEvent(postListener2);

    }

    public void getRequestUserDetails(){

        ValueEventListener postListener3 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Map<String, Object> td = (HashMap<String,Object>) dataSnapshot.getValue();

                List<Object> values = new ArrayList<>(td.values());


                for (int p = 0; p < ListRequestUserIds.size(); p++) {

                    for (int i=0; i<values.size(); i++) {

                        Map<String, Object> ts = (HashMap<String, Object>) values.get(i);
                        List<Object> list = new ArrayList<>(ts.values());

                        List keys = new ArrayList<>(td.keySet());
                        String getKey = keys.get(i).toString();
                        UidAvailabe = getKey;

                        Boolean checkTrue = false;


                        if (ListRequestUserIds.get(p).equals(UidAvailabe)){
                            checkTrue = true;
                        }


                        if (checkTrue) {

                            String profileId = list.get(0).toString();
                            String description = list.get(1).toString();
                            String image = list.get(2).toString();
                            String birthdate = list.get(3).toString();
                            String name = list.get(4).toString();
                            Gender gender = Gender.valueOf( list.get(5).toString());
                            String phonenumber = list.get(7).toString();
                            String location = list.get(8).toString();
                            Boolean availbale = Boolean.parseBoolean(list.get(9).toString());


                            ArrayList<String> list2 = (ArrayList<String>) list.get(6);
                            ArrayList<String> language = new ArrayList<>();

                            for (int o = 0; o < list2.size(); o++) {
                                language.add(list2.get(o).toString());
                            }


                            ProfileDetails profile = new ProfileDetails(profileId, language,gender,phonenumber,birthdate,description,availbale,location,name,image);

                            ListProfileDetails.add(profile);

                        }

                }

                    getAvailableGuidesById();


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        };
        DatabaseReference myRef = database.getReference("profileDetails");
        myRef.addListenerForSingleValueEvent(postListener3);

    }

    public void getAvailableGuidesById(){

        ValueEventListener postListener4 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Map<String, Object> td = (HashMap<String,Object>) dataSnapshot.getValue();
                List Keys = new ArrayList(td.keySet());

                List<Object> values = new ArrayList<>(td.values());



                    for (int i=0; i<values.size(); i++){

                        Map<String, Object> ts = (HashMap<String,Object>) values.get(i);
                        List<Object> list = new ArrayList<>(ts.values());

                        String currentKey = Keys.get(i).toString();

                        Boolean checkTrue = false;

                        for (int q = 0; q < ListAvailableGuidesIds.size(); q++) {
                            if (ListAvailableGuidesIds.get(q).equals(currentKey)) {
                                checkTrue = true;
                            }
                        }

                            if (checkTrue) {

                                String name = list.get(8).toString();
                                String country = list.get(2).toString();
                                String location = list.get(4).toString();
                                String dateFrom = list.get(7).toString();
                                String dateTill = list.get(1).toString();
                                String maxPeople = list.get(6).toString();
                                String price = list.get(0).toString();
                                String transport = list.get(10).toString();
                                String userId = list.get(5).toString();
                                String photoUri = list.get(3).toString();

                                ArrayList<String> list2 = (ArrayList<String>) list.get(9);
                                ArrayList<String> type = new ArrayList<>();

                                for (int o = 0; o < list2.size(); o++) {
                                    type.add(list2.get(o).toString());
                                }

                                AvaiableGuides guide = new AvaiableGuides(name, country, location, dateFrom, dateTill, maxPeople, price, type, transport, userId, photoUri);

                                ListUserGuides2.add(guide);

                            }


                }

                displayInList("second");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        };
        DatabaseReference myRef = database.getReference("avaiableGuides");
        myRef.addListenerForSingleValueEvent(postListener4);

    }




    public void displayInList(String position){

        progress.dismiss();

        if(position.equals("first")){

            textViewAvailable.setVisibility(View.VISIBLE);

            mAdapter = new AvailabeGuidesAdapter(ListUserGuides, ListBookingIds, thisActivity);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);

            ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                @Override
                public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                    TextView date = (TextView) v.findViewById(R.id.textViewTime);


                    Toast.makeText(getActivity(), date.getText().toString(),
                            Toast.LENGTH_LONG).show();

                }
            });
        }

        if(position.equals("second")){

            textViewBookings.setVisibility(View.VISIBLE);

            mBAdapter = new bookings_as_guide_adapter(ListBookingRequests, ListProfileDetails, ListUserGuides2 , thisActivity);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            recyclerViewBookings.setLayoutManager(mLayoutManager);
            recyclerViewBookings.setItemAnimator(new DefaultItemAnimator());
            recyclerViewBookings.setAdapter(mBAdapter);

        }







    }


}
