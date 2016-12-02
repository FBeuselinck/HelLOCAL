package be.howest.nmct.hellocal;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import be.howest.nmct.hellocal.adapters.PlacesAutoCompleteAdapter;
import be.howest.nmct.hellocal.models.Gender;
import be.howest.nmct.hellocal.models.ProfileDetails;
import be.howest.nmct.hellocal.multiSelectionSpinner.MultiSelectionSpinner;

public class ProfileFragment extends Fragment implements MultiSelectionSpinner.OnMultipleItemsSelectedListener, DatePickerDialog.OnDateSetListener {


    //region Variables
    EditText editTextMail, editTextName, editTextPhoneNumber, editTextBirthDate, editTextDescription;
    Button buttonSave;
    ImageView imageViewProfilePic;
    Spinner spinnerLanguages, spinnerGender;
    private  MultiSelectionSpinner multiSelectionSpinner;
    ToggleButton tglAvailable;

    String stringUserId, mPhoneNumber, mBirthDate, mDescription, mHomeTown, HomeTown = "";
    private List<String> Languages;
    private List<String> LanguagesIds;
    Boolean blAvailable;
    Boolean mBooleanLanguageChanged;
    Gender mGender;

    private GoogleApiClient mGoogleApiClient;
    private PlacesAutoCompleteAdapter mPlacesAdapter;

    private AutoCompleteTextView homeTown;


    FirebaseUser mUser;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    Calendar myCalendar = Calendar.getInstance();
    //endregion


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        //region LayoutControls
        editTextMail = (EditText) view.findViewById(R.id.editText_Profile_mail);
        editTextName = (EditText) view.findViewById(R.id.editText_Profile_Name);
        editTextPhoneNumber = (EditText) view.findViewById(R.id.EditText_Profile_PhoneNumber);
        editTextBirthDate = (EditText) view.findViewById(R.id.editText_Profile_BirthDate);
        editTextDescription = (EditText) view.findViewById(R.id.editText_Profile_Description) ;
        spinnerGender = (Spinner) view.findViewById(R.id.spinnerGender);
        imageViewProfilePic = (ImageView) view.findViewById(R.id.image_profile);
        buttonSave = (Button) view.findViewById(R.id.btnSave);
        multiSelectionSpinner = (MultiSelectionSpinner) view.findViewById(R.id.spinnerLanguage);
        tglAvailable = (ToggleButton) view.findViewById(R.id.tglAvailable);
        //endregion


        tglAvailable.setTextOff("No");
        tglAvailable.setTextOn("Yes");


        mGoogleApiClient = new GoogleApiClient
                .Builder(getContext())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();


        homeTown = (AutoCompleteTextView) view.findViewById(R.id.homeTown);
        mPlacesAdapter = new PlacesAutoCompleteAdapter(getContext(), android.R.layout.simple_list_item_1,
                mGoogleApiClient,null, null);
        homeTown.setOnItemClickListener(mAutocompleteClickListener);
        homeTown.setAdapter(mPlacesAdapter);

