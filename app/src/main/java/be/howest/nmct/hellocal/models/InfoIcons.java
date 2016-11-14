package be.howest.nmct.hellocal.models;

import android.media.Image;

/**
 * Created by Simon on 14/11/2016.
 */

public class InfoIcons {

    public Image active;
    public Image culture;
    public Image city;
    public Image smthElse;
    public Image transport;
    public Image language1;
    public Image language2;


    public InfoIcons(){

    }


    public InfoIcons(Image active, Image city, Image culture, Image language1, Image language2, Image smthElse, Image transport) {
        this.active = active;
        this.city = city;
        this.culture = culture;
        this.language1 = language1;
        this.language2 = language2;
        this.smthElse = smthElse;
        this.transport = transport;
    }


    public Image getActive() {
        return active;
    }

    public void setActive(Image active) {
        this.active = active;
    }

    public Image getCity() {
        return city;
    }

    public void setCity(Image city) {
        this.city = city;
    }

    public Image getCulture() {
        return culture;
    }

    public void setCulture(Image culture) {
        this.culture = culture;
    }

    public Image getLanguage1() {
        return language1;
    }

    public void setLanguage1(Image language1) {
        this.language1 = language1;
    }

    public Image getLanguage2() {
        return language2;
    }

    public void setLanguage2(Image language2) {
        this.language2 = language2;
    }

    public Image getSmthElse() {
        return smthElse;
    }

    public void setSmthElse(Image smthElse) {
        this.smthElse = smthElse;
    }

    public Image getTransport() {
        return transport;
    }

    public void setTransport(Image transport) {
        this.transport = transport;
    }
}
