package be.howest.nmct.hellocal;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

<<<<<<< HEAD
import java.util.ArrayList;
import java.util.List;
=======
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
>>>>>>> origin/develop

import be.howest.nmct.hellocal.models.Gender;
import be.howest.nmct.hellocal.models.Language;
import be.howest.nmct.hellocal.models.ProfileDetails;
import be.howest.nmct.hellocal.multiSelectionSpinner.MultiSelectionSpinner;


/**
 * A simple {@link Fragment} subclass.
 */
<<<<<<< HEAD
public class ProfileFragment extends Fragment implements MultiSelectionSpinner.OnMultipleItemsSelectedListener {
=======
public class ProfileFragment extends Fragment implements DatePickerDialog.OnDateSetListener {
>>>>>>> origin/develop

    EditText editTextMail, editTextName, editTextPhoneNumber, editTextBirthDate, editTextDescription;
    Button buttonSave;
    ImageView imageViewProfilePic;
    Spinner spinnerLanguages, spinnerGender;
    String stringChangeName = "Change full name here";
    String stringUserId, mPhoneNumber, mBirthDate, mDescription;
    FirebaseUser mUser;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    int intSpinnerLanguageStartValue, intSpinnerGenderStartValue;
    Calendar myCalendar = Calendar.getInstance();
    static final int DATE_DIALOG_ID = 999;

    private List<String> Languages;

    private List<String> LanguagesIds;



    private  MultiSelectionSpinner multiSelectionSpinner;



    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        editTextMail = (EditText) view.findViewById(R.id.editText_Profile_mail);
        editTextName = (EditText) view.findViewById(R.id.editText_Profile_Name);
        editTextPhoneNumber = (EditText) view.findViewById(R.id.EditText_Profile_PhoneNumber);
        editTextBirthDate = (EditText) view.findViewById(R.id.editText_Profile_BirthDate);
        editTextDescription = (EditText) view.findViewById(R.id.editText_Profile_Description) ;

        spinnerLanguages = (Spinner) view.findViewById(R.id.spinnerLanguage);
        spinnerGender = (Spinner) view.findViewById(R.id.spinnerGender);

        imageViewProfilePic = (ImageView) view.findViewById(R.id.image_profile);

        buttonSave = (Button) view.findViewById(R.id.btnSave);

        mUser = FirebaseAuth.getInstance().getCurrentUser();

        multiSelectionSpinner = (MultiSelectionSpinner) view.findViewById(R.id.spinnerLanguage);


        showDetails();


<<<<<<< HEAD




=======
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
>>>>>>> origin/develop

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


<<<<<<< HEAD

=======
>>>>>>> origin/develop
        // Inflate the layout for this fragment
        return view;
    }

<<<<<<< HEAD
    @Override
    public void selectedIndices(List<Integer> indices) {

    }

    @Override
    public void selectedStrings(List<String> strings) {

        Languages = strings;
=======
    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        editTextBirthDate.setText(sdf.format(myCalendar.getTime()));
        saveDetails();
>>>>>>> origin/develop
    }

