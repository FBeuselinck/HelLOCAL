package be.howest.nmct.hellocal;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText editTextEmail, editTextPassword;
    private Button buttonSingUp;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.sign_up_button).setOnClickListener(this);

        editTextEmail = (EditText) findViewById(R.id.editTextUseremail);
        editTextPassword = (EditText) findViewById(R.id.edittextPassword);

        mAuth = FirebaseAuth.getInstance();

    }



    @Override
    public void onStart() {
        super.onStart();
        //mAuth.addAuthStateListener(mAuthListener);
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
       //iput code here
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
                    }
                });
    }

}

