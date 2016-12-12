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
import be.howest.nmct.hellocal.contracts.SqliteContract;
import be.howest.nmct.hellocal.helpers.SqliteHelper;
import be.howest.nmct.hellocal.models.AvaiableGuides;


public class BookingsFragment extends Fragment {

    FirebaseUser mUser;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    SqliteHelper mdbHelper;
    Boolean mBooleanIsOnline;

    String Uid;
    String UidAvailabe;

    private List<AvaiableGuides> ListUserGuides = new ArrayList<>();
    private List<String> ListBookingIds = new ArrayList<>();
    private RecyclerView recyclerView;
    private AvailabeGuidesAdapter mAdapter;

    private ProgressDialog progress;

    private TextView textViewNoBookings;
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

        textViewNoBookings = (TextView) v.findViewById(R.id.noGuides);
        textViewAvailable = (TextView) v.findViewById(R.id.availableBookings);

        textViewDate = (TextView) v.findViewById(R.id.textViewDate);

        btnRemove = (Button) v.findViewById(R.id.btnClose);



        textViewNoBookings.setVisibility(View.GONE);
        textViewAvailable.setVisibility(View.GONE);


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
                        String gfsg = "";

                    }

                }
                displayInList();
                sqliteSave();

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

    public void displayInList(){


        progress.dismiss();
        textViewAvailable.setVisibility(View.VISIBLE);

        mAdapter = new AvailabeGuidesAdapter(ListUserGuides, ListBookingIds, thisActivity);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
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

//        btnRemove.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                removeBooking(textViewDate.getTag(R.id.bookingId).toString());
//            }
//        });


    }

//    public void removeBooking(final String BookingId){
//
//
//        DatabaseReference myRef = database.getReference("avaiableGuides");
//
//        myRef.getParent().child(BookingId).removeValue();
//
//    }

}
