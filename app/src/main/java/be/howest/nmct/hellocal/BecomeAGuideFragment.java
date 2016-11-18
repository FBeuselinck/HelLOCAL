package be.howest.nmct.hellocal;


import android.app.DatePickerDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;


import be.howest.nmct.hellocal.models.AvaiableGuides;
import be.howest.nmct.hellocal.R;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class BecomeAGuideFragment extends Fragment implements View.OnClickListener  {


    private DatabaseReference mDatabaseReference;
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

    private String Location;

    Calendar myCalendar = Calendar.getInstance();

    private GoogleApiClient mGoogleApiClient;


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



        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment2);

        autocompleteFragment.setHint("City");


        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Location= place.getName().toString();
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("test 2.0", "An error occurred: " + status);
            }
        });

        return v;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnSave:
                if((!Location.isEmpty()) && !isEmpty(EditTextFrom) && !isEmpty(EditTextTill)&& !isEmpty(EditTextPrice)){

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {

                        String uid = user.getUid();
                        String Naam = user.getDisplayName();

                        ArrayList<String> type = new ArrayList<String>();
                        if(chkActive.isChecked()){
                            type.add("Active");
                        }
                        if(chkCity.isChecked()){
                            type.add("City");
                        }
                        if(chkCulture.isChecked()){
                            type.add("Culture");
                        }
                        if(chkElse.isChecked()){
                            type.add("Else");
                        }


//                        String Combined = SpinnerCountry.getSelectedItem().toString() + "_"
//                                + EditTextLocation.getText().toString().trim() + "_"
//                                + EditTextFrom.getText().toString().trim() + "_"
//                                + EditTextTill.getText().toString().trim() + "_"
//                                + spinnerPeople.getSelectedItem().toString() + "_"
//                                + type.toString() + "_"
//                                + spinnerTransport.getSelectedItem().toString() + "_"
//                                + "Dutch" + "_"
//                                + EditTextPrice.getText().toString().trim();


                        String photoUri = user.getPhotoUrl().toString();

                        if(!photoUri.isEmpty()){

                            newBooking(Naam, SpinnerCountry.getSelectedItem().toString(), Location,EditTextFrom.getText().toString(),
                                    EditTextTill.getText().toString().trim(),spinnerPeople.getSelectedItem().toString(),EditTextPrice.getText().toString().trim(),
                                    type,spinnerTransport.getSelectedItem().toString(),"Dutch",uid,photoUri);

                            Toast.makeText(getContext(),"The booking is added!", Toast.LENGTH_SHORT).show();
                            break;

                        }else{
                            Toast.makeText(getContext(),"Please update your profile first!", Toast.LENGTH_SHORT).show();
                            break;
                        }


                    }


                }else{
                    if(Location.isEmpty()){
                        Toast.makeText(getContext(), "Please enter a valid location!", Toast.LENGTH_SHORT).show();
                    }else if(isEmpty(EditTextFrom)){
                        Toast.makeText(getContext(), "Please enter a valid startdate", Toast.LENGTH_SHORT).show();
                    }else if(isEmpty(EditTextTill)){
                        Toast.makeText(getContext(), "Please enter a valid endDate", Toast.LENGTH_SHORT).show();
                    }
                }
                //to remove current fragment

        }
    }

    private void newBooking(String name, String country, String location, String dateFrom, String dateTill, String maxPeople, String price, ArrayList<String> type, String transport, String language, String userId, String photoUri) {
        //Creating a movie object with user defined variables
        AvaiableGuides guide = new AvaiableGuides(name, country ,location,dateFrom,dateTill,maxPeople,price,type,transport,language,userId, photoUri);
        //referring to movies node and setting the values from movie object to that location
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