        showDetails();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };


        editTextBirthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDetails();
            }
        });
        imageViewProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadProfilePic();
            }
        });


        return view;
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
            HomeTown = place.getName().toString();
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
    public void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.disconnect();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void selectedIndices(List<Integer> indices) {

    }

    @Override
    public void selectedStrings(List<String> strings) {
        Languages = strings;
        mBooleanLanguageChanged = true;
        saveDetails();
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        editTextBirthDate.setText(sdf.format(myCalendar.getTime()));
        saveDetails();
    }

    public void showDetails()
    {
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        if(mUser != null) {

            String name = mUser.getDisplayName();
            String email = mUser.getEmail();
            Uri photoUrl = mUser.getPhotoUrl();
            stringUserId = mUser.getUid();


            editTextMail.setText(email);

            if(name != null)editTextName.setText(name);
            if(photoUrl != null) Picasso.with(this.getContext()).load(photoUrl.toString()).into(imageViewProfilePic);


            ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    ProfileDetails profileDetails = dataSnapshot.getValue(ProfileDetails.class);

                    if(profileDetails != null){

                        List<String> UserLanguage;
                        List<String> DefaultLang = new ArrayList<>();
                        DefaultLang.add("English");
                        mPhoneNumber = "";
                        mBirthDate = "";
                        mDescription = "";
                        mGender = Gender.Undefined;
                        mHomeTown = "";

                        UserLanguage = profileDetails.getLanguage();
                        mGender = profileDetails.getGender();
                        mPhoneNumber = profileDetails.getPhoneNumber();
                        mBirthDate = profileDetails.getBirthDate();
                        mDescription = profileDetails.getDescription();
                        blAvailable = profileDetails.getAvailable();
                        mHomeTown = profileDetails.getHomeTown();

                        LanguagesIds = new ArrayList<>();

                        if(UserLanguage != null) if(UserLanguage.size() != 0) LanguagesIds = UserLanguage;
                        PopulateLanguages();

                        if(blAvailable != null) tglAvailable.setChecked(blAvailable);
                        else {
                            tglAvailable.setChecked(true);
                            blAvailable = false;
                        }

                        if(mGender == Gender.Male) spinnerGender.setSelection(0);
                        else if(mGender == Gender.Female) spinnerGender.setSelection(1);
                        else  if(mGender == Gender.Undefined) spinnerGender.setSelection(2);

                        editTextPhoneNumber.setText(mPhoneNumber);
                        editTextBirthDate.setText(mBirthDate);

                        homeTown.setText(mHomeTown);

                        if(mBirthDate != null && !mBirthDate.equals("")) {
                            myCalendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(mBirthDate.substring(0,2)));
                            myCalendar.set(Calendar.MONTH, Integer.parseInt(mBirthDate.substring(3,5)) -1);
                            myCalendar.set(Calendar.YEAR, Integer.parseInt(mBirthDate.substring(6,10)));
                        }
                        editTextDescription.setText(mDescription);
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


    private void PopulateLanguages(){
        String[] array = {"English", "Dutch","French", "German", "Spanish", "Portuguese", "Russian"};
        multiSelectionSpinner.setItems(array);
        if(LanguagesIds.size() != 0) multiSelectionSpinner.setSelection(LanguagesIds);
        multiSelectionSpinner.setListener(this);
        Languages = multiSelectionSpinner.getSelectedStrings();
    }

    private void UploadProfilePic()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Chose your profile picture"),1);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mGoogleApiClient.disconnect();

    }

    private void saveDetails()
    {
        Boolean booleanChangeFound = false;
        String name = editTextName.getText().toString();
        String birthDate = editTextBirthDate.getText().toString();
        Gender selectedGender = Gender.Undefined;
        String phoneNumber = editTextPhoneNumber.getText().toString();
        String description = editTextDescription.getText().toString();
        boolean available = tglAvailable.isChecked();

        if(spinnerGender.getSelectedItemPosition() == 0) selectedGender = Gender.Male;
        if(spinnerGender.getSelectedItemPosition() == 1) selectedGender = Gender.Female;
        if(selectedGender != mGender) booleanChangeFound = true;

        if(!name.equals(mUser.getDisplayName())) booleanChangeFound = true;
        if(!birthDate.equals(mBirthDate)) booleanChangeFound = true;
        if(!phoneNumber.equals(mPhoneNumber)) booleanChangeFound = true;
        if(description.equals(mDescription)) booleanChangeFound = true;
        if(available == blAvailable) booleanChangeFound = true;




        if(booleanChangeFound || mBooleanLanguageChanged)
        {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build();


            mUser.updateProfile(profileUpdates);
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            ProfileDetails profileDetails = new ProfileDetails(mUser.getUid(), Languages, selectedGender, phoneNumber, birthDate, description, available, HomeTown, name, mUser.getPhotoUrl().toString());
            mDatabase.child("profileDetails").child(profileDetails.getProfileId()).setValue(profileDetails);
            Toast.makeText(getContext(), "Changes made", Toast.LENGTH_SHORT).show();
        }
        else Toast.makeText(getContext(), "You need to change something", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        editTextBirthDate.setText(dayOfMonth + "/" + monthOfYear + "/" + year);
    }
}
