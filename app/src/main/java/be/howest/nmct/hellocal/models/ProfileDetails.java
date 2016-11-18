package be.howest.nmct.hellocal.models;

/**
 * Created by Arno on 14/11/2016.
 */

public class ProfileDetails {

    public String profileId;
    public Language language;



    public ProfileDetails(String profileId, Language language) {
        this.profileId = profileId;
        this.language = language;
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
}
