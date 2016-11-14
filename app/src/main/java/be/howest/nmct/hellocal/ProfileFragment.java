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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    EditText editTextMail, editTextName;
    Button buttonSave;
    ImageView imageViewProfilePic;
    String stringChangeName = "Change full name here";
    String stringUserId;
    FirebaseUser mUser;




    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        editTextMail = (EditText) view.findViewById(R.id.editText_Profile_mail);
        editTextName = (EditText) view.findViewById(R.id.editText_Profile_Name);

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

    private void showDetails()
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


        }

    }


    private void UploadProfilePic()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Chose profile picture"),1);
    }


    private void saveDetails()
    {
        Boolean booleanChangeFound = false;
        String name = editTextName.getText().toString();

        if(name.length() != 0)
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
                                }
                            }
                        });
            }

        }

        if(booleanChangeFound)
        {
            Toast.makeText(getContext(), "Changes made", Toast.LENGTH_SHORT).show();
            showDetails();
        }


    }

}
