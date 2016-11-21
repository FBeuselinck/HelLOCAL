package be.howest.nmct.hellocal.models;

/**
 * Created by Arno on 14/11/2016.
 */

public class ProfileDetails {

    private String profileId;
    public Language language;
    private Gender gender;
    private String phoneNumber;
    private String birthDate;
    private String description;



    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
    public ProfileDetails(String profileId, Language language) {
        this.profileId = profileId;
        this.language = language;
    }
    public ProfileDetails (String profileId, Language language, Gender gender)
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
    public ProfileDetails (String profileId, Language language, Gender gender, String phoneNumber)
    {
        this.profileId = profileId;
        this.language = language;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
    }
    public ProfileDetails (String profileId, Language language, Gender gender, String phoneNumber, String birthDate){
        this.profileId = profileId;
        this.language = language;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
    }

    public ProfileDetails (String profileId, Language language, Gender gender, String phoneNumber, String birthDate, String description){
        this.profileId = profileId;
        this.language = language;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.description = description;
    }



    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
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

    public String getdescription() {
        return description;
    }

    public void setdescription(String description) {
        description = description;
    }

}

