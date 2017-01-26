package be.howest.nmct.hellocal.auth;


import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.content.Context;

public class AuthHelper {
    private static AccountManager mAccountManager;
    private static AccountAuthenticatorResponse mAccountAuthenticatorResponse;

    public static String getUsername(Context context){
        mAccountManager = AccountManager.get(context);

        Account[] accounts =  mAccountManager.getAccountsByType(Contract.ACCOUNT_TYPE);

        if(accounts.length>0){
            return accounts[0].name;
        }
        else return null;
    }

    public static Boolean isUserLoggedIn(Context context){
        mAccountManager = AccountManager.get(context);
        Account[] accounts =  mAccountManager.getAccountsByType(Contract.ACCOUNT_TYPE);
        if(accounts.length>0){
            return true;
        }
        else return false;

    }

    public static void logUserOff(Context context) {
        mAccountManager = AccountManager.get(context);
        Account[] accounts = mAccountManager.getAccountsByType(Contract.ACCOUNT_TYPE);
        for (int index = 0; index < accounts.length; index++) {
            mAccountManager.removeAccount(accounts[index], null, null,null);
        }

    }
}