    public void showDetails()
    {
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        if(mUser != null)
        {
            String name = mUser.getDisplayName();
            String email = mUser.getEmail();
            Uri photoUrl = mUser.getPhotoUrl();
            stringUserId = mUser.getUid();

            editTextMail.setText(email);
            if(name != null)
            {
                editTextName.setText(name);
            }
            else {
                editTextName.setText("");
                editTextName.setHint(stringChangeName);
            }
            if(photoUrl != null)
            {
                Picasso.with(this.getContext()).load(photoUrl.toString()).into(imageViewProfilePic);
            }

            ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Get Post object and use the values to update the UI

                    ProfileDetails profileDetails = dataSnapshot.getValue(ProfileDetails.class);

                    List<String> UserLanguage = null;
                    List<String> DefaultLang = new ArrayList<String>();
                    DefaultLang.add("English");
                    Gender gender = null;
                    mPhoneNumber = "";
                    mBirthDate = "";
                    mDescription = "";

                    if(profileDetails != null){
                        UserLanguage = profileDetails.getLanguage();
                        gender = profileDetails.getGender();
                        mPhoneNumber = profileDetails.getPhoneNumber();
                        mBirthDate = profileDetails.getBirthDate();
                        mDescription = profileDetails.getdescription();
                    }


                    LanguagesIds = new ArrayList<String>();

                    if(UserLanguage.size() == 0)
                   {
                       DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                       ProfileDetails profileDetailsnew = new ProfileDetails(mUser.getUid(), DefaultLang);
                       mDatabase.child("profileDetails").child(mUser.getUid()).setValue(profileDetailsnew);
                       intSpinnerLanguageStartValue = 0;
                   }else{
                        LanguagesIds = UserLanguage;
                    }


                    PopulateLanguages();



//                    if(UserLanguage == Language.English){
//                    spinnerLanguages.setSelection(0);
//                        intSpinnerLanguageStartValue = 0;
//                    }
//                    if(UserLanguage == Language.Dutch){
//                        spinnerLanguages.setSelection(1);
//                        intSpinnerLanguageStartValue = 1;
//                    }
//                    if(UserLanguage == Language.French){
//                        spinnerLanguages.setSelection(2);
//                        intSpinnerLanguageStartValue = 2;
//                    }
//                    if(UserLanguage == Language.German){
//                        spinnerLanguages.setSelection(3);
//                        intSpinnerLanguageStartValue = 3;
//                    }
//                    else if(UserLanguage == null)
//                    {
//                        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
//                        ProfileDetails profileDetailsnew = new ProfileDetails(mUser.getUid(), Language.English);
//                        mDatabase.child("profileDetails").child(mUser.getUid()).setValue(profileDetailsnew);
//                        intSpinnerLanguageStartValue = 0;
//                    }

                    if(gender == Gender.Male){
                        spinnerGender.setSelection(0);
                        intSpinnerGenderStartValue = 0;
                    }
                    else if(gender == Gender.Female){
                        spinnerGender.setSelection(1);
                        intSpinnerGenderStartValue = 1;
                    }
                    else  if(gender == Gender.Undefined){
                        spinnerGender.setSelection(2);
                        intSpinnerGenderStartValue = 2;
                    }
                    else if(gender == null)
                    {
                        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                        ProfileDetails profileDetailsnew = new ProfileDetails(mUser.getUid(), Gender.Undefined);
                        mDatabase.child("profileDetails").child(mUser.getUid()).setValue(profileDetailsnew);
                        intSpinnerGenderStartValue = 0;
                    }

                    editTextPhoneNumber.setText(mPhoneNumber);
                    editTextBirthDate.setText(mBirthDate);
                    if(mBirthDate != null && !mBirthDate.equals("")) {

                        myCalendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(mBirthDate.substring(0,2)));
                        myCalendar.set(Calendar.MONTH, Integer.parseInt(mBirthDate.substring(3,5)) -1);
                        myCalendar.set(Calendar.YEAR, Integer.parseInt(mBirthDate.substring(6,10)));
                    }
                    editTextDescription.setText(mDescription);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message

                    // ...
                }
            };


            DatabaseReference myRef = database.getReference("profileDetails").child(mUser.getUid());
            myRef.addListenerForSingleValueEvent(postListener);

        }

    }


    private void PopulateLanguages(){
        String[] array = {"English", "Dutch", "German", "Spanish", "Portuguese", "Russian"};
        multiSelectionSpinner.setItems(array);
        multiSelectionSpinner.setSelection(LanguagesIds);
        multiSelectionSpinner.setListener(this);
    }

    private void UploadProfilePic()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Chose profile picture"),1);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void saveDetails()
    {
        Boolean booleanChangeFound = false;
        String name = editTextName.getText().toString();
        Language language = Language.English;
        Gender gender = Gender.Male;
        String phoneNumber = editTextPhoneNumber.getText().toString();
        String birthDate = editTextBirthDate.getText().toString();
        String description = editTextDescription.getText().toString();

        if(name.length() != 0 && ! name.equals(mUser.getDisplayName()))
        {
            booleanChangeFound = true;
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
            if(profileUpdates != null)
            {
                mUser.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(getContext(), "Username failed to change", Toast.LENGTH_SHORT).show();
                                    showDetails();
                                }
                            }
                        });
            }
        }
        if(phoneNumber.length() == 0)
        {
            phoneNumber = "";
        }
        else booleanChangeFound = true;

        if(birthDate.length() == 0){
           birthDate = "";
        } else  booleanChangeFound = true;
        if(description.length() == 0) description = "";else booleanChangeFound= true;

        if(spinnerLanguages.getSelectedItemPosition() != intSpinnerLanguageStartValue) booleanChangeFound = true;

        if(intSpinnerGenderStartValue == spinnerGender.getSelectedItemPosition()) booleanChangeFound = true;



        //Language update
        if(spinnerLanguages.getSelectedItemPosition() == 0)
        {
            language = Language.English;
        }
        else if(spinnerLanguages.getSelectedItemPosition() == 1)
        {
            language = Language.Dutch;
        }
        else if(spinnerLanguages.getSelectedItemPosition() == 2)
        {
            language = Language.French;
        }
        else if(spinnerLanguages.getSelectedItemPosition() == 3)
        {
            language = Language.German;
        }

        //GenderUpdate
        if(spinnerGender.getSelectedItemPosition() == 0)
        {
            gender = Gender.Male;
        }
        if(spinnerGender.getSelectedItemPosition() == 1)
        {
            gender = Gender.Female;
        }
        if(spinnerGender.getSelectedItemPosition() == 2)
        {
            gender = Gender.Undefined;
        }


        if(booleanChangeFound)
        {
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
<<<<<<< HEAD
            ProfileDetails profileDetails = new ProfileDetails(mUser.getUid(), Languages, gender, phoneNumber);
=======
            ProfileDetails profileDetails = new ProfileDetails(mUser.getUid(), language, gender, phoneNumber, birthDate, description);
>>>>>>> origin/develop
            mDatabase.child("profileDetails").child(profileDetails.getProfileId()).setValue(profileDetails);

            Toast.makeText(getContext(), "Changes made", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        editTextBirthDate.setText(dayOfMonth + "/" + monthOfYear + "/" + year);
    }
}
