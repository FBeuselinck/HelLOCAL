package be.howest.nmct.hellocal.models;


import java.util.Date;

public class Message {
    public Date date;
    public String msg;
    public String photoUrl;
    public String receiver;
    public String sender;
    public Boolean sent;
    public int status;

    public Message() {}

    public Message(Date date, String msg, String photoUrl, String receiver, String sender, Boolean sent, int status){
        this.date = date;
        this.msg = msg;
        this.photoUrl = photoUrl;
        this.receiver = receiver;
        this.sender = sender;
        this.sent = sent;
        this.status = status;
    }

    public Date getDate() {
        return this.date;
    }
    public void setDate(Date date) {
        this.date = date;
    }

    public String getMsg() {
        return this.msg;
    }
    public void setMsg(String msg){this.msg = msg;}

    public String getPhotoUrl() {
        return this.photoUrl;
    }
    public void setPhotoUrl(String photoUrl){this.photoUrl = photoUrl;}

    public String getReceiver() {
        return this.receiver;
    }
    public void setReceiver(String receiver){this.receiver = receiver;}

    public String getSender() {
        return this.sender;
    }
    public void setSender(String sender){this.sender = sender;}

    public Boolean getSent() {
        return this.sent;
    }
    public void setSent(Boolean sent){this.sent = sent;}

    public int getStatus() {
        return this.status;
    }
    public void setStatus(int status){this.status = status;}
}
