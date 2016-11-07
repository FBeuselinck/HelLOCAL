package be.howest.nmct.hellocal.models;

import com.google.firebase.database.GenericTypeIndicator;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Simon on 7/11/2016.
 */

public class AvaiableGuides {
    public String name;
    public String location;
    public String dateFrom;
    public String dateTill;
    public String maxPeople;
    public String price;
    public String type;
    public String transport;
    public String language;


    public AvaiableGuides(){

    }

    public AvaiableGuides(String name, String location, String dateFrom, String dateTill, String maxPeople, String price, String type, String transport, String language){
        this.name=name;
        this.location=location;
        this.dateFrom=dateFrom;
        this.dateTill=dateTill;
        this.maxPeople=maxPeople;
        this.price=price;
        this.type=type;
        this.transport=transport;
        this.language=language;
    }

    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public String getDateTill() {
        return dateTill;
    }

    public String getLanguage() {
        return language;
    }

    public String getLocation() {
        return location;
    }

    public String getMaxPeople() {
        return maxPeople;
    }

    public String getPrice() {
        return price;
    }

    public String getTransport() {
        return transport;
    }

    public String getType() {
        return type;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public void setDateTill(String dateTill) {
        this.dateTill = dateTill;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setMaxPeople(String maxPeople) {
        this.maxPeople = maxPeople;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setTransport(String transport) {
        this.transport = transport;
    }

    public void setType(String type) {
        this.type = type;
    }
}




