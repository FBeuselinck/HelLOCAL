package be.howest.nmct.hellocal.auth;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AuthenticatorService extends Service {

    private Authenticator mAuthenticator;

    public AuthenticatorService(){}

    //The system calls this method when the service is first created, to perform one-time setup procedures
    @Override
    public void onCreate() {
        super.onCreate();

        mAuthenticator = new Authenticator(this);
    }

    //The system calls this method when another component wants to bind with the service
    //IBinder: you must provide an interface that clients use to communicate with the service, by returning an IBinder
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }


}
