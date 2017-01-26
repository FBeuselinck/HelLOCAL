package be.howest.nmct.hellocal.models;

import android.net.Uri;

/**
 * Created by Simon on 16/11/2016.
 */

public class User {
    public String name;
    public String email;
    public Uri photoUrl;
    public String Uid;

    public User(){

    }

    public User(String email, String name, Uri photoUrl, String uid) {
        this.email = email;
        this.name = name;
        this.photoUrl = photoUrl;
        Uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Uri getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(Uri photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }
}
