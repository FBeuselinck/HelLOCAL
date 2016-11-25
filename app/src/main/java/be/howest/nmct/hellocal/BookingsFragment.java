package be.howest.nmct.hellocal;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import be.howest.nmct.hellocal.adapters.AvailabeGuidesAdapter;
import be.howest.nmct.hellocal.models.AvaiableGuides;


/**
 * A simple {@link Fragment} subclass.
 */
public class BookingsFragment extends Fragment {

    FirebaseUser mUser;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    String Uid;
    String UidAvailabe;

    private List<AvaiableGuides> ListUserGuides = new ArrayList<>();
    private RecyclerView recyclerView;
    private AvailabeGuidesAdapter mAdapter;

    private ProgressDialog progress;

    private TextView textViewNoBookings;
    private TextView textViewAvailable;


    public BookingsFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_bookings, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerViewBookings);

        textViewNoBookings = (TextView) v.findViewById(R.id.noGuides);
        textViewAvailable = (TextView) v.findViewById(R.id.availableBookings);

        textViewNoBookings.setVisibility(View.GONE);
        textViewAvailable.setVisibility(View.GONE);


        progress = new ProgressDialog(getActivity());
        progress.setTitle("Loading bookings");
        progress.setMessage("Hold on, we are finding your bookings!");
        progress.setCancelable(false);
        progress.show();

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


                        // TODO -> get rid of static types

                        // Code hieronder marcheert niet

//                        Map<String, Object> tq = (HashMap<String,Object>) list.get(9);
//                        List<Object> list2 = new ArrayList<>(tq.values());
//
                        ArrayList<String> type = new ArrayList<>();
//                        for (int o = 0; o<list2.size();o++){
//                            type.add(list2.get(o).toString());
//                        }




                        type.add("Active");
                        type.add("Culture");

                        AvaiableGuides guide = new AvaiableGuides(name,country,location,dateFrom,dateTill,maxPeople,price,type,transport,userId,photoUri);

                        ListUserGuides.add(guide);

                    }

                }


                displayInList();



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        };


        DatabaseReference myRef = database.getReference("avaiableGuides");
        myRef.addListenerForSingleValueEvent(postListener);

    }



    public void displayInList(){


        progress.dismiss();
        textViewAvailable.setVisibility(View.VISIBLE);

        mAdapter = new AvailabeGuidesAdapter(ListUserGuides);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


    }

}
