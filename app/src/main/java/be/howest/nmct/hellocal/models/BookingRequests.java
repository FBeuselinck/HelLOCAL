package be.howest.nmct.hellocal.models;


public class BookingRequests {

    private String GuideId;
    private String RequestUserId;
    private Boolean IsConfirmed;
    private String Date;

    public BookingRequests(){

    }

    public BookingRequests(String guideId, String requestUserId, Boolean isConfirmed, String date) {
        GuideId = guideId;
        RequestUserId = requestUserId;
        IsConfirmed = isConfirmed;
        Date = date;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getGuideId() {
        return GuideId;
    }

    public void setGuideId(String guideId) {
        GuideId = guideId;
    }

    public String getRequestUserId() {
        return RequestUserId;
    }

    public void setRequestUserId(String requestUserId) {
        RequestUserId = requestUserId;
    }

    public Boolean getConfirmed() {
        return IsConfirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        IsConfirmed = confirmed;
    }
}
