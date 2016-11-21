package be.howest.nmct.hellocal;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import be.howest.nmct.hellocal.models.Language;
import be.howest.nmct.hellocal.models.ProfileDetails;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText editTextEmail, editTextPassword;
    private Button buttonSingUp;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView tx = (TextView)findViewById(R.id.appTitle);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/Amethyst.ttf");
        tx.setTypeface(custom_font);

        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.sign_up_button).setOnClickListener(this);

        editTextEmail = (EditText) findViewById(R.id.editTextUseremail);
        editTextPassword = (EditText) findViewById(R.id.edittextPassword);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    LogedIn();
                } else {
                    // User is signed out

                }

            }
        };

    }

    private void LogedIn(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                SignIn();
                break;
            case R.id.sign_up_button:
                SignUp();
                break;
        }
    }

   private void SignIn()
   {
       mAuth.signInWithEmailAndPassword(editTextEmail.getText().toString(), editTextPassword.getText().toString())
               .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {

                       // If sign in fails, display a message to the user. If sign in succeeds
                       // the auth state listener will be notified and logic to handle the
                       // signed in user can be handled in the listener.
                       if (!task.isSuccessful()) {

                           Toast.makeText(LoginActivity.this, "MY MESSAGE NO CORRECT LOGIN",
                                   Toast.LENGTH_SHORT).show();
                       }
                   }
               });
   }

    private void SignUp()
    {
        mAuth.createUserWithEmailAndPassword(editTextEmail.getText().toString(), editTextPassword.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "MY MESSAGE LOGIN FAILED",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else CreateDefaultProfileValues();
                    }
                });
    }
    private void CreateDefaultProfileValues()
    {
        List<String> Test =  new ArrayList<String>();
        Test.add("English");
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        ProfileDetails profileDetails = new ProfileDetails(FirebaseAuth.getInstance().getCurrentUser().getUid(),Test);
        mDatabase.child("profileDetails").child(profileDetails.getProfileId()).setValue(profileDetails);
    }

}

