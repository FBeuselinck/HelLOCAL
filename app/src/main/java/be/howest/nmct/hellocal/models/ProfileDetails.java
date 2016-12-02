package be.howest.nmct.hellocal.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Arno on 14/11/2016.
 */

public class ProfileDetails implements Serializable {


    private String profileId;
    public List<String> language;
    private Gender gender;
    private String phoneNumber;
    private String birthDate;
    private String description;
    private Boolean available;
    private String homeTown;
    private String name;
    private String photoUri;



    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
    public ProfileDetails(String profileId, List<String> language) {
        this.profileId = profileId;
        this.language = language;
    }
    public ProfileDetails (String profileId, List<String> language, Gender gender)
    {
        this.profileId = profileId;
        this.language = language;
        this.gender = gender;
    }
    public ProfileDetails (String profileId, Gender gender)
    {
        this.profileId = profileId;
        this.gender = gender;
    }
    public ProfileDetails()
    {

    }
    public ProfileDetails (String profileId, List<String> language, Gender gender, String phoneNumber)
    {
        this.profileId = profileId;
        this.language = language;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
    }
    public ProfileDetails (String profileId, List<String> language, Gender gender, String phoneNumber, String birthDate){
        this.profileId = profileId;
        this.language = language;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
    }

    public ProfileDetails (String profileId, List<String> language, Gender gender, String phoneNumber, String birthDate, String description){
        this.profileId = profileId;
        this.language = language;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.description = description;
    }

    public ProfileDetails (String profileId, List<String> language, Gender gender, String phoneNumber, String birthDate, String description, Boolean available, String homeTown, String name, String photoUri){
        this.profileId = profileId;
        this.language = language;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.description = description;
        this.available = available;
        this.homeTown=homeTown;
        this.name = name;
        this.photoUri = photoUri;
    }
    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public List<String> getLanguage() {
        return language;
    }

    public void setLanguage(List<String> language) {
        this.language = language;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public String getHomeTown() {
        return homeTown;
    }

    public void setHomeTown(String homeTown) {
        this.homeTown = homeTown;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }
}

