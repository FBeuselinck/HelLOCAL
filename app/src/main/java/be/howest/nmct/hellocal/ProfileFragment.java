package be.howest.nmct.hellocal;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

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


    private ProfileDetails mProfileDetails;
    private List<String> LanguagesIds;
    Boolean mBooleanLanguageChanged;

    private GoogleApiClient mGoogleApiClient;
    private PlacesAutoCompleteAdapter mPlacesAdapter;

    private AutoCompleteTextView homeTown;


    FirebaseUser mUser;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    Calendar myCalendar = Calendar.getInstance();

    private final String SHAREDPREFERENCE = "profilesave";


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

        mProfileDetails = new ProfileDetails();
        mUser = FirebaseAuth.getInstance().getCurrentUser();


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


        //authState listener
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mUser = firebaseAuth.getCurrentUser();
                if (mUser != null) {
                    // User is signed in
                    showDetails();
                    Log.d("ArnoDev", "AuthStateChanged");
                } else {
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        };



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
            mProfileDetails.setHomeTown(place.getName().toString());
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
        mAuth.removeAuthStateListener(mAuthListener);
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
        mProfileDetails.setLanguage(strings);
        mBooleanLanguageChanged = true;
        saveDetails();
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        editTextBirthDate.setText(sdf.format(myCalendar.getTime()));
        saveDetails();
    }

    private void getDataFromSharedPreference(){
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);

        mProfileDetails.setAvailable(sharedPref.getBoolean("availible", false));
        mProfileDetails.setDescription(sharedPref.getString("description", ""));
        mProfileDetails.setHomeTown(sharedPref.getString("hometown", ""));
        mProfileDetails.setBirthDate(sharedPref.getString("birthdate", ""));
        mProfileDetails.setName(sharedPref.getString("name",""));
        mProfileDetails.setPhoneNumber(sharedPref.getString("phonenumber",""));
        mProfileDetails.setPhotoUri(sharedPref.getString("photouri",""));
        mProfileDetails.setProfileId(sharedPref.getString("id",""));
        Gender g = Gender.Undefined;
        String stringgender = sharedPref.getString("gender", "");
        if (stringgender.equals("Male")) g = Gender.Male;
        if(stringgender.equals("Female")) g= Gender.Female;
        mProfileDetails.setGender(g);

        if(sharedPref.getStringSet("languages", null) !=null){
            List<String> list = Arrays.asList(
                    sharedPref.getStringSet("languages",null).toArray( new String[0] ) );
            mProfileDetails.setLanguage(list);
        }


        showFromData();
    }

    private void showFromData()
    {
        PopulateLanguages();
        tglAvailable.setChecked(mProfileDetails.getAvailable());
        editTextPhoneNumber.setText(mProfileDetails.getPhoneNumber());
        editTextBirthDate.setText(mProfileDetails.getBirthDate());
        homeTown.setText(mProfileDetails.getHomeTown());
        if(mProfileDetails.getBirthDate() != null && !mProfileDetails.getBirthDate().equals("")) {
            myCalendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(mProfileDetails.getBirthDate().substring(0,2)));
            myCalendar.set(Calendar.MONTH, Integer.parseInt(mProfileDetails.getBirthDate().substring(3,5)) -1);
            myCalendar.set(Calendar.YEAR, Integer.parseInt(mProfileDetails.getBirthDate().substring(6,10)));
        }
        editTextDescription.setText(mProfileDetails.getDescription());

        if(mProfileDetails.getGender() == Gender.Male) spinnerGender.setSelection(0);
        else if(mProfileDetails.getGender() == Gender.Female) spinnerGender.setSelection(1);
        else  if(mProfileDetails.getGender() == Gender.Undefined) spinnerGender.setSelection(2);

    }

    public void showDetails()
    {
        getDataFromSharedPreference();

        if(mUser != null) {

            String name = mUser.getDisplayName();
            String email = mUser.getEmail();
            Uri photoUrl = mUser.getPhotoUrl();
            mProfileDetails.setProfileId(mUser.getUid());


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

                        mProfileDetails = profileDetails;

                        if(mProfileDetails.getGender() == null) mProfileDetails.setGender(Gender.Undefined);

                        if(mProfileDetails.getAvailable() == null) {
                            tglAvailable.setChecked(false);
                            mProfileDetails.setAvailable(false);
                        }
                        saveDataToSharedPreference();
                        showFromData();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };

            DatabaseReference myRef = database.getReference("profileDetails").child(mUser.getUid());
            myRef.addListenerForSingleValueEvent(postListener);
        }
    }


    private void saveDataToSharedPreference(){
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putBoolean("availible", mProfileDetails.getAvailable());
        editor.putString("birthdate", mProfileDetails.getBirthDate());
        editor.putString("description", mProfileDetails.getDescription());
        editor.putString("gender", mProfileDetails.getGender().toString());
        editor.putString("hometown", mProfileDetails.getHomeTown());

        Set<String> foo = new HashSet<String>(mProfileDetails.getLanguage());
        editor.putStringSet("languages", (foo));
        editor.putString("name", mUser.getDisplayName());
        editor.putString("phonenumber", mProfileDetails.getPhoneNumber());
        if(mUser.getPhotoUrl()!= null) editor.putString("photouri", mUser.getPhotoUrl().toString());
        editor.putString("id", mUser.getUid());

        editor.apply();
    }

    private void PopulateLanguages(){
        LanguagesIds = new ArrayList<>();
        if(mProfileDetails.getLanguage() != null) LanguagesIds = mProfileDetails.getLanguage();
        String[] array = {"English", "Dutch","French", "German", "Spanish", "Portuguese", "Russian"};
        multiSelectionSpinner.setItems(array);
        if(LanguagesIds.size() != 0) multiSelectionSpinner.setSelection(LanguagesIds);
        multiSelectionSpinner.setListener(this);
        mProfileDetails.setLanguage(multiSelectionSpinner.getSelectedStrings());
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
        if(selectedGender != mProfileDetails.getGender()) booleanChangeFound = true;

        if(!name.equals(mUser.getDisplayName())) booleanChangeFound = true;
        if(!birthDate.equals(mProfileDetails.getBirthDate())) booleanChangeFound = true;
        if(!phoneNumber.equals(mProfileDetails.getPhoneNumber())) booleanChangeFound = true;
        if(description.equals(mProfileDetails.getDescription())) booleanChangeFound = true;
        if(available == mProfileDetails.getAvailable()) booleanChangeFound = true;




        if(booleanChangeFound || mBooleanLanguageChanged)
        {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build();


            mUser.updateProfile(profileUpdates);
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            String Photouri = "";
            if(mUser.getPhotoUrl() != null)  Photouri = mUser.getPhotoUrl().toString();
            ProfileDetails profileDetails = new ProfileDetails(mUser.getUid(), mProfileDetails.getLanguage(), selectedGender, phoneNumber, birthDate, description, available, mProfileDetails.getHomeTown(), name, Photouri);
            mDatabase.child("profileDetails").child(profileDetails.getProfileId()).setValue(profileDetails);
            saveDataToSharedPreference();
            Toast.makeText(getContext(), "Changes made", Toast.LENGTH_SHORT).show();


        }
        else Toast.makeText(getContext(), "You need to change something", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        editTextBirthDate.setText(dayOfMonth + "/" + monthOfYear + "/" + year);
    }

    public void setImageViewProfilePic(String path){
        Picasso.with(getContext()).load(new File(path)).into(imageViewProfilePic);
    }

}
