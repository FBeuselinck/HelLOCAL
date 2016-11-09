package be.howest.nmct.hellocal;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    EditText editTextMail, editTextName;
    Button buttonSave;
    String stringChangeName = "Change full name here";
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

        buttonSave = (Button) view.findViewById(R.id.btnSave);

        mUser = FirebaseAuth.getInstance().getCurrentUser();

        showDetails();


        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDetails();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    private void showDetails()
    {
        if(mUser != null)
        {
            String name = mUser.getDisplayName();
            String email = mUser.getEmail();

            editTextMail.setText(email);
            if(name != null)
            {
                editTextName.setText(name);
            }
            else {
                editTextName.setText("");
                editTextName.setHint(stringChangeName);
            }
        }

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

        if(booleanChangeFound) Toast.makeText(getContext(), "Changes made", Toast.LENGTH_SHORT).show();


    }

}
