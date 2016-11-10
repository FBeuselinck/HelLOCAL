package be.howest.nmct.hellocal;


import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;


import be.howest.nmct.hellocal.models.AvaiableGuides;
import be.howest.nmct.hellocal.R;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class BecomeAGuideFragment extends Fragment implements View.OnClickListener  {


    private DatabaseReference mDatabaseReference;
    private EditText EditTextLocation;
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


    public BecomeAGuideFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_become_aguide, container, false);

        EditTextLocation = (EditText) v.findViewById(R.id.EditTextLocation);
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

        return v;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnSave:
                if(!isEmpty(EditTextLocation) && !isEmpty(EditTextFrom) && !isEmpty(EditTextTill)){

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {

                        String uid = user.getUid();
                        String Naam = user.getDisplayName();
                        newBooking(Naam, SpinnerCountry.getSelectedItem().toString(), EditTextLocation.getText().toString().trim(),EditTextFrom.getText().toString(),
                                EditTextTill.getText().toString().trim(),spinnerPeople.getSelectedItem().toString(),EditTextPrice.getText().toString().trim(),
                                "Active",spinnerTransport.getSelectedItem().toString(),"Dutch",uid);

                    }


                }else{
                    if(isEmpty(EditTextLocation)){
                        Toast.makeText(getContext(), "Please enter a valid location!", Toast.LENGTH_SHORT).show();
                    }else if(isEmpty(EditTextFrom)){
                        Toast.makeText(getContext(), "Please enter a valid startdate", Toast.LENGTH_SHORT).show();
                    }else if(isEmpty(EditTextTill)){
                        Toast.makeText(getContext(), "Please enter a valid endDate", Toast.LENGTH_SHORT).show();
                    }
                }
                //to remove current fragment
               Toast.makeText(getContext(),"The booking is added!", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void newBooking( String name,String country, String location, String dateFrom, String dateTill, String maxPeople,String price, String type, String transport, String language, String userId) {
        //Creating a movie object with user defined variables
        AvaiableGuides guide = new AvaiableGuides(name, country ,location,dateFrom,dateTill,maxPeople,price,type,transport,language,userId);
        //referring to movies node and setting the values from movie object to that location
        mDatabaseReference.child("avaiableGuides").push().setValue(guide);
    }

    //check if edittext is empty
    private boolean isEmpty(EditText textInputEditText) {
        if (textInputEditText.getText().toString().trim().length() > 0)
            return false;
        return true;
    }
}
