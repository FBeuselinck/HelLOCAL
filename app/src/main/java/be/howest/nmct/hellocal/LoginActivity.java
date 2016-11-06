package be.howest.nmct.hellocal;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.OnAccountsUpdateListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import be.howest.nmct.hellocal.R;
import be.howest.nmct.hellocal.auth.AuthHelper;
import be.howest.nmct.hellocal.auth.Contract;

public class LoginActivity extends AppCompatActivity implements
        View.OnClickListener {

    private static final String TAG = "LoginActivity";

    /* Keys for persisting instance variables in savedInstanceState */
    private static final String KEY_IS_RESOLVING = "is_resolving";
    private static final String KEY_SHOULD_RESOLVE = "should_resolve";


    /* View to display current status (signed-in, signed-out, disconnected, etc) */
    private TextView mStatus;

    // [START resolution_variables]
    /* Is there a ConnectionResult resolution in progress? */
    private boolean mIsResolving = false;

    /* Should we automatically resolve ConnectionResults when possible? */
    private boolean mShouldResolve = false;

    private AppCompatEditText editTextUseremail;
    private AppCompatEditText editTextPassword;
    private LinearLayout linearLayout;
    private String token = "";

    private AccountManager mAccountManager;
    private AccountAuthenticatorResponse mAccountAuthenticatorResponse;
    private String mFullName;
    private String mEmail;

    //listener voor AccountManager -> update UI
    private OnAccountsUpdateListener onAccountsUpdateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up view instances
        mStatus = (TextView) findViewById(R.id.status);
        editTextUseremail = (AppCompatEditText) findViewById(R.id.editTextUseremail);
        editTextPassword = (AppCompatEditText) findViewById(R.id.edittextPassword);
        linearLayout = (LinearLayout) findViewById(R.id.linearlayout1);
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.sign_out_button).setOnClickListener(this);

        mAccountManager = AccountManager.get(this);
        mAccountAuthenticatorResponse = this.getIntent().getParcelableExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE);
        if (mAccountAuthenticatorResponse != null) {
            mAccountAuthenticatorResponse.onRequestContinued();
        }


        if (savedInstanceState != null) {
            mIsResolving = savedInstanceState.getBoolean(KEY_IS_RESOLVING);
            mShouldResolve = savedInstanceState.getBoolean(KEY_SHOULD_RESOLVE);
        }

        updateUI(AuthHelper.isUserLoggedIn(this));
    }



    @Override
    protected void onResume() {
        super.onResume();
        onAccountsUpdateListener = new OnAccountsUpdateListener() {
            @Override
            public void onAccountsUpdated(Account[] accounts) {
                updateUI(AuthHelper.isUserLoggedIn(LoginActivity.this));
            }
        };

        mAccountManager.addOnAccountsUpdatedListener(onAccountsUpdateListener, null, true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (onAccountsUpdateListener != null)
            mAccountManager.removeOnAccountsUpdatedListener(onAccountsUpdateListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                onSignInClicked();
                break;
            case R.id.sign_out_button:
                onSignOut();
                break;
        }
    }

    private void onSignInClicked() {
        mEmail = editTextUseremail.getText().toString();
        String passwordString = editTextPassword.getText().toString();
        addAccount(mEmail);
    }


    private void onSignOut() {
        AuthHelper.logUserOff(this);

        Snackbar snackbar = Snackbar
                .make(linearLayout, "Logged out successfully", Snackbar.LENGTH_LONG)
                .setAction("Close app?", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ActivityCompat.finishAffinity(LoginActivity.this);
                    }
                });
        snackbar.show();

    }


    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            mStatus.setVisibility(View.VISIBLE);
            mStatus.setText(AuthHelper.getUsername(this) + " is logged in.");
            editTextUseremail.setVisibility(View.GONE);
            editTextPassword.setVisibility(View.GONE);
            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            findViewById(R.id.sign_out_button).setVisibility(View.VISIBLE);
        } else {
            // Show signed-out message and clear email field
            mStatus.setText(R.string.signed_out);
            mStatus.setVisibility(View.GONE);
            editTextUseremail.setVisibility(View.VISIBLE);
            editTextPassword.setVisibility(View.VISIBLE);
            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_button).setVisibility(View.GONE);

            editTextUseremail.setText("");
            editTextPassword.setText("");
            mStatus.setText("");
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_IS_RESOLVING, mIsResolving);
        outState.putBoolean(KEY_SHOULD_RESOLVE, mShouldResolve);
    }


    private void addAccount(String userNameString) {
        Account[] accountsByType = mAccountManager.getAccountsByType(Contract.ACCOUNT_TYPE);
        Account account;
        if (accountsByType.length == 0) {
            // nog geen account aanwezig
            account = new Account(userNameString, Contract.ACCOUNT_TYPE);
            mAccountManager.addAccountExplicitly(account, null, null);
        } else if (!userNameString.equals(accountsByType[0].name)) {
            // er bestaat reeds een account met andere naam
            mAccountManager.removeAccount(accountsByType[0], this, null, null);
            account = new Account(userNameString, Contract.ACCOUNT_TYPE);
            mAccountManager.addAccountExplicitly(account, null, null);
        } else {
            // account met de zelfde username terug gevonden
            account = accountsByType[0];
        }

        Intent intent = new Intent();
        intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, userNameString);
        intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, Contract.ACCOUNT_TYPE);

        if (mAccountAuthenticatorResponse != null) {
            Bundle bundle = intent.getExtras();
            bundle.putString(AccountManager.KEY_ACCOUNT_NAME, userNameString);
            bundle.putString(AccountManager.KEY_ACCOUNT_TYPE, Contract.ACCOUNT_TYPE);
            mAccountAuthenticatorResponse.onResult(bundle);
        }

        setResult(RESULT_OK, intent);
        finish();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //als user niet meer ingelogd is, en de gebruiker drukt op back-button ~> app laten afsluiten
        if (!AuthHelper.isUserLoggedIn(this)) {
            ActivityCompat.finishAffinity(LoginActivity.this);
        }
    }
}

