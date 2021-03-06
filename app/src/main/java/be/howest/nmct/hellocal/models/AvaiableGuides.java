package be.howest.nmct.hellocal.models;

import java.util.ArrayList;

/**
 * Created by Simon on 7/11/2016.
 */

public class AvaiableGuides {
    public String name;
    public String country;
    public String location;
    public String dateFrom;
    public String dateTill;
    public String maxPeople;
    public String price;
    public ArrayList<String> type;
    public String transport;
    public String userId;
    public String photoUri;
    private String Id;
    public String key;
    public Boolean canBeBooked;

//    public String combined;


    public AvaiableGuides(){

    }

    public AvaiableGuides(String name,String country, String location, String dateFrom, String dateTill, String maxPeople, String price, ArrayList<String> type, String transport,  String userId, String photoUri){
        this.name=name;
        this.country = country;
        this.location=location;
        this.dateFrom=dateFrom;
        this.dateTill=dateTill;
        this.maxPeople=maxPeople;
        this.price=price;
        this.type=type;
        this.transport=transport;
        this.userId = userId;
        this.photoUri = photoUri;
//        this.combined=combined;
    }

    public AvaiableGuides(String name,String country, String location, String dateFrom, String dateTill, String maxPeople, String price, ArrayList<String> type, String transport,  String userId, String photoUri, boolean canBeBooked){
        this.name=name;
        this.country = country;
        this.location=location;
        this.dateFrom=dateFrom;
        this.dateTill=dateTill;
        this.maxPeople=maxPeople;
        this.price=price;
        this.type=type;
        this.transport=transport;
        this.userId = userId;
        this.photoUri = photoUri;
        this.canBeBooked = canBeBooked;
//        this.combined=combined;
    }



    public AvaiableGuides(String name,String country, String location, String dateFrom, String dateTill, String maxPeople, String price, ArrayList<String> type, String transport,  String userId, String photoUri, String key, Boolean canBeBooked){
        this.name=name;
        this.country = country;
        this.location=location;
        this.dateFrom=dateFrom;
        this.dateTill=dateTill;
        this.maxPeople=maxPeople;
        this.price=price;
        this.type=type;
        this.transport=transport;
        this.userId = userId;
        this.photoUri = photoUri;
        this.key=key;
        this.canBeBooked = canBeBooked;
//        this.combined=combined;
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

    public ArrayList<String> getType() {
        return type;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public void setDateTill(String dateTill) {
        this.dateTill = dateTill;
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

    public void setType(ArrayList<String> type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

    //    public String getLocationByLocation(String Location){
//        if(location.equals(Location)){
//            return location;
//        }
//        return "false";
//    }


//    public String getCombined() {
//        return combined;
//    }
//
//    public void setCombined(String combined) {
//        this.combined = combined;
//    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Boolean getCanBeBooked() {
        return canBeBooked;
    }

    public void setCanBeBooked(Boolean canBeBooked) {
        this.canBeBooked = canBeBooked;
    }
}




