package be.howest.nmct.hellocal;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import be.howest.nmct.hellocal.models.Language;
import be.howest.nmct.hellocal.models.ProfileDetails;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    EditText editTextMail, editTextName;
    Button buttonSave;
    ImageView imageViewProfilePic;
    Spinner spinnerLanguages;
    String stringChangeName = "Change full name here";
    String stringUserId;
    FirebaseUser mUser;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    int intSpinnerStartValue;







    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        editTextMail = (EditText) view.findViewById(R.id.editText_Profile_mail);
        editTextName = (EditText) view.findViewById(R.id.editText_Profile_Name);
        spinnerLanguages = (Spinner) view.findViewById(R.id.spinnerLanguage);

        imageViewProfilePic = (ImageView) view.findViewById(R.id.image_profile);

        buttonSave = (Button) view.findViewById(R.id.btnSave);

        mUser = FirebaseAuth.getInstance().getCurrentUser();

        showDetails();


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

        // Inflate the layout for this fragment
        return view;
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

                    String sUserLanguage = dataSnapshot.getValue(String.class);

                    String sTest = Language.English.toString();
                    if(sUserLanguage.equals(Language.English.toString())){
                    spinnerLanguages.setSelection(0);
                        intSpinnerStartValue = 0;
                    }
                    if(sUserLanguage.equals(Language.Dutch.toString())){
                        spinnerLanguages.setSelection(1);
                        intSpinnerStartValue = 1;
                    }
                    if(sUserLanguage.equals(Language.French.toString())){
                        spinnerLanguages.setSelection(2);
                        intSpinnerStartValue = 2;
                    }
                    if(sUserLanguage.equals(Language.German.toString())){
                        spinnerLanguages.setSelection(3);
                        intSpinnerStartValue = 3;
                    }
                    else if(sUserLanguage == null)
                    {
                        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                        ProfileDetails profileDetails = new ProfileDetails(mUser.getUid(), Language.English);
                        mDatabase.child("profileDetails").child(profileDetails.getProfileId()).setValue(profileDetails);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message

                    // ...
                }
            };

            DatabaseReference myRef = database.getReference("profileDetails").child(mUser.getUid()).child("language");
            myRef.addListenerForSingleValueEvent(postListener);

        }

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
        if(spinnerLanguages.getSelectedItemPosition() != intSpinnerStartValue)
        {
            booleanChangeFound = true;
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            ProfileDetails profileDetails = null;

            if(spinnerLanguages.getSelectedItemPosition() == 0)
            {
                profileDetails = new ProfileDetails(FirebaseAuth.getInstance().getCurrentUser().getUid(), Language.English);
            }
            else if(spinnerLanguages.getSelectedItemPosition() == 1)
            {
                profileDetails = new ProfileDetails(FirebaseAuth.getInstance().getCurrentUser().getUid(), Language.Dutch);
            }
            else if(spinnerLanguages.getSelectedItemPosition() == 2)
            {
                profileDetails = new ProfileDetails(FirebaseAuth.getInstance().getCurrentUser().getUid(), Language.French);
            }
            else if(spinnerLanguages.getSelectedItemPosition() == 3)
            {
                profileDetails = new ProfileDetails(FirebaseAuth.getInstance().getCurrentUser().getUid(), Language.German);
            }
            mDatabase.child("profileDetails").child(profileDetails.getProfileId()).setValue(profileDetails);

        }

        if(booleanChangeFound)
        {
            Toast.makeText(getContext(), "Changes made", Toast.LENGTH_SHORT).show();
        }


    }

}
