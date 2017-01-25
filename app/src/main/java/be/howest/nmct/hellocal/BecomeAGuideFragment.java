package be.howest.nmct.hellocal;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import be.howest.nmct.hellocal.adapters.PlacesAutoCompleteAdapter;
import be.howest.nmct.hellocal.models.AvaiableGuides;
import be.howest.nmct.hellocal.models.ProfileDetails;


/**
 * A simple {@link Fragment} subclass.
 */
public class BecomeAGuideFragment extends Fragment implements View.OnClickListener {


    private DatabaseReference mDatabaseReference;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    //    private EditText EditTextLocation;
    private Spinner SpinnerCountry;
    private EditText EditTextFrom;
    private EditText EditTextTill;
    private Spinner spinnerPeople;
    private Spinner spinnerTransport;
    private CheckBox chkActive;
    private CheckBox chkCity;
    private CheckBox chkCulture;
    private CheckBox chkElse;
    private EditText EditTextPrice;
    private Button btnSave;

    private AutoCompleteTextView EditTextLocation;

    private String Location;

    String stringUserId, mHomeTown;


    Calendar myCalendar = Calendar.getInstance();

    private GoogleApiClient mGoogleApiClient;


    private PlacesAutoCompleteAdapter mPlacesAdapter;
    private AutoCompleteTextView myLocation;

    FirebaseUser mUser;


    public BecomeAGuideFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_become_aguide, container, false);

//        EditTextLocation = (EditText) v.findViewById(R.id.EditTextLocation);
        SpinnerCountry = (Spinner) v.findViewById(R.id.SpinnerCountry);
        EditTextFrom = (EditText) v.findViewById(R.id.EditTextFrom);
        EditTextTill = (EditText) v.findViewById(R.id.EditTextTill);
        spinnerPeople = (Spinner) v.findViewById(R.id.spinnerPeople);
        spinnerTransport = (Spinner) v.findViewById(R.id.spinnerTransport);
        chkActive = (CheckBox) v.findViewById(R.id.chkActive);
        chkCity = (CheckBox) v.findViewById(R.id.chkCity);
        chkCulture = (CheckBox) v.findViewById(R.id.chkCulture);
        chkElse = (CheckBox) v.findViewById(R.id.chkElse);
        EditTextPrice = (EditText) v.findViewById(R.id.EditTextPrice);
        btnSave = (Button) v.findViewById(R.id.btnSave);
        EditTextLocation = (AutoCompleteTextView) v.findViewById(R.id.myLocation2);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        btnSave.setOnClickListener(this);

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {


            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {


                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }


        };


        final DatePickerDialog.OnDateSetListener date2 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel2();
            }

        };

        EditTextFrom.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        EditTextTill.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getContext(), date2, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        mGoogleApiClient = new GoogleApiClient
                .Builder(getContext())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();


        myLocation = (AutoCompleteTextView) v.findViewById(R.id.myLocation2);
        mPlacesAdapter = new PlacesAutoCompleteAdapter(getContext(), android.R.layout.simple_list_item_1,
                mGoogleApiClient, null, null);
        myLocation.setOnItemClickListener(mAutocompleteClickListener);
        myLocation.setAdapter(mPlacesAdapter);


        showHomeTown();


        return v;
    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final PlacesAutoCompleteAdapter.PlaceAutocomplete item = mPlacesAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e("place", "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);
            Location = place.getName().toString();
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();


        mGoogleApiClient.disconnect();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.disconnect();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mGoogleApiClient.disconnect();
    }


    public void showHomeTown() {

        mUser = FirebaseAuth.getInstance().getCurrentUser();

        if (mUser != null) {

            stringUserId = mUser.getUid();

            ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    ProfileDetails profileDetails = dataSnapshot.getValue(ProfileDetails.class);

                    if (profileDetails != null) {

                        mHomeTown = "";

                        mHomeTown = profileDetails.getHomeTown();
                        myLocation.setText(mHomeTown);

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };

            DatabaseReference myRef = database.getReference("profileDetails").child(stringUserId);
            myRef.addListenerForSingleValueEvent(postListener);
        }

    }

    private ArrayList<String> getSelectedTypes() {
        ArrayList<String> types = new ArrayList<>();

        if (chkActive.isChecked()) types.add(("Active"));
        if (chkCity.isChecked()) types.add("City");
        if (chkCulture.isChecked()) types.add("Culture");
        if (chkElse.isChecked()) types.add("Else");

        return types;
    }

    private void showToast(String messsage) {
        Toast.makeText(getContext(), messsage, Toast.LENGTH_SHORT).show();
    }

    private Boolean checkForm() {
        if (isEmpty(EditTextLocation)) {
            showToast("Please enter a valid location!");
            return true;
        }
        if (isEmpty(EditTextPrice)) {
            showToast("Please enter a valid Price!");
            return true;
        }
        if (isEmpty(EditTextFrom)) {
            showToast("Please enter a valid start date!");
            return true;
        }
        if (isEmpty(EditTextTill)) {
            showToast("Please enter a valid end date!");
            return true;
        }


        return false;
    }


    private void SaveBooking() {
        if (checkForm()) return;

        ArrayList<String> types = getSelectedTypes();
        if (types.size() == 0) {
            showToast("Please select one type");
            return;
        }

        if (mUser.getPhotoUrl() == null) {
            showToast("Please add a profile picture to your account!");
            return;
        }
        if (mUser.getDisplayName() == null || mUser.getDisplayName() == "") {
            showToast("Please add a new nickname to your account!");
            return;
        }

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ProfileDetails profileDetails = dataSnapshot.getValue(ProfileDetails.class);
                if (profileDetails == null) {
                    showToast("Please update your profile!");
                    return;
                }
                if (profileDetails.getLanguage().size() == 0) {
                    showToast("Please add a language to your account!");
                    return;
                }
                if (profileDetails.getDescription() == null || profileDetails.getDescription() == "") {
                    showToast("Please add a description to your account!");
                    return;
                }

                newBooking(mUser.getDisplayName(), SpinnerCountry.getSelectedItem().toString(), EditTextLocation.getText().toString(), EditTextFrom.getText().toString(),
                        EditTextTill.getText().toString().trim(), spinnerPeople.getSelectedItem().toString(), EditTextPrice.getText().toString().trim(),
                        getSelectedTypes(), spinnerTransport.getSelectedItem().toString(), mUser.getUid(), mUser.getPhotoUrl().toString());

                showToast("The booking is added");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                showToast("Error finding your profile details, restart the app");
            }
        };
        DatabaseReference myRef = database.getReference("profileDetails").child(mUser.getUid());
        myRef.addListenerForSingleValueEvent(postListener);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSave:
                SaveBooking();
        }
    }

    private void newBooking(String name, String country, String location, String dateFrom, String dateTill, String maxPeople, String price, ArrayList<String> type, String transport, String userId, String photoUri) {
        AvaiableGuides guide = new AvaiableGuides(name, country, location, dateFrom, dateTill, maxPeople, price, type, transport, userId, photoUri, true);
        mDatabaseReference.child("avaiableGuides").push().setValue(guide);
    }

    //check if edittext is empty
    private boolean isEmpty(EditText textInputEditText) {
        if (textInputEditText.getText().toString().trim().length() > 0)
            return false;
        return true;
    }

    private void updateLabel() {

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);

        EditTextFrom.setText(sdf.format(myCalendar.getTime()));

    }

    private void updateLabel2() {

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);

        EditTextTill.setText(sdf.format(myCalendar.getTime()));

    }


}
